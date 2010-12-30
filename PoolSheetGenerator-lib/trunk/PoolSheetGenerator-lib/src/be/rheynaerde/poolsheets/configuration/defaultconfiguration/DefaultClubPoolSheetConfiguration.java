/* DefaultClubPoolSheetConfiguration.java
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
import be.rheynaerde.poolsheets.configuration.ClubPoolSheetConfiguration;
import com.itextpdf.text.Font;

import com.itextpdf.text.Image;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author nvcleemp
 */
public class DefaultClubPoolSheetConfiguration extends DefaultAbstractPoolSheetConfiguration implements ClubPoolSheetConfiguration {

    private static final String[] SUMMARY_COLUMNS_KEYS = {"v", "td", "tr", "tt", "place"};

    private int nrOfPlayers;
    private String[] playerNames;
    private String[] summaryColumns;

    public DefaultClubPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image) {
        this(nrOfPlayers, squareCellSize, image, Locale.getDefault(), null);
    }

    public DefaultClubPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image, Locale locale) {
        this(nrOfPlayers, squareCellSize, image, locale, null);
    }

    public DefaultClubPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image, Locale locale, String title) {
        super(squareCellSize, image, locale, title, null);
        this.nrOfPlayers = nrOfPlayers;
        this.playerNames = null;
        ResourceBundle bundle =
                ResourceBundle.getBundle
                ("be.rheynaerde.poolsheets.configuration.defaultconfiguration.defaultclubpoolsheet",
                locale);
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
        return false;
    }

    @Override
    public BoutOrder getBoutOrder() {
        throw new UnsupportedOperationException("A ClubPoolSheet has no bout order section.");
    }

    @Override
    public int getBoutOrderColumns() {
        throw new UnsupportedOperationException("A ClubPoolSheet has no bout order section.");
    }

    @Override
    public int getBoutOrderSpacing() {
        throw new UnsupportedOperationException("A ClubPoolSheet has no bout order section.");
    }

    @Override
    public boolean putBoutOrderOnNewPage() {
        throw new UnsupportedOperationException("A ClubPoolSheet has no bout order section.");
    }

    @Override
    public String getSubtitle() {
        throw new UnsupportedOperationException("A ClubPoolSheet has no subtitle.");
    }

    @Override
    public Font getSubtitleFont() {
        throw new UnsupportedOperationException("A ClubPoolSheet has no subtitle.");
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

    @Override
    public boolean isLandscape(int page) {
        return true;
    }
}
