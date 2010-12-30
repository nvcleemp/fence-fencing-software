/* StandardPoolSheet.java
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

import be.rheynaerde.poolsheets.configuration.StandardPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.DefaultStandardPoolSheetConfiguration;
import be.rheynaerde.poolsheets.boutorder.BoutOrder;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ResourceBundle;

/**
 *
 * @author nvcleemp
 */
public class StandardPoolSheet extends AbstractPoolSheet {

    private StandardPoolSheetConfiguration configuration;

    private ResourceBundle bundle;

    public StandardPoolSheet(int nrOfPlayers) throws DocumentException{
        this(nrOfPlayers, 20f, null);
    }

    public StandardPoolSheet(int nrOfPlayers, Image image) throws DocumentException{
        this(nrOfPlayers, 20f, image);
    }

    public StandardPoolSheet(int nrOfPlayers, float squareCellSize, Image image) throws DocumentException{
        this(new DefaultStandardPoolSheetConfiguration(nrOfPlayers, squareCellSize, image));
    }
    
    public StandardPoolSheet(StandardPoolSheetConfiguration configuration) throws DocumentException{
        super(configuration);
        this.configuration = configuration;
        this.bundle =
                ResourceBundle.getBundle("be.rheynaerde.poolsheets.standardpoolsheet",
                                        configuration.getLocale());
        buildSheet();
    }

    protected void buildTable(Document document) throws DocumentException {
        int columnCount = 1 + 1 + configuration.getNrOfPlayers()+ 1
                            + configuration.getSummaryColumnCount();
                      // name + number + scores + spacer 
                      //    + summary
        PdfPTable table = new PdfPTable(columnCount);
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

        table.setTotalWidth(
                (configuration.getNrOfPlayers()
                   + 7 //the name column + extra space between grid and summary
                       //currently the name column just gets a width that is
                       //a multiple of the width of the other cells. Better
                       //would be to let this cell take up the remaining space.
                   + configuration.getSummaryColumnCount())
                   *configuration.getSquareCellSize());
        table.setLockedWidth(true);
        float[] widths =
                new float[columnCount];
        widths[0] = 5f;
        for (int i = 1; i < widths.length; i++) {
            widths[i] = 1f;
        }
        widths[widths.length-1-configuration.getSummaryColumnCount()] = 0.1f;
        table.setWidths(widths);

        {
            table.addCell(getHeaderCell(bundle.getString("name")));
            table.addCell(getBlackCell());
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
                PdfPCell cell = new PdfPCell(new Phrase(result==null ? "" : result));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            table.addCell(getBlackCell());
            for(int j = i+1; j < configuration.getNrOfPlayers(); j++){
                String result = configuration.getResult(i+1, j+1);
                PdfPCell cell = new PdfPCell(new Phrase(result==null ? "" : result));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            table.addCell(new PdfPCell()); //spacer column
            for(int j = 0; j < configuration.getSummaryColumnCount(); j++){
                String result = configuration.getSummaryColumnValue(i+1, j);
                PdfPCell cell = new PdfPCell(new Phrase(result==null ? "" : result));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
        }
        document.add(table);
    }

    @Override
    protected PdfPCell getBoutCell(BoutOrder mo, int boutNumber) {
        PdfPCell cell = new PdfPCell();
        int firstPlayerOfBout = mo.getFirstPlayerOfBout(boutNumber);
        int secondPlayerOfBout = mo.getSecondPlayerOfBout(boutNumber);
        String player1 = configuration.getNamePlayer(firstPlayerOfBout);
        String player2 = configuration.getNamePlayer(secondPlayerOfBout);
        cell.addElement(getBout(player1 != null ? player1 : Integer.toString(firstPlayerOfBout), player2 != null ? player2 : Integer.toString(secondPlayerOfBout)));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}
