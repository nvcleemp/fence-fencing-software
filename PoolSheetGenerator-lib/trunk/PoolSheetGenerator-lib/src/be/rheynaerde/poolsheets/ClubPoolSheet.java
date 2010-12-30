/* ClubPoolSheet.java
 * =========================================================================
 * This file is part of the PoolSheetGenerator project
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

package be.rheynaerde.poolsheets;

import be.rheynaerde.poolsheets.configuration.ClubPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.DefaultClubPoolSheetConfiguration;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author nvcleemp
 */
public class ClubPoolSheet extends AbstractPoolSheet{

    private ClubPoolSheetConfiguration configuration;
    private ResourceBundle bundle;

    public ClubPoolSheet(int nrOfPlayers) throws DocumentException{
        this(nrOfPlayers, 20f, null);
    }

    public ClubPoolSheet(int nrOfPlayers, Image image) throws DocumentException{
        this(nrOfPlayers, 20f, image);
    }

    public ClubPoolSheet(int nrOfPlayers, float squareCellSize, Image image) throws DocumentException{
        this(nrOfPlayers, squareCellSize, image, null);
    }

    public ClubPoolSheet(int nrOfPlayers, float squareCellSize, Image image, String title) throws DocumentException{
        this(nrOfPlayers, squareCellSize, image, Locale.getDefault(), title);
    }

    public ClubPoolSheet(int nrOfPlayers, float squareCellSize, Image image, Locale locale, String title) throws DocumentException{
        this(new DefaultClubPoolSheetConfiguration(nrOfPlayers, squareCellSize, image, locale, title));
    }

    public ClubPoolSheet(ClubPoolSheetConfiguration configuration) throws DocumentException{
        super(configuration);
        this.configuration = configuration;
        this.bundle =
                ResourceBundle.getBundle("be.rheynaerde.poolsheets.clubpoolsheet",
                                        configuration.getLocale());
        buildSheet();
    }

    @Override
    protected void buildTitle(Document document) throws DocumentException {
        //do nothing
    }

    @Override
    protected void buildTable(Document document) throws DocumentException {
        int columnCount = configuration.getNrOfPlayers() + configuration.getSummaryColumnCount() + 3;
        int nameCellWidth = 5;

        PdfPTable table = new PdfPTable(columnCount);
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        table.setTotalWidth((configuration.getNrOfPlayers() + 1 + nameCellWidth + 0.1f + configuration.getSummaryColumnCount())*configuration.getSquareCellSize());
        table.setLockedWidth(true);
        float[] widths = new float[columnCount];
        widths[0] = 1f*nameCellWidth;
        for (int i = 1; i < widths.length; i++) {
            widths[i] = 1f;
        }
        widths[widths.length-1-configuration.getSummaryColumnCount()] = 0.1f;
        table.setWidths(widths);

        PdfPCell cell = new PdfPCell(new Paragraph(configuration.getTitle(), configuration.getTitleFont()));
	cell.setColspan(columnCount);
        cell.setPaddingBottom(configuration.getSquareCellSize()/2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	table.addCell(cell);

        {
            table.addCell(getHeaderCell(bundle.getString("name")));
            table.addCell(getSolidCell());
            for (int i = 0; i < configuration.getNrOfPlayers(); i++) {
                table.addCell(getHeaderCell(Integer.toString(i+1)));
            }
            table.addCell(new PdfPCell()); //spacer column
            for (int i = 0; i < configuration.getSummaryColumnCount(); i++) {
                table.addCell(getHeaderCell(configuration.getSummaryColumnName(i)));
            }
        }

        for (int i = 0; i < configuration.getNrOfPlayers(); i++) {
            table.addCell(configuration.getNamePlayer(i+1)==null ? "" : configuration.getNamePlayer(i+1));
            table.addCell(getHeaderCell(Integer.toString(i+1)));
            for(int j = 0; j < i; j++){
                String result = configuration.getResult(i+1, j+1);
                PdfPCell resultCell = new PdfPCell(new Phrase(result==null ? "" : result));
                resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(resultCell);
            }
            table.addCell(getSolidCell());
            for(int j = i+1; j < configuration.getNrOfPlayers(); j++){
                String result = configuration.getResult(i+1, j+1);
                PdfPCell resultCell = new PdfPCell(new Phrase(result==null ? "" : result));
                resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(resultCell);
            }
            table.addCell(new PdfPCell()); //spacer column
            for(int j = 0; j < configuration.getSummaryColumnCount(); j++){
                String result = configuration.getSummaryColumnValue(i+1, j);
                PdfPCell summaryCell = new PdfPCell(new Phrase(result==null ? "" : result));
                summaryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                summaryCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(summaryCell);
            }
        }

        document.add(table);
    }

    @Override
    protected void buildBoutOrder(Document document) throws DocumentException {
        //do nothing
    }

}
