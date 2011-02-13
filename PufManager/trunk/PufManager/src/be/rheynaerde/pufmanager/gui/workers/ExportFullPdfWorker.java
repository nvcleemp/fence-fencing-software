/* ExportFullPdfWorker.java
 * =========================================================================
 * This file is part of the Fence project
 * More info can be found at http://nvcleemp.wordpress.com
 * 
 * Copyright (C) 2010-2011 Nico Van Cleemput
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package be.rheynaerde.pufmanager.gui.workers;

import be.rheynaerde.poolsheets.PufCompletePoolSheet;
import be.rheynaerde.poolsheets.PufSingleTeamPoolSheet;
import be.rheynaerde.poolsheets.PufTeamPoolSheet;
import be.rheynaerde.poolsheets.configuration.PufCompletePoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.PufSingleTeamPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.PufTeamPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.NamedPufCompletePoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.NamedPufSingleTeamPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.NamedPufTeamPoolSheetConfiguration;
import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Match;
import be.rheynaerde.pufmanager.data.Round;
import be.rheynaerde.pufmanager.data.Team;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import java.awt.Component;
import java.awt.event.ActionEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

/**
 *
 * @author nvcleemp
 */
public class ExportFullPdfWorker extends SwingWorker<byte[], Integer>{

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.workers");

    private File file;

    private ProgressMonitor progressMonitor;

    private Competition competition;

    public ExportFullPdfWorker(File file, Competition competition) {
        this.file = file;
        this.competition = competition;
    }

    public void startExport(Component parent){
        progressMonitor = new ProgressMonitor(parent, BUNDLE.getString("ExportFullPdf.monitor.message"), null, 0, competition.getRoundCount()*10);
        execute();
    }

    @Override
    protected void done() {
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(get());
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(ExportFullPdfWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExportFullPdfWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ExportFullPdfWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void process(List<Integer> list) {
        int max = 0;
        for (Integer integer : list) {
            if(max < integer)
                max = integer;
        }
        progressMonitor.setProgress(max*10);
        if(max < competition.getRoundCount())
            progressMonitor.setNote(String.format(BUNDLE.getString("ExportFullPdf.monitor.note"),max));
    }

    @Override
    protected byte[] doInBackground() throws Exception {
        ByteArrayOutputStream largeBaos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, largeBaos);
        document.open();

        String stampText =
                ResourceBundle.getBundle
                    ("be.rheynaerde.pufmanager.gui.export",
                     competition.getSettings().getLocale()
                    ).getString("ExportFullPdf.poolsheet.stamp");

        for (int i = 0; i < competition.getRoundCount(); i++) {
            publish(i);
            Round round = competition.getRound(i);
            int piste = 1;
            for (Match match : round.getMatches()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(match.getRound().includeInternalBouts()){
                    PufCompletePoolSheetConfiguration config =
                            new NamedPufCompletePoolSheetConfiguration(
                                match.getFirstTeam().getFencerNames(),
                                match.getSecondTeam().getFencerNames(),
                                20f, competition.getSettings().getImage(), competition.getSettings().getLocale(),
                                competition.getSettings().getTitle(), competition.getSettings().getSubtitle());
                    PufCompletePoolSheet sheet = new PufCompletePoolSheet(config);
                    sheet.export(baos);
                } else {
                    PufTeamPoolSheetConfiguration config =
                            new NamedPufTeamPoolSheetConfiguration(
                                match.getFirstTeam().getFencerNames(),
                                match.getSecondTeam().getFencerNames(),
                                20f, competition.getSettings().getImage(), competition.getSettings().getLocale(),
                                competition.getSettings().getTitle(), competition.getSettings().getSubtitle());
                    PufTeamPoolSheet sheet = new PufTeamPoolSheet(config);
                    sheet.export(baos);

                }
                addPdfToDocument(baos, copy, String.format(stampText, i+1, piste));
                piste++;
            }
            if(i==0){
                for (Team team : round.getRestingTeams()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PufSingleTeamPoolSheetConfiguration config =
                            new NamedPufSingleTeamPoolSheetConfiguration(
                                team.getFencerNames(),
                                20f, competition.getSettings().getImage(), competition.getSettings().getLocale(),
                                competition.getSettings().getTitle(), competition.getSettings().getSubtitle());
                    PufSingleTeamPoolSheet sheet = new PufSingleTeamPoolSheet(config);
                    sheet.export(baos);
                    addPdfToDocument(baos, copy, String.format(stampText, i+1, piste));
                    piste++;
                }
            }
        }

        document.close();
        publish(competition.getRoundCount());

        return largeBaos.toByteArray();
    }

    private void addPdfToDocument(ByteArrayOutputStream baos, PdfCopy copy, String stampText) throws IOException, BadPdfFormatException {
        PdfReader reader = new PdfReader(new ByteArrayInputStream(baos.toByteArray()));
        int n = reader.getNumberOfPages();
        for (int page = 1; page <= n; page++) {
            PdfImportedPage importedPage = copy.getImportedPage(reader, page);
            PdfCopy.PageStamp stamp = copy.createPageStamp(importedPage);
            Rectangle rectangle = importedPage.getBoundingBox();
            ColumnText.showTextAligned(
                    stamp.getUnderContent(),
                    Element.ALIGN_RIGHT,
                    new Phrase(stampText),
                    rectangle.getRight()-20f, rectangle.getTop()-20f, 0);
            stamp.alterContents();
            copy.addPage(importedPage);
        }
    }

    public static void main(String[] args) throws DocumentException {
        final java.util.List<be.rheynaerde.pufmanager.data.Team> teams =
                new java.util.ArrayList<be.rheynaerde.pufmanager.data.Team>();
        for (int i = 0; i < 7; i++) {
            String teamName = "Team " + Character.toString((char) ('A' + i));
            List<Fencer> fencers = new ArrayList<Fencer>();
            for (int j = 0; j < 3; j++) {
                fencers.add(new Fencer(teamName + " " + (j+1)));
            }
            teams.add(new be.rheynaerde.pufmanager.data.Team
                    (teamName, fencers));
        }
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JButton button = new JButton();
        button.setAction(new AbstractAction("export") {

            public void actionPerformed(ActionEvent e) {
                new ExportFullPdfWorker(new File("puf.pdf"), new Competition(teams)).startExport(button);
            }
        });
        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }
}
