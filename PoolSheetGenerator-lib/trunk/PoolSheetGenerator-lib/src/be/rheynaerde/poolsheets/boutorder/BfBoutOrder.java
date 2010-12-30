/* BfBoutOrder.java
 * =========================================================================
 * This file is part of the PoolSheetGenerator project
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

package be.rheynaerde.poolsheets.boutorder;

/**
 *
 * @author nvcleemp
 */
public class BfBoutOrder {

    private BfBoutOrder() {
        //private
    }

    private static final BoutOrder POOL4 = new DefaultBoutOrder(4,
             new int[][]{{1,2},{3,4},{3,1},{4,2},{1,4},{2,3}});

    private static final BoutOrder POOL5 = new DefaultBoutOrder(5,
             new int[][]{{1,2},{3,4},{5,1},{2,3},{5,4},
                         {1,3},{2,5},{4,1},{3,5},{4,2}
                        });

    private static final BoutOrder POOL6 = new DefaultBoutOrder(6,
             new int[][]{{1,2},{4,5},{2,3},
                         {5,6},{3,1},{6,4},
                         {2,5},{1,4},{5,3},
                         {1,6},{4,2},{3,6},
                         {5,1},{3,4},{6,2}
                        });

    private static final BoutOrder POOL7 = new DefaultBoutOrder(7,
             new int[][]{{1,4},{2,5},{3,6},
                         {7,1},{5,4},{2,3},
                         {6,7},{5,1},{4,3},
                         {6,2},{5,7},{3,1},
                         {4,6},{7,2},{3,5},
                         {1,6},{2,4},{7,3},
                         {6,5},{1,2},{4,7}
                        });

    /**
     * Return the BoutOrder according to the BF for the given number of
     * players. When there isn't a BoutOrder defined for a given number
     * of players, then an {@link EmptyBoutOrder} will be returned.
     * 
     * @param nrOfPlayers the number of players in the pool.
     * @return A BoutOrder for the given number of players.
     */
    public static BoutOrder getBFBoutOrder(int nrOfPlayers){
        if(nrOfPlayers==4)
            return POOL4;
        else if(nrOfPlayers==5)
            return POOL5;
        else if(nrOfPlayers==6)
            return POOL6;
        else if(nrOfPlayers==7)
            return POOL7;
        else
            return EmptyBoutOrder.getInstance();
    }

}
