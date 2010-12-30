/* AbstractPoolSheet.java
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

import be.rheynaerde.poolsheets.boutorder.BoutOrder;
import be.rheynaerde.poolsheets.configuration.AbstractPoolSheetConfiguration;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractPoolSheet {

    private byte[] sheet;

    private AbstractPoolSheetConfiguration configuration;

    public AbstractPoolSheet(AbstractPoolSheetConfiguration configuration) throws DocumentException{
        this.configuration = configuration;
    }

    /*
     * Construct pool sheet and export it to the byte array sheet. The
     * construction is delegated to three separate methods: buildTitle,
     * buildTable and buildBoutOrder.
     */
    protected void buildSheet() throws DocumentException {
        Rectangle pageSize = configuration.isLandscape(AbstractPoolSheetConfiguration.TITLE_PAGE) ? PageSize.A4.rotate() : PageSize.A4;
        Document document = new Document(pageSize);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        buildTitle(document);
        buildTable(document);

        if(configuration.includeOrderOfBouts()){
            if(configuration.putBoutOrderOnNewPage()){
                pageSize = configuration.isLandscape(AbstractPoolSheetConfiguration.BOUT_ORDER_PAGE) ? PageSize.A4.rotate() : PageSize.A4;
                document.setPageSize(pageSize);
                document.newPage();
            }

            buildBoutOrder(document);
        }
        
        document.close();
        sheet = baos.toByteArray();
    }

    protected void buildTitle(Document document) throws DocumentException {
        //the title is placed in a one-column table of width 100%
        PdfPTable table = new PdfPTable(1);
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        table.setWidthPercentage(100f);

        //one row for the title
        PdfPCell cell = new PdfPCell(new Phrase(configuration.getTitle(), configuration.getTitleFont()));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(10f);
        table.addCell(cell);

        //one row for the subtitle
        cell = new PdfPCell(new Phrase(configuration.getSubtitle(), configuration.getSubtitleFont()));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(Rectangle.TOP);
        table.addCell(cell);

        //make sure that there is some spacing between the title and the scores
        //20f just seems to work allright, but maybe something more configurable
        //would be better.
        table.setSpacingAfter(20f);
        document.add(table);
    }

    abstract protected void buildTable(Document document) throws DocumentException;

    protected void buildBoutOrder(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(configuration.getBoutOrderColumns());
        BoutOrder mo = configuration.getBoutOrder();
        if(mo.getNrOfBouts()==0) return;
        int rows = mo.getNrOfBouts()/configuration.getBoutOrderColumns();
        if(mo.getNrOfBouts()%configuration.getBoutOrderColumns()!=0)
            rows++;
        int shortComing = (rows - (mo.getNrOfBouts()%rows))%rows;
        for (int i = 0; i < rows - shortComing; i++) {
            for (int j = 0; j < configuration.getBoutOrderColumns(); j++) {
                int boutNumber = j*rows + i;
                table.addCell(getBoutCell(mo, boutNumber));
            }
            for (int j = 0; j < configuration.getBoutOrderSpacing(); j++) {
                for (int k = 0; k < configuration.getBoutOrderColumns(); k++) {
                    PdfPCell spacing = new PdfPCell();
                    spacing.setBorder(Rectangle.NO_BORDER);
                    spacing.setFixedHeight(configuration.getSquareCellSize());
                    table.addCell(spacing);
                }
            }
        }
        for (int i = rows - shortComing; i < rows; i++) {
            for (int j = 0; j < configuration.getBoutOrderColumns()-1; j++) {
                int boutNumber = j*rows + i;
                table.addCell(getBoutCell(mo, boutNumber));
            }
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            for (int j = 0; j < configuration.getBoutOrderSpacing(); j++) {
                for (int k = 0; k < configuration.getBoutOrderColumns(); k++) {
                    PdfPCell spacing = new PdfPCell();
                    spacing.setBorder(Rectangle.NO_BORDER);
                    spacing.setFixedHeight(configuration.getSquareCellSize());
                    table.addCell(spacing);
                }
            }
        }
        table.setSpacingBefore(20f);
        document.add(table);
    }

    protected PdfPCell getBoutCell(BoutOrder mo, int boutNumber) {
        PdfPCell cell = new PdfPCell();
        int firstPlayerOfBout = mo.getFirstPlayerOfBout(boutNumber);
        int secondPlayerOfBout = mo.getSecondPlayerOfBout(boutNumber);
        cell.addElement(getBout(Integer.toString(firstPlayerOfBout), Integer.toString(secondPlayerOfBout)));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    protected PdfPTable getBout(String player1, String player2){
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(configuration.getSquareCellSize()*2);
        PdfPCell player1Name = new PdfPCell(new Phrase(player1));
        player1Name.setBorder(Rectangle.BOTTOM);
        player1Name.setHorizontalAlignment(Element.ALIGN_CENTER);
        player1Name.setFixedHeight(configuration.getSquareCellSize());
        PdfPCell player2Name = new PdfPCell(new Phrase(player2));
        player2Name.setBorder(Rectangle.BOTTOM);
        player2Name.setHorizontalAlignment(Element.ALIGN_CENTER);
        player2Name.setFixedHeight(configuration.getSquareCellSize());
        PdfPCell player1Score = new PdfPCell(new Phrase(" "));
        player1Score.setBorder(Rectangle.RIGHT);
        player1Score.setFixedHeight(configuration.getSquareCellSize());
        PdfPCell player2Score = new PdfPCell(new Phrase(" "));
        player2Score.setBorder(Rectangle.LEFT);
        player2Score.setFixedHeight(configuration.getSquareCellSize());
        table.addCell(player1Name);
        table.addCell(player2Name);
        table.addCell(player1Score);
        table.addCell(player2Score);
        table.setSpacingBefore(10);
        return table;
    }

    protected PdfPCell getHeaderCell(String text){
        PdfPCell headerCell =
                new PdfPCell(
                    new Phrase(text, configuration.getHeaderFont())
                );
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerCell.setFixedHeight(configuration.getSquareCellSize());
        return headerCell;
    }

    protected PdfPCell getBlackCell(){
        if(configuration.getImage()==null){
            PdfPCell blackCell = new PdfPCell();
            blackCell.setBackgroundColor(BaseColor.BLACK);
            blackCell.setFixedHeight(configuration.getSquareCellSize());
            return blackCell;
        } else {
            PdfPCell blackCell = new PdfPCell(configuration.getImage(), true);
            blackCell.setFixedHeight(configuration.getSquareCellSize());
            return blackCell;
        }
    }

    public void export(OutputStream os) throws DocumentException, IOException{
        os.write(sheet);
    }
}
