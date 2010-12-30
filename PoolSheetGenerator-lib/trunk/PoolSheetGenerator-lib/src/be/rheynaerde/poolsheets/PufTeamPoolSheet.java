/* PufTeamPoolSheet.java
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

import be.rheynaerde.poolsheets.configuration.PufTeamPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.DefaultPufTeamPoolSheetConfiguration;
import com.itextpdf.text.BaseColor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public class PufTeamPoolSheet extends AbstractPoolSheet{

    protected static final BaseColor PUF_TEAM_POOL_SHEET_GRAY = new BaseColor(224, 224, 224);

    private PufTeamPoolSheetConfiguration configuration;

    public PufTeamPoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB) throws DocumentException{
        this(nrOfPlayersTeamA, nrOfPlayersTeamB, 20f, null, Locale.getDefault(), null, null);
    }

    public PufTeamPoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB, Image image) throws DocumentException{
        this(nrOfPlayersTeamA, nrOfPlayersTeamB, 20f, image, Locale.getDefault(), null, null);
    }

    public PufTeamPoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB, float squareCellSize, Image image) throws DocumentException{
        this(nrOfPlayersTeamA, nrOfPlayersTeamB, squareCellSize, image, Locale.getDefault(), null, null);
    }

    public PufTeamPoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB, float squareCellSize, Image image, Locale locale, String title, String subtitle) throws DocumentException{
        this(new DefaultPufTeamPoolSheetConfiguration(nrOfPlayersTeamA, nrOfPlayersTeamB, squareCellSize, image, locale, title, subtitle));
    }

    public PufTeamPoolSheet(PufTeamPoolSheetConfiguration configuration) throws DocumentException{
        super(configuration);
        this.configuration = configuration;
        buildSheet();
    }
    
    private PdfPCell getNameCell(String text){
        PdfPCell nameCell = text==null ? new PdfPCell() : new PdfPCell(new Phrase(text));
        nameCell.setBorder(Rectangle.BOTTOM);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nameCell.setFixedHeight(configuration.getSquareCellSize());
        return nameCell;
    }

    private PdfPCell getEmptyCell(){
        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(Rectangle.NO_BORDER);
        emptyCell.setFixedHeight(configuration.getSquareCellSize());
        return emptyCell;
    }

    @Override
    protected void buildTable(Document document) throws DocumentException {
        //table with names
        PdfPTable nameTable = new PdfPTable(1);
        nameTable.setWidthPercentage(100f);
        nameTable.addCell(getEmptyCell());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < configuration.getNrOfPlayers(i+1); j++) {
                nameTable.addCell(getNameCell(configuration.getNamePlayer(i+1, j+1)));
            }
        }

        //table for scores
        PdfPTable table = new PdfPTable(configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2) + 1);
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        table.setTotalWidth((configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2) + 1)*configuration.getSquareCellSize());
        table.setLockedWidth(true);
        float[] widths = new float[configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2) + 1];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = 1f;
        }
        table.setWidths(widths);

        {
            table.addCell(getSolidCell());
            for (int i = 0; i < configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2); i++) {
                table.addCell(getHeaderCell(Integer.toString(i+1)));
            }
        }

        for (int i = 0; i < configuration.getNrOfPlayers(1); i++) {
            table.addCell(getHeaderCell(Integer.toString(i+1)));
            if(i==0){
                PdfPCell blackCell = getSolidCell();
                blackCell.setColspan(configuration.getNrOfPlayers(1));
                blackCell.setRowspan(configuration.getNrOfPlayers(1));
                table.addCell(blackCell);
            }
            for(int j = 0; j < configuration.getNrOfPlayers(2); j++){
                table.addCell("");
            }
        }

        for (int i = 0; i < configuration.getNrOfPlayers(2); i++) {
            table.addCell(getHeaderCell(Integer.toString(configuration.getNrOfPlayers(1)+ i+1)));
            for(int j = 0; j < configuration.getNrOfPlayers(1); j++){
                table.addCell("");
            }
            if(i==0){
                PdfPCell blackCell = getSolidCell();
                blackCell.setColspan(configuration.getNrOfPlayers(2));
                blackCell.setRowspan(configuration.getNrOfPlayers(2));
                table.addCell(blackCell);
            }
        }
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        //large table to take care of the layout
        PdfPTable largeTable = new PdfPTable(3);
        largeTable.setWidths(new int[]{9,1,10});

        PdfPCell cell1 = getEmptyCell();
        cell1.setFixedHeight(configuration.getSquareCellSize()*(configuration.getNrOfPlayers(1)+configuration.getNrOfPlayers(2)+2));
        cell1.addElement(nameTable);
        largeTable.addCell(cell1);

        PdfPCell cellSpacer = getEmptyCell();
        cellSpacer.setFixedHeight(configuration.getSquareCellSize()*(configuration.getNrOfPlayers(1)+configuration.getNrOfPlayers(2)+2));
        largeTable.addCell(cellSpacer);

        PdfPCell cell2 = getEmptyCell();
        cell2.setFixedHeight(configuration.getSquareCellSize()*(configuration.getNrOfPlayers(1)+configuration.getNrOfPlayers(2)+2));
        cell2.addElement(table);
        largeTable.addCell(cell2);
        document.add(largeTable);
    }

    @Override
    protected BaseColor getSolidCellColor() {
        return PUF_TEAM_POOL_SHEET_GRAY;
    }


}
