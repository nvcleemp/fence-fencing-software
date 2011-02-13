/* PoolRowHeaderTableModel.java
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

import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Pool;
import be.rheynaerde.pufmanager.data.listener.PoolAdapter;
import be.rheynaerde.pufmanager.data.listener.PoolListener;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nvcleemp
 */
public class PoolRowHeaderTableModel extends AbstractTableModel {

    private Pool pool;

    private final PoolListener listener = new PoolAdapter() {

        @Override
        public void fencerAdded(Fencer fencer) {
            //both the number of columns as the number of rows has changed
            fireTableStructureChanged();
        }

        @Override
        public void fencerRemoved(Fencer fencer) {
            //both the number of columns as the number of rows has changed
            fireTableStructureChanged();
        }

        @Override
        public void fencerMoved(Fencer fencer) {
            //structure changed since the order of the columns is changed as well
            fireTableStructureChanged();
        }

        @Override
        public void fencersChanged() {
            fireTableStructureChanged();
        }
    };

    public PoolRowHeaderTableModel(Pool pool) {
        this.pool = pool;
        pool.addPoolListener(listener);
    }

    public int getRowCount() {
        return pool.getPoolSize();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return rowIndex+1;
        } else {
            return pool.getFencerAt(rowIndex).getName();
        }
    }

}
