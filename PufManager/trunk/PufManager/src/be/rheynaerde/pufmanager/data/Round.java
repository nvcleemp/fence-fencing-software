/* Round.java
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

package be.rheynaerde.pufmanager.data;

import be.rheynaerde.pufmanager.util.ImmutableDelegateListModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;

/**
 *
 * @author nvcleemp
 */
public class Round {

    private List<Match> matches;
    private List<Team> restingTeams;
    private int roundNumber;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
        matches = new ArrayList<Match>();
        restingTeams = new ArrayList<Team>();
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void addRestingTeam(Team team) {
        restingTeams.add(team);
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Team> getRestingTeams() {
        return restingTeams;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public ListModel getMatchesModel(){
        return new ImmutableDelegateListModel(matches);
    }

    public ListModel getRestingTeamsModel(){
        return new ImmutableDelegateListModel(restingTeams);
    }
}
