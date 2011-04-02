/* DefaultPufTeamPoolSheetConfiguration.java
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
import be.rheynaerde.poolsheets.configuration.PufTeamPoolSheetConfiguration;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public class DefaultPufTeamPoolSheetConfiguration extends DefaultAbstractPoolSheetConfiguration implements PufTeamPoolSheetConfiguration{

    private final int[] teamSizes = new int[2];

    private String[][] playerNames = new String[2][];

    public DefaultPufTeamPoolSheetConfiguration(int nrOfPlayersTeamA, int nrOfPlayersTeamB, float squareCellSize, Image image, Locale locale, String title, String subtitle){
        super(squareCellSize, image, locale, title, subtitle);
        teamSizes[0] = nrOfPlayersTeamA;
        teamSizes[1] = nrOfPlayersTeamB;
        playerNames[0] = new String[nrOfPlayersTeamA];
        playerNames[1] = new String[nrOfPlayersTeamB];
    }

    public DefaultPufTeamPoolSheetConfiguration(int nrOfPlayersTeamA, int nrOfPlayersTeamB, float squareCellSize, Image image, Locale locale){
        super(squareCellSize, image, locale);
        teamSizes[0] = nrOfPlayersTeamA;
        teamSizes[1] = nrOfPlayersTeamB;
        playerNames[0] = new String[nrOfPlayersTeamA];
        playerNames[1] = new String[nrOfPlayersTeamB];
    }

    public int getNrOfPlayers(int team) {
        return teamSizes[team-1];
    }

    public String getNamePlayer(int team, int i) {
        return playerNames[team-1][i-1];
    }

    @Override
    public BoutOrder getBoutOrder() {
        return PufBoutOrder.getTeamBoutOrder(teamSizes[0], teamSizes[1]);
    }

    @Override
    public int getBoutOrderColumns() {
        if(teamSizes[0]+teamSizes[1]<5)
            return 2;
        else if(teamSizes[0]+teamSizes[1]<8)
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

    public String getResult(int playerTeam, int player, int opponentTeam, int opponent) {
        return null;
    }

    @Override
    public String getResult(int player, int opponent) {
        if((player<teamSizes[0])==(opponent<teamSizes[0])){
            //players are in the same team
            throw new IllegalArgumentException("These two players are in the same team");
        } else if(player<teamSizes[0]){
            return getResult(0, player, 1, opponent - teamSizes[0]);
        } else {
            return getResult(1, player - teamSizes[0], 0, opponent);
        }
    }

}
