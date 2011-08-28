/* SummaryValuesFactory.java
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

import be.rheynaerde.pufmanager.data.Pool;
import be.rheynaerde.pufmanager.data.PoolResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class that offers some summary values and some default lists
 * containing these values.
 * 
 * @author nvcleemp
 */
public class SummaryValuesFactory {

    private SummaryValuesFactory() {
        //do not instantiate
    }
    
    public static List<SummaryValue> getDefaultList(){
        List<SummaryValue> summaries = new ArrayList<SummaryValue>();
        summaries.add(VICTORIES);
        summaries.add(LOSSES);
        summaries.add(POINTS);
        summaries.add(COUNTERPOINTS);
        summaries.add(TOTAL_POINTS);
        summaries.add(POSITION);
        return summaries;
    }
    
    public static List<SummaryValue> getCompactList(){
        List<SummaryValue> summaries = new ArrayList<SummaryValue>();
        summaries.add(VICTORIES);
        summaries.add(POINTS);
        summaries.add(COUNTERPOINTS);
        summaries.add(TOTAL_POINTS);
        summaries.add(POSITION);
        return summaries;
    }

    public static final SummaryValue VICTORIES = new SummaryValue() {

        public String getName() {
            return "V";
        }

        public Object getValue(int rowIndex, Pool pool) {
            int victories = 0;
            for (int i = 0; i < pool.getPoolSize(); i++) {
                PoolResult result = pool.getResult(pool.getFencerAt(rowIndex), pool.getFencerAt(i));
                if (result != null && result.isVictory()) {
                    victories++;
                }
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
                if (result != null && !result.isVictory()) {
                    losses++;
                }
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
                if (result != null) {
                    points += result.getScore();
                }
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
                if (result != null) {
                    points += result.getScore();
                }
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
                if (result != null) {
                    points += result.getScore();
                }
                result = pool.getResult(pool.getFencerAt(i), pool.getFencerAt(rowIndex));
                if (result != null) {
                    points -= result.getScore();
                }
            }
            return Integer.toString(points);
        }

        public Class getValueClass() {
            return String.class;
        }
    };
    public static final SummaryValue POSITION = new SummaryValue() {

        public String getName() {
            return "P";
        }

        public Object getValue(int rowIndex, Pool pool) {
            //TODO: improve this to allow for ex aequo's
            return Integer.toString(pool.getPositions().indexOf(pool.getFencerAt(rowIndex)) + 1);
        }

        public Class getValueClass() {
            return String.class;
        }
    };
}
