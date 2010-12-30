/* TeamBoutOrder.java
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

package be.rheynaerde.poolsheets.boutorder;

/**
 *
 * @author nvcleemp
 */
public class TeamBoutOrder implements BoutOrder{
    
    private int nrOfPlayersTeamA;
    private int nrOfPlayersTeamB;
    private int[][] boutOrder;

    public TeamBoutOrder(int nrOfPlayersTeamA, int nrOfPlayersTeamB, int[][] boutOrder){
        if(boutOrder.length!=nrOfPlayersTeamA*nrOfPlayersTeamB)
            throw new IllegalArgumentException("Wrong number of bouts for a default bout order");
        this.nrOfPlayersTeamA = nrOfPlayersTeamA;
        this.nrOfPlayersTeamB = nrOfPlayersTeamB;
        this.boutOrder = boutOrder;
    }

    public int getNrOfPlayers() {
        return nrOfPlayersTeamA + nrOfPlayersTeamB;
    }

    public int getNrOfPlayers(int team) {
        if(team==1)
            return nrOfPlayersTeamA;
        else if(team==2)
            return nrOfPlayersTeamB;
        else
            throw new IllegalArgumentException();
    }

    public int getNrOfBouts() {
        return boutOrder.length;
    }

    public int getFirstPlayerOfBout(int bout) {
        return boutOrder[bout][0];
    }

    public int getSecondPlayerOfBout(int bout) {
        return boutOrder[bout][1];
    }
}
