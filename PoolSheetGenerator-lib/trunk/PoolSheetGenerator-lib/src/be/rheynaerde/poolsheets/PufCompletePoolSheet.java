/* PufCompletePoolSheet.java
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

import be.rheynaerde.poolsheets.configuration.PufCompletePoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.DefaultPufCompletePoolSheetConfiguration;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public class PufCompletePoolSheet extends AbstractPufPoolSheet {

    private PufCompletePoolSheetConfiguration configuration;

    public PufCompletePoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB) throws DocumentException {
        this(nrOfPlayersTeamA, nrOfPlayersTeamB, 20f, null, Locale.getDefault(), null, null);
    }

    public PufCompletePoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB, Image image) throws DocumentException {
        this(nrOfPlayersTeamA, nrOfPlayersTeamB, 20f, image, Locale.getDefault(), null, null);
    }

    public PufCompletePoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB, float squareCellSize, Image image) throws DocumentException {
        this(nrOfPlayersTeamA, nrOfPlayersTeamB, squareCellSize, image, Locale.getDefault(), null, null);
    }

    public PufCompletePoolSheet(int nrOfPlayersTeamA, int nrOfPlayersTeamB, float squareCellSize, Image image, Locale locale, String title, String subtitle) throws DocumentException {
        this(new DefaultPufCompletePoolSheetConfiguration(nrOfPlayersTeamA, nrOfPlayersTeamB, squareCellSize, image, locale, title, subtitle));
    }

    public PufCompletePoolSheet(PufCompletePoolSheetConfiguration configuration) throws DocumentException {
        super(configuration);
        this.configuration = configuration;
        buildSheet();
    }

    @Override
    protected PdfPTable getScoreTable() throws DocumentException {
        final int columnCount = configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2) + 2;
        //one column for each player, an extra column for the numbers and a column for the team marks

        PdfPTable table = new PdfPTable(columnCount);
        table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
        table.setTotalWidth((columnCount) * configuration.getSquareCellSize());
        table.setLockedWidth(true);
        float[] widths = new float[columnCount];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = 1f;
        }
        table.setWidths(widths);

        PdfPCell topCell = getCellWithBorder(table.getDefaultCell(), Rectangle.RIGHT, 1, 2);
        topCell.setFixedHeight(configuration.getSquareCellSize());
        table.addCell(topCell);
        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.RIGHT, 1, configuration.getNrOfPlayers(1)));
        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.RIGHT, 1, configuration.getNrOfPlayers(2)));

        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.BOTTOM, 1, 1));
        {
            table.addCell(getSolidCell());
            for (int i = 0; i < configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2); i++) {
                table.addCell(getHeaderCell(Integer.toString(i + 1)));
            }
        }

        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.BOTTOM, configuration.getNrOfPlayers(1), 1));
        for (int i = 0; i < configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2); i++) {
            if(i==configuration.getNrOfPlayers(1))
                table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.BOTTOM, configuration.getNrOfPlayers(2), 1));

            table.addCell(getHeaderCell(Integer.toString(i + 1)));
            for (int j = 0; j < configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2); j++) {
                if (i == j) {
                    PdfPCell blackCell = getSolidCell();
                    table.addCell(blackCell);
                } else {
                    table.addCell("");
                }
            }
        }

        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return table;
    }
    
    @Override
    protected int getNumberOfPlayers(){
        return configuration.getNrOfPlayers(1) + configuration.getNrOfPlayers(2);
    }

    @Override
    protected String getPlayerName(int index){
        if(index <= configuration.getNrOfPlayers(1))
            return configuration.getNamePlayer(1, index);
        else
            return configuration.getNamePlayer(2, index - configuration.getNrOfPlayers(1));
    }

}
