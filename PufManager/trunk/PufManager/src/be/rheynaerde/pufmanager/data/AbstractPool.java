/* AbstractPool.java
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

import be.rheynaerde.pufmanager.data.listener.PoolListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractPool implements Pool {
    
    protected List<Fencer> fencers = new ArrayList<Fencer>();

    protected List<PoolListener> listeners = new ArrayList<PoolListener>();

    public AbstractPool() {
    }

    public Fencer getFencerAt(int position) {
        return fencers.get(position);
    }

    public int getPoolSize() {
        return fencers.size();
    }

    public int getPositionOf(Fencer fencer) {
        return fencers.indexOf(fencer);
    }

    //==========================================================================

    public void addPoolListener(PoolListener l){
        listeners.add(l);
    }

    public void removePoolListener(PoolListener l){
        listeners.remove(l);
    }

    protected void fireAdded(Fencer fencer){
        for (PoolListener poolListener : listeners) {
            poolListener.fencerAdded(fencer);
        }
    }

    protected void fireRemoved(Fencer fencer){
        for (PoolListener poolListener : listeners) {
            poolListener.fencerRemoved(fencer);
        }
    }

    protected void fireMoved(Fencer fencer){
        for (PoolListener poolListener : listeners) {
            poolListener.fencerMoved(fencer);
        }
    }

    protected void fireResultUpdated(Fencer fencer, Fencer opponent){
        for (PoolListener poolListener : listeners) {
            poolListener.resultUpdated(fencer, opponent);
        }
    }

    protected void fireFencersChanged(){
        for (PoolListener poolListener : listeners) {
            poolListener.fencersChanged();
        }
    }
}
