/* DefaultRoundGenerator.java
 * =========================================================================
 * This file is part of the Fence project
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

package be.rheynaerde.pufmanager;

import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Match;
import be.rheynaerde.pufmanager.data.Round;
import be.rheynaerde.pufmanager.data.Team;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class DefaultRoundGenerator {

    public List<Round> getRounds(List<Team> teams){
        List<Round> rounds = new ArrayList<Round>();

        if(teams.size()==0) return rounds;
        
        int pisteCount = teams.size() % 2 == 0 ? teams.size()/2 : teams.size()/2+1;
        Team[][] roundHelper = new Team[pisteCount][2];

        //fill the round helper
        for (int i = 0; i < pisteCount; i++) {
            roundHelper[i][0] = teams.get(i);
        }
        for (int i = 0; i < teams.size() - pisteCount; i++) {
            roundHelper[i][1] = teams.get(i+pisteCount);
        }

        //create the rounds
        rounds.add(getRound(roundHelper, 0, true));
        for (int i = 1; i < pisteCount*2-1; i++) {
            //next round
            Team tempStorage = roundHelper[0][1];
            for (int j = 0; j < pisteCount-1; j++) {
                roundHelper[j][1] = roundHelper[j+1][1];
            }
            roundHelper[pisteCount-1][1] = roundHelper[pisteCount-1][0];
            for (int j = pisteCount-1; j > 1; j--) {
                roundHelper[j][0] = roundHelper[j-1][0];
            }
            roundHelper[1][0] = tempStorage;
            
            //create round
            rounds.add(getRound(roundHelper, i));
        }

        return rounds;
    }

    protected final Round getRound(Team[][] roundHelper, int roundNumber){
        return getRound(roundHelper, roundNumber, false);
    }

    protected final Round getRound(Team[][] roundHelper, int roundNumber, boolean includeInternalBouts){
        Round round = new Round(roundNumber);
        for (int i = 0; i < roundHelper.length; i++) {
            if(roundHelper[i][0]==null)
                round.addRestingTeam(roundHelper[i][1]);
            else if(roundHelper[i][1]==null)
                round.addRestingTeam(roundHelper[i][0]);
            else if(roundNumber%2==0)
                round.addMatch(new Match(roundHelper[i][0], roundHelper[i][1], includeInternalBouts));
            else
                round.addMatch(new Match(roundHelper[i][1], roundHelper[i][0], includeInternalBouts));
        }
        return round;
    }
}
