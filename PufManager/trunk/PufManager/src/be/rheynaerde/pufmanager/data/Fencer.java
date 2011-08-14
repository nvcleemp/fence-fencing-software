/* Fencer.java
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

import be.rheynaerde.pufmanager.data.listener.FencerListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class Fencer {

    private String name;
    private String club;
    private String id;
    private List<FencerListener> listeners = new ArrayList<FencerListener>();

    public Fencer(String name) {
        this(name, null);
    }

    public Fencer(String name, String club) {
        if (name == null) {
            throw new IllegalArgumentException("The name of a fencer can't be null");
        }
        this.name = name;
        this.club = club;
    }

    public Fencer(String name, String club, String id) {
        this(name, club);
        this.id = id;
    }

    public String getClub() {
        return club;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setClub(String club) {
        if (club == null ? this.club != null : !club.equals(this.club)) {
            this.club = club;
            fireClubChanged();
        }
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name of a fencer can't be null");
        }
        if (!name.equals(this.name)) {
            this.name = name;
            fireNameChanged();
        }
    }

    public void setId(String id) {
        if(id == null ? this.id != null : !id.equals(this.id)){
            this.id = id;
            fireIdChanged();
        }
    }
    
    public void setData(String name, String club){
        if (name == null) {
            throw new IllegalArgumentException("The name of a fencer can't be null");
        }
        if (name.equals(this.name)) {
            setClub(club);
        } else if(club == null ? this.club == null : club.equals(this.club)) {
            setName(name);
        } else {
            this.name = name;
            this.club = club;
            fireDataChanged();
        }
        
    }

    @Override
    public String toString() {
        return name;
    }

    public void addFencerListener(FencerListener listener) {
        listeners.add(listener);
    }

    public void removeFencerListener(FencerListener listener) {
        listeners.remove(listener);
    }

    private void fireNameChanged() {
        for (FencerListener fencerListener : listeners) {
            fencerListener.nameChanged(this);
        }
    }

    private void fireClubChanged() {
        for (FencerListener fencerListener : listeners) {
            fencerListener.clubChanged(this);
        }
    }
    
    private void fireIdChanged() {
        for (FencerListener fencerListener : listeners) {
            fencerListener.idChanged(this);
        }
    }
    
    private void fireDataChanged() {
        for (FencerListener fencerListener : listeners) {
            fencerListener.dataChanged(this);
        }
    }
}
