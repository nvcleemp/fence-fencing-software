/* AbstractPufPoolSheet.java
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

import be.rheynaerde.poolsheets.configuration.AbstractPoolSheetConfiguration;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractPufPoolSheet extends AbstractPoolSheet {

    protected static final BaseColor PUF_POOL_SHEET_GRAY = new BaseColor(224, 224, 224);

    private AbstractPoolSheetConfiguration configuration;

    public AbstractPufPoolSheet(AbstractPoolSheetConfiguration configuration) throws DocumentException {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    protected void buildTable(Document document) throws DocumentException {
        //large table to take care of the layout
        PdfPTable largeTable = new PdfPTable(3);
        largeTable.setWidths(new int[]{19, 1, 20});
        final float largeTableHeight = configuration.getSquareCellSize() * (getNumberOfRows() + 1);
        PdfPCell cell1 = getEmptyCell();
        cell1.setFixedHeight(largeTableHeight);
        cell1.addElement(getNameTable());
        largeTable.addCell(cell1);
        PdfPCell cellSpacer = getEmptyCell();
        cellSpacer.setFixedHeight(largeTableHeight);
        largeTable.addCell(cellSpacer);
        PdfPCell cell2 = getEmptyCell();
        cell2.setFixedHeight(largeTableHeight);
        cell2.addElement(getScoreTable());
        largeTable.addCell(cell2);
        document.add(largeTable);
    }

    protected PdfPCell getCellWithBorder(PdfPCell prototype, int border, int rowspan, int colspan) {
        PdfPCell cell = new PdfPCell(prototype);
        cell.setBorder(border);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        return cell;
    }

    protected PdfPCell getEmptyCell() {
        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(Rectangle.NO_BORDER);
        emptyCell.setFixedHeight(configuration.getSquareCellSize());
        return emptyCell;
    }

    protected PdfPCell getNameCell(String text) {
        PdfPCell nameCell = text == null ? new PdfPCell() : new PdfPCell(new Phrase(text));
        nameCell.setBorder(Rectangle.BOTTOM);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nameCell.setFixedHeight(configuration.getSquareCellSize());
        return nameCell;
    }

    protected PdfPTable getNameTable() {
        //table with names
        PdfPTable nameTable = new PdfPTable(1);
        nameTable.setWidthPercentage(100.0F);
        nameTable.addCell(getEmptyCell());
        nameTable.addCell(getEmptyCell());
        for (int i = 1; i <= getNumberOfPlayers(); i++) {
            nameTable.addCell(getNameCell(getPlayerName(i)));
        }
        return nameTable;
    }

    protected abstract PdfPTable getScoreTable() throws DocumentException;

    protected abstract int getNumberOfPlayers();

    protected abstract String getPlayerName(int index);

    protected int getNumberOfRows() {
        return getNumberOfPlayers() + 2;
    }

    @Override
    protected BaseColor getSolidCellColor() {
        return PufTeamPoolSheet.PUF_POOL_SHEET_GRAY;
    }

}
