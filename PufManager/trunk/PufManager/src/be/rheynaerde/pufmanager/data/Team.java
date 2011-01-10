/* Team.java
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

import be.rheynaerde.pufmanager.data.listener.TeamListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A team is a small group of fencers that will stay together throughout the
 * competition.
 * 
 * @author nvcleemp
 */
public class Team {

    private String teamName;
    private List<Fencer> fencers;

    private List<TeamListener> listeners = new ArrayList<TeamListener>();

    public Team(String teamName, List<Fencer> fencers) {
        this.teamName = teamName;
        this.fencers = new ArrayList<Fencer>(fencers);
    }

    public Team(String teamName) {
        this.teamName = teamName;
        this.fencers = new ArrayList<Fencer>();
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamSize() {
        return fencers.size();
    }

    public Fencer getFencer(int index){
        return fencers.get(index);
    }

    public String getFencerName(int index){
        return fencers.get(index).getName();
    }

    public String[] getFencerNames() {
        String[] names = new String[fencers.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = getFencerName(i);
        }
        return names;
    }

    public void addFencer(Fencer fencer){
        if(!fencers.contains(fencer)){
            fencers.add(fencer);
            fireFencerAdded(fencer);
        }
    }

    public void removeFencer(Fencer fencer){
        if(fencers.contains(fencer)){
            int index = fencers.indexOf(fencer);
            fencers.remove(fencer);
            fireFencerRemoved(fencer, index);
        }
    }

    public void addListener(TeamListener listener){
        listeners.add(listener);
    }

    public void removeListener(TeamListener listener){
        listeners.remove(listener);
    }

    protected void fireFencerAdded(Fencer fencer){
        int index = fencers.indexOf(fencer);
        for (TeamListener l : listeners) {
            l.fencerAdded(fencer, index);
        }
    }

    protected void fireFencerRemoved(Fencer fencer, int index){
        for (TeamListener l : listeners) {
            l.fencerRemoved(fencer, index);
        }
    }

    @Override
    public String toString() {
        return getTeamName();
    }

}
