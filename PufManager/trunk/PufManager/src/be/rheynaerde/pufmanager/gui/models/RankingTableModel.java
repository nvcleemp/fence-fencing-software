/* RankingTableModel.java
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

package be.rheynaerde.pufmanager.gui.models;

import be.rheynaerde.pufmanager.data.CompetitionPool;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.listener.PoolListener;

import javax.swing.table.AbstractTableModel;

/**
 * Table model to display the ranking.
 * 
 * @author nvcleemp
 */
public class RankingTableModel extends AbstractTableModel {
    
    private final CompetitionPool competitionPool;
    
    private final PoolListener poolListener = new PoolListener() {

        public void resultUpdated(Fencer fencer, Fencer opponent) {
            fireTableRowsUpdated(0, competitionPool.getPoolSize());
        }

        public void fencerAdded(Fencer fencer) {
            fireTableDataChanged();
        }

        public void fencerRemoved(Fencer fencer) {
            fireTableDataChanged();
        }

        public void fencerMoved(Fencer fencer) {
            //does not affect the ranking.
        }

        public void fencersChanged() {
            fireTableRowsUpdated(0, competitionPool.getPoolSize());
        }

        public void maximumScoreChanged() {
            fireTableRowsUpdated(0, competitionPool.getPoolSize());
        }
    };

    public RankingTableModel(CompetitionPool competitionPool) {
        this.competitionPool = competitionPool;
        this.competitionPool.addPoolListener(poolListener);
    }

    public int getRowCount() {
        return competitionPool.getPoolSize();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return rowIndex+1;
        } else if(columnIndex==1){
            return competitionPool.getPositions().get(rowIndex);
        } else {
            throw new IllegalArgumentException("No cell at position: " 
                                                + rowIndex + "," + columnIndex);
        }
    }
    
}
