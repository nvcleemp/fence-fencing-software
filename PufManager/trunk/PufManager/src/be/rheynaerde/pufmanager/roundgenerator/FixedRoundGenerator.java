/* FixedRoundGenerator.java
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

package be.rheynaerde.pufmanager.roundgenerator;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.Match;
import be.rheynaerde.pufmanager.data.Round;
import be.rheynaerde.pufmanager.data.Team;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class FixedRoundGenerator implements RoundGenerator{

    private final int numberOfRounds;
    private final FixedRound[] fixedRounds;

    public FixedRoundGenerator(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
        fixedRounds = new FixedRound[numberOfRounds];
        for (int i = 0; i < fixedRounds.length; i++) {
            fixedRounds[i] = new FixedRound();
        }
    }
    
    public void addRestingTeam(int roundNumber, Team team){
        if(roundNumber>=0 && roundNumber<numberOfRounds){
            fixedRounds[roundNumber].restingTeam.add(team);
        } else {
            throw new IndexOutOfBoundsException("Round number " + roundNumber + " is not valid.");
        }
    }
    
    public void addMatch(int roundNumber, Team team1, Team team2){
        if(roundNumber>=0 && roundNumber<numberOfRounds){
            fixedRounds[roundNumber].matches.add(new FixedRoundMatch(team1, team2));
        } else {
            throw new IndexOutOfBoundsException("Round number " + roundNumber + " is not valid.");
        }
    }
    
    public void setIncludeInternalBouts(int roundNumber, boolean includeInternalBouts){
        if(roundNumber>=0 && roundNumber<numberOfRounds){
            fixedRounds[roundNumber].includeInternalBouts = includeInternalBouts;
        } else {
            throw new IndexOutOfBoundsException("Round number " + roundNumber + " is not valid.");
        }
    }
    
    public List<Round> getRounds(Competition competition) {
        List<Round> rounds = new ArrayList<Round>();
        for (int i = 0; i < fixedRounds.length; i++) {
            Round round = new Round(i, competition, fixedRounds[i].includeInternalBouts);
            for (FixedRoundMatch match : fixedRounds[i].matches) {
                round.addMatch(new Match(match.team1, match.team2, round));
            }
            for (Team team : fixedRounds[i].restingTeam) {
                round.addRestingTeam(team);
            }
            rounds.add(round);
        }
        return rounds;
    }
    
    private class FixedRound{
        private List<FixedRoundMatch> matches = new ArrayList<FixedRoundMatch>();
        private List<Team> restingTeam = new ArrayList<Team>();
        private boolean includeInternalBouts = false;
    }
    
    private class FixedRoundMatch{
        private Team team1;
        private Team team2;

        public FixedRoundMatch(Team team1, Team team2) {
            this.team1 = team1;
            this.team2 = team2;
        }
    }
}
