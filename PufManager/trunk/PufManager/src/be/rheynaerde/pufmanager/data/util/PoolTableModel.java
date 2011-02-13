/* PoolTableModel.java
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
import be.rheynaerde.pufmanager.data.DefaultPool;
import be.rheynaerde.pufmanager.data.Pool;
import be.rheynaerde.pufmanager.data.PoolResult;
import be.rheynaerde.pufmanager.data.listener.PoolAdapter;
import be.rheynaerde.pufmanager.data.listener.PoolListener;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nvcleemp
 */
public class PoolTableModel extends AbstractTableModel {

    private Pool pool;
    private List<SummaryValue> summaries;

    private final PoolListener listener = new PoolAdapter() {

        @Override
        public void resultUpdated(Fencer fencer, Fencer opponent) {
            //we need to fire for the complete row since the values in the
            //summary columns might have changed.
            int row = pool.getPositionOf(fencer);
            fireTableRowsUpdated(row, row);
        }

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

    public PoolTableModel(DefaultPool pool) {
        this.pool = pool;
        pool.addPoolListener(listener);
        summaries = new ArrayList<SummaryValue>();
        summaries.add(VICTORIES);
        summaries.add(LOSSES);
        summaries.add(POINTS);
        summaries.add(COUNTERPOINTS);
        summaries.add(TOTAL_POINTS);
    }

    public int getRowCount() {
        return pool.getPoolSize();
    }

    public int getColumnCount() {
        return pool.getPoolSize() + summaries.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex<pool.getPoolSize()){
            return pool.getResult(pool.getFencerAt(rowIndex), pool.getFencerAt(columnIndex));
        } else {
            return summaries.get(columnIndex-pool.getPoolSize()).getValue(rowIndex, pool);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return rowIndex != columnIndex && columnIndex < pool.getPoolSize();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(isCellEditable(rowIndex, columnIndex) && aValue instanceof PoolResult){
            pool.setResult(pool.getFencerAt(rowIndex), pool.getFencerAt(columnIndex), (PoolResult)aValue);
        }
    }

    @Override
    public String getColumnName(int column) {
        if(column<pool.getPoolSize()){
            return Integer.toString(column + 1);
        } else {
            return summaries.get(column-pool.getPoolSize()).getName();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex<pool.getPoolSize()){
            return PoolResult.class;
        } else {
            return summaries.get(columnIndex-pool.getPoolSize()).getValueClass();
        }
    }



    public static interface SummaryValue{
        public String getName();
        public Object getValue(int rowIndex, Pool pool);
        public Class getValueClass();
    }

    public static final SummaryValue VICTORIES = new SummaryValue() {

        public String getName() {
            return "V";
        }

        public Object getValue(int rowIndex, Pool pool) {
            int victories = 0;
            for (int i = 0; i < pool.getPoolSize(); i++) {
                PoolResult result = pool.getResult(pool.getFencerAt(rowIndex), pool.getFencerAt(i));
                if(result!=null && result.isVictory())
                    victories++;
            }
            return Integer.toString(victories);
        }

        public Class getValueClass() {
            return String.class;
        }
    };

    public static final SummaryValue LOSSES = new SummaryValue() {

        public String getName() {
            return "D";
        }

        public Object getValue(int rowIndex, Pool pool) {
            int losses = 0;
            for (int i = 0; i < pool.getPoolSize(); i++) {
                PoolResult result = pool.getResult(pool.getFencerAt(rowIndex), pool.getFencerAt(i));
                if(result!=null && !result.isVictory())
                    losses++;
            }
            return Integer.toString(losses);
        }

        public Class getValueClass() {
            return String.class;
        }
    };

    public static final SummaryValue POINTS = new SummaryValue() {

        public String getName() {
            return "TD";
        }

        public Object getValue(int rowIndex, Pool pool) {
            int points = 0;
            for (int i = 0; i < pool.getPoolSize(); i++) {
                PoolResult result = pool.getResult(pool.getFencerAt(rowIndex), pool.getFencerAt(i));
                if(result!=null)
                    points+=result.getScore();
            }
            return Integer.toString(points);
        }

        public Class getValueClass() {
            return String.class;
        }
    };

    public static final SummaryValue COUNTERPOINTS = new SummaryValue() {

        public String getName() {
            return "TR";
        }

        public Object getValue(int rowIndex, Pool pool) {
            int points = 0;
            for (int i = 0; i < pool.getPoolSize(); i++) {
                PoolResult result = pool.getResult(pool.getFencerAt(i), pool.getFencerAt(rowIndex));
                if(result!=null)
                    points+=result.getScore();
            }
            return Integer.toString(points);
        }

        public Class getValueClass() {
            return String.class;
        }
    };

    public static final SummaryValue TOTAL_POINTS = new SummaryValue() {

        public String getName() {
            return "TT";
        }

        public Object getValue(int rowIndex, Pool pool) {
            int points = 0;
            for (int i = 0; i < pool.getPoolSize(); i++) {
                PoolResult result = pool.getResult(pool.getFencerAt(rowIndex), pool.getFencerAt(i));
                if(result!=null)
                    points+=result.getScore();
                result = pool.getResult(pool.getFencerAt(i), pool.getFencerAt(rowIndex));
                if(result!=null)
                    points-=result.getScore();
            }
            return Integer.toString(points);
        }

        public Class getValueClass() {
            return String.class;
        }
    };

}
