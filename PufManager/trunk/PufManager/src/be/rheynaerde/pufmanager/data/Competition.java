/* Competition.java
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

import be.rheynaerde.pufmanager.roundgenerator.DefaultRoundGenerator;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;
import be.rheynaerde.pufmanager.data.listener.TeamAdapter;
import be.rheynaerde.pufmanager.data.listener.TeamListener;
import be.rheynaerde.pufmanager.roundgenerator.RoundGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class Competition {

    private List<CompetitionListener> listeners = new ArrayList<CompetitionListener>();

    private TeamListener teamListener = new TeamAdapter() {

        @Override
        public void fencerAdded(Fencer fencer, int index) {
            fencers = null;
            fireFencersChanged();
        }

        @Override
        public void fencerRemoved(Fencer fencer, int index) {
            fencers = null;
            fireFencersChanged();
        }
    };

    private List<Round> rounds;
    private List<Team> teams;

    private List<Fencer> unassignedFencers = new ArrayList<Fencer>();

    private List<Fencer> fencers;

    private CompetitionPool competitionPool;

    private DefaultRoundGenerator drg = new DefaultRoundGenerator();

    private final CompetitionSettings settings;

    public Competition() {
        this(new CompetitionSettings());
    }

    public Competition(CompetitionSettings settings) {
        this(new DefaultRoundGenerator(), new ArrayList<Team>(), null, settings);
    }

    public Competition(List<Team> teams) {
        this(teams, new CompetitionSettings());
    }

    public Competition(List<Team> teams, CompetitionSettings settings) {
        this(new DefaultRoundGenerator(), teams, null, settings);
    }

    private Competition(RoundGenerator roundGenerator, List<Team> teams, List<Fencer> fencers, CompetitionSettings settings) {
        this.teams = new ArrayList<Team>(teams);
        for (Team team : teams) {
            team.addListener(teamListener);
        }
        this.fencers = fencers;
        this.settings = settings;
        this.rounds = roundGenerator.getRounds(this);
        this.competitionPool = new CompetitionPool(this);
    }

    public void addTeam(Team team){
        if(team!=null && !teams.contains(team)){
            teams.add(team);
            team.addListener(teamListener);
            rounds = drg.getRounds(this);
            fireTeamAdded(team);
            fireRoundsChanged();
        }
    }

    public void removeTeam(Team team){
        if(team!=null && teams.contains(team)){
            team.removeListener(teamListener);
            for (int i = 0; i < team.getTeamSize(); i++) {
                addUnassignedFencer(team.getFencer(i));
            }
            int index = teams.indexOf(team);
            teams.remove(team);
            rounds = drg.getRounds(this);
            fireTeamRemoved(team, index);
            fireRoundsChanged();
        }
    }

    public int getTeamCount(){
        return teams.size();
    }

    public Team getTeam(int index){
        return teams.get(index);
    }

    public int getRoundCount(){
        return rounds.size();
    }

    public Round getRound(int index){
        return rounds.get(index);
    }

    public int getNumberOfUnassignedFencers(){
        return unassignedFencers.size();
    }

    public Fencer getUnassignedFencer(int index){
        return unassignedFencers.get(index);
    }

    public List<Fencer> getFencers(){
        if(fencers==null){
            fencers = new ArrayList<Fencer>();
            for (Team team : teams) {
                for (int i = 0; i < team.getTeamSize(); i++) {
                    fencers.add(team.getFencer(i));
                }
            }
            fencers.addAll(unassignedFencers);
        }
        return new ArrayList<Fencer>(fencers);
    }

    public void addUnassignedFencer(Fencer fencer){
        if(!unassignedFencers.contains(fencer)){
            unassignedFencers.add(fencer);
            fencers = null;
            fireUnassignedFencerAdded(fencer);
            fireFencersChanged();
        }
    }

    public void removeUnassignedFencer(Fencer fencer){
        if(unassignedFencers.contains(fencer)){
            int index = unassignedFencers.indexOf(fencer);
            unassignedFencers.remove(fencer);
            fencers = null;
            fireUnassignedFencerRemoved(fencer, index);
            fireFencersChanged();
        }
    }

    public CompetitionPool getCompetitionPool() {
        return competitionPool;
    }

    public CompetitionSettings getSettings() {
        return settings;
    }

    protected void fireRoundsChanged(){
        for (CompetitionListener l : listeners) {
            l.roundsChanged();
        }
    }

    protected void fireTeamAdded(Team team){
        int index = teams.indexOf(team);
        for (CompetitionListener l : listeners) {
            l.teamAdded(team, index);
        }
    }

    protected void fireTeamRemoved(Team team, int index){
        for (CompetitionListener l : listeners) {
            l.teamRemoved(team, index);
        }
    }

    protected void fireUnassignedFencerAdded(Fencer fencer){
        int index = unassignedFencers.indexOf(fencer);
        for (CompetitionListener l : listeners) {
            l.unassignedFencerAdded(fencer, index);
        }
    }

    protected void fireUnassignedFencerRemoved(Fencer fencer, int index){
        for (CompetitionListener l : listeners) {
            l.unassignedFencerRemoved(fencer, index);
        }
    }

    protected void fireFencersChanged(){
        for (CompetitionListener l : listeners) {
            l.fencersChanged();
        }
    }

    public void addListener(CompetitionListener listener){
        listeners.add(listener);
    }

    public void removeListener(CompetitionListener listener){
        listeners.remove(listener);
    }
    
    public static Competition constructCompetition(RoundGenerator roundGenerator, List<Fencer> unassignedFencers, List<Team> teams, CompetitionSettings settings){
        return new Competition(roundGenerator, teams, unassignedFencers, settings);
    }
}
