/* DelegatePool.java
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
public class DelegatePool extends AbstractPool {

    private Pool delegate;

    private PoolListener delegateListener = new PoolListener() {

        public void resultUpdated(Fencer fencer, Fencer opponent) {
            if(fencers.contains(fencer) && fencers.contains(opponent)){
                fireResultUpdated(fencer, opponent);
            }
        }

        public void fencerAdded(Fencer fencer) {
            //ignore
        }

        public void fencerRemoved(Fencer fencer) {
            if(fencers.contains(fencer)){
                fencers.remove(fencer);
                fireRemoved(fencer);
            }
        }

        public void fencerMoved(Fencer fencer) {
            //ignore
        }

        public void fencersChanged() {
            List<Fencer> removedFencers = new ArrayList<Fencer>();
            for (Fencer fencer : fencers) {
                if(delegate.getPositionOf(fencer)==-1){
                    removedFencers.add(fencer);
                }
            }
            if(!removedFencers.isEmpty()){
                fencers.removeAll(removedFencers);
                fireFencersChanged();
            }
        }
    };

    public DelegatePool(Pool delegate) {
        this.delegate = delegate;
        delegate.addPoolListener(delegateListener);
    }

    public DelegatePool(Pool delegate, List<Fencer> fencers) {
        super(fencers);
        this.delegate = delegate;
        delegate.addPoolListener(delegateListener);
    }

    public PoolResult getResult(Fencer fencer, Fencer opponent) {
        return delegate.getResult(fencer, opponent);
    }

    public void setResult(Fencer fencer, Fencer opponent, PoolResult poolResult) {
        if(fencers.contains(fencer) && fencers.contains(opponent)){
            delegate.setResult(fencer, opponent, poolResult);
            //firing will be handled by delegateListener
        }
    }
}
