/* DefaultPufSingleTeamPoolSheetConfiguration.java
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

import be.rheynaerde.poolsheets.boutorder.BoutOrder;
import be.rheynaerde.poolsheets.boutorder.PufBoutOrder;
import be.rheynaerde.poolsheets.boutorder.StandardBoutOrder;
import be.rheynaerde.poolsheets.configuration.PufCompletePoolSheetConfiguration;
import be.rheynaerde.poolsheets.configuration.PufSingleTeamPoolSheetConfiguration;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public class DefaultPufSingleTeamPoolSheetConfiguration extends DefaultAbstractPoolSheetConfiguration implements PufSingleTeamPoolSheetConfiguration{

    private int nrOfPlayers;

    private String[] playerNames;

    public DefaultPufSingleTeamPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image, Locale locale, String title, String subtitle){
        super(squareCellSize, image, locale, title, subtitle);
        this.nrOfPlayers = nrOfPlayers;
        playerNames = new String[nrOfPlayers];
    }

    public DefaultPufSingleTeamPoolSheetConfiguration(int nrOfPlayers, float squareCellSize, Image image, Locale locale){
        super(squareCellSize, image, locale);
        this.nrOfPlayers = nrOfPlayers;
        playerNames = new String[nrOfPlayers];
    }

    public int getNrOfPlayers() {
        return nrOfPlayers;
    }

    public String getNamePlayer(int i) {
        return playerNames[i-1];
    }

    @Override
    public BoutOrder getBoutOrder() {
        return StandardBoutOrder.getStandardBoutOrder(nrOfPlayers);
    }

    @Override
    public int getBoutOrderColumns() {
        if(nrOfPlayers<3)
            return 1;
        else if(nrOfPlayers<5)
            return 2;
        else if(nrOfPlayers<8)
            return 3;
        else
            return 4;
    }

    @Override
    public int getBoutOrderSpacing() {
        return 1;
    }

    @Override
    public Font getSubtitleFont() {
        return FontFactory.getFont("courier", 24f, Font.BOLD);
    }

}
