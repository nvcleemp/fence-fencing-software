/* UnassignedFencersListModel.java
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

package be.rheynaerde.pufmanager.data.util;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.listener.CompetitionAdapter;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;
import javax.swing.AbstractListModel;

/**
 *
 * @author nvcleemp
 */
public class UnassignedFencersListModel extends AbstractListModel {

    private Competition competition;
    
    private CompetitionListener competitionListener = new CompetitionAdapter() {

        @Override
        public void unassignedFencerAdded(Fencer fencer, int index) {
            fireIntervalAdded(this, index, index);
        }

        @Override
        public void unassignedFencerRemoved(Fencer fencer, int index) {
            fireIntervalRemoved(this, index, index);
        }
    };

    public UnassignedFencersListModel(Competition competition) {
        this.competition = competition;
        competition.addListener(competitionListener);
    }

    public int getSize() {
        return competition.getNumberOfUnassignedFencers();
    }

    public Object getElementAt(int index) {
        return competition.getUnassignedFencer(index);
    }
}
