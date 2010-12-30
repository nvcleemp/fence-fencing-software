/* DefaultStandardPoolSheetConfiguration.java
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

package be.rheynaerde.poolsheets.configuration.defaultconfiguration;

import be.rheynaerde.poolsheets.configuration.StandardPoolSheetConfiguration;
import be.rheynaerde.poolsheets.boutorder.BoutOrder;
import be.rheynaerde.poolsheets.boutorder.BoutOrders;

import com.itextpdf.text.Image;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author nvcleemp
 */
public class DefaultStandardPoolSheetConfiguration extends DefaultAbstractPoolSheetConfiguration implements StandardPoolSheetConfiguration{

    private static final String[] SUMMARY_COLUMNS_KEYS = {"v", "td", "tr", "tt", "place"};

    private int nrOfPlayers;
    private String[] playerNames;
    private BoutOrders mo;
    private String[] summaryColumns;

    public DefaultStandardPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image) {
        this(nrOfPlayers, squareCellSize, image, Locale.getDefault(), null, null);
    }

    public DefaultStandardPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image, Locale locale) {
        this(nrOfPlayers, squareCellSize, image, locale, null, null);
    }

    public DefaultStandardPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image, Locale locale, String title, String subtitle) {
        super(squareCellSize, image, locale, title, subtitle);
        this.nrOfPlayers = nrOfPlayers;
        this.playerNames = null;
        ResourceBundle bundle =
                ResourceBundle.getBundle
                ("be.rheynaerde.poolsheets.configuration.defaultconfiguration.defaultstandardpoolsheet",
                locale);
        this.mo = BoutOrders.STANDARD;
        this.summaryColumns = new String[SUMMARY_COLUMNS_KEYS.length];
        for (int i = 0; i < SUMMARY_COLUMNS_KEYS.length; i++) {
            summaryColumns[i] = bundle.getString(SUMMARY_COLUMNS_KEYS[i]);
        }
    }

    public int getNrOfPlayers() {
        return nrOfPlayers;
    }

    public String getNamePlayer(int i){
        if(playerNames==null)
            return null;
        else
            return playerNames[i];
    }

    @Override
    public boolean includeOrderOfBouts() {
        return nrOfPlayers > 3 && nrOfPlayers < 10;
    }

    @Override
    public BoutOrder getBoutOrder() {
        return mo.getBoutOrder(nrOfPlayers);
    }

    @Override
    public int getBoutOrderColumns() {
        if(nrOfPlayers>11)
            return 6;
        else if(nrOfPlayers>9)
            return 5;
        else if(nrOfPlayers>7)
            return 4;
        else if(nrOfPlayers>5)
            return 3;
        else
            return 2;
    }

    public int getSummaryColumnCount(){
        return summaryColumns.length;
    }

    public String getSummaryColumnName(int column){
        return summaryColumns[column];
    }

    public String getSummaryColumnValue(int player, int column){
        return null;
    }
}
