/* PufSingleTeamPoolSheet.java
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

import be.rheynaerde.poolsheets.configuration.PufSingleTeamPoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.DefaultPufSingleTeamPoolSheetConfiguration;

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
public class PufSingleTeamPoolSheet extends AbstractPufPoolSheet {

    private PufSingleTeamPoolSheetConfiguration configuration;

    public PufSingleTeamPoolSheet(int nrOfPlayers) throws DocumentException {
        this(nrOfPlayers, 20f, null, Locale.getDefault(), null, null);
    }

    public PufSingleTeamPoolSheet(int nrOfPlayers, Image image) throws DocumentException {
        this(nrOfPlayers, 20f, image, Locale.getDefault(), null, null);
    }

    public PufSingleTeamPoolSheet(int nrOfPlayers, float squareCellSize, Image image) throws DocumentException {
        this(nrOfPlayers, squareCellSize, image, Locale.getDefault(), null, null);
    }

    public PufSingleTeamPoolSheet(int nrOfPlayers, float squareCellSize, Image image, Locale locale, String title, String subtitle) throws DocumentException {
        this(new DefaultPufSingleTeamPoolSheetConfiguration(nrOfPlayers, squareCellSize, image, locale, title, subtitle));
    }

    public PufSingleTeamPoolSheet(PufSingleTeamPoolSheetConfiguration configuration) throws DocumentException {
        super(configuration);
        this.configuration = configuration;
        buildSheet();
    }

    @Override
    protected PdfPTable getScoreTable() throws DocumentException {
        final int columnCount = configuration.getNrOfPlayers() + 2;
        
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
        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.RIGHT, 1, configuration.getNrOfPlayers()));

        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.BOTTOM, 1, 1));
        {
            table.addCell(getSolidCell());
            for (int i = 0; i < configuration.getNrOfPlayers(); i++) {
                table.addCell(getHeaderCell(Integer.toString(i + 1)));
            }
        }

        table.addCell(getCellWithBorder(table.getDefaultCell(), Rectangle.BOTTOM, configuration.getNrOfPlayers(), 1));
        for (int i = 0; i < configuration.getNrOfPlayers(); i++) {
            table.addCell(getHeaderCell(Integer.toString(i + 1)));
            for (int j = 0; j < configuration.getNrOfPlayers(); j++) {
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
    protected int getNumberOfPlayers() {
        return configuration.getNrOfPlayers();
    }

    @Override
    protected String getPlayerName(int index) {
        return configuration.getNamePlayer(index);
    }
}
