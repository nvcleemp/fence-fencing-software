/* DefaultPool.java
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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class DefaultPool extends AbstractPool {

    private Map<Fencer, Map<Fencer, PoolResult>> results = new HashMap<Fencer, Map<Fencer, PoolResult>>();

    protected void addFencer(Fencer fencer, boolean isChanging){
        if(!results.containsKey(fencer)){
            results.put(fencer, new HashMap<Fencer, PoolResult>());
            fencers.add(fencer);
            if(!isChanging)
                fireAdded(fencer);
        }
    }

    public void addFencer(Fencer fencer){
        addFencer(fencer, false);
    }

    protected void removeFencer(Fencer fencer, boolean isChanging){
        if(fencers.contains(fencer)){
            results.remove(fencer);
            fencers.remove(fencer);
            for (Fencer f : results.keySet()) {
                results.get(f).remove(fencer);
            }
            if(!isChanging)
                fireRemoved(fencer);
        }
    }

    public void removeFencer(Fencer fencer){
        removeFencer(fencer, false);
    }

    public PoolResult getResult(Fencer fencer, Fencer opponent){
        return results.get(fencer).get(opponent);
    }

    public void setResult(Fencer fencer, Fencer opponent, PoolResult poolResult) {
        if(results.containsKey(fencer)){
            if(poolResult == null){
                results.get(fencer).remove(opponent);
            } else {
                results.get(fencer).put(opponent, poolResult);
            }
            fireResultUpdated(fencer, opponent);
        }
    }


}
