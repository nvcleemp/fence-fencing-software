/* CreateRoundsPdfAction.java
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

package be.rheynaerde.pufmanager.gui.actions;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Team;
import be.rheynaerde.pufmanager.data.listener.CompetitionAdapter;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;
import be.rheynaerde.pufmanager.gui.workers.ExportFullPdfWorker;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nvcleemp
 */
public class CreateRoundsPdfAction extends AbstractAction {

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.actions");

    private Competition competition;

    private CompetitionListener competitionListener = new CompetitionAdapter() {

        @Override
        public void roundsChanged() {
            setEnabled();
        }
    };

    private JFrame parent;

    private JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
    
    public CreateRoundsPdfAction(Competition competition) {
        super(BUNDLE.getString("create.rounds.pdf"));
        this.competition = competition;
        competition.addListener(competitionListener);
        chooser.setFileFilter(new FileNameExtensionFilter(BUNDLE.getString("pdf.file.filter"), "pdf"));
        setEnabled();
    }

    public void actionPerformed(ActionEvent e) {
        if(chooser.showSaveDialog(parent)==JFileChooser.APPROVE_OPTION){
            new ExportFullPdfWorker(chooser.getSelectedFile(), competition).startExport(parent);
        }
    }

    protected void setEnabled(){
        setEnabled(competition.getRoundCount()>0);
    }
}
