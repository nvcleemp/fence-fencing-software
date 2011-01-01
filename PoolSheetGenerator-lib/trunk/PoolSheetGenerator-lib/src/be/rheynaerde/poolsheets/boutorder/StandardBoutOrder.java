/* StandardBoutOrder.java
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
 * Default bout orders that don't follow any specific rules, but just fit well.
 * The advantage is that these are specified for larger pools. For sizes 4 up
 * to 7 they coincide with the BF order.
 *
 * @author nvcleemp
 */
public class StandardBoutOrder {

    private StandardBoutOrder() {
        //private
    }

    private static final BoutOrder POOL2 = new DefaultBoutOrder(2,
             new int[][]{{1,2}});

    private static final BoutOrder POOL3 = new DefaultBoutOrder(3,
             new int[][]{{1,2},{2,3},{1,3}});

    private static final BoutOrder POOL4 = new DefaultBoutOrder(4,
             new int[][]{{1,4},{2,3},{1,3},{2,4},{3,4},{1,2}});

    private static final BoutOrder POOL5 = new DefaultBoutOrder(5,
             new int[][]{{1,2},{3,4},{5,1},{2,3},{5,4},
                         {1,3},{2,5},{4,1},{3,5},{4,2}
                        });

    private static final BoutOrder POOL6 = new DefaultBoutOrder(6,
             new int[][]{{1,4},{2,5},{3,6},
                         {5,1},{4,2},{3,1},
                         {6,2},{5,3},{6,4},
                         {1,2},{3,4},{5,6},
                         {2,3},{1,6},{4,5}
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

    private static final BoutOrder POOL8 = new DefaultBoutOrder(8,
             new int[][]{{2,3},{1,5},{7,4},{6,8},
                         {1,2},{3,4},{5,6},{8,7},
                         {4,1},{5,2},{8,3},{6,7},
                         {4,2},{8,1},{7,5},{3,6},
                         {2,8},{5,4},{6,1},{3,7},
                         {4,8},{2,6},{3,5},{1,7},
                         {4,6},{8,5},{7,2},{1,3}
                        });

    private static final BoutOrder POOL9 = new DefaultBoutOrder(9,
             new int[][]{{1,9},{2,8},{3,7},{4,6},
                         {1,5},{2,9},{8,3},{7,4},
                         {6,5},{1,2},{9,3},{8,4},
                         {7,5},{6,1},{3,2},{9,4},
                         {5,8},{7,6},{3,1},{2,4},
                         {5,9},{8,6},{7,1},{4,3},
                         {5,2},{6,9},{8,7},{4,1},
                         {5,3},{6,2},{9,7},{1,8},
                         {4,5},{3,6},{2,7},{9,8}
                        });

    private static final BoutOrder POOL10 = new DefaultBoutOrder(10,
             new int[][]{{1,4},{6,9},{2,5},{7,10},{3,1},
                         {8,6},{4,5},{9,10},{2,3},{7,8},
                         {5,1},{10,6},{4,2},{9,7},{5,3},
                         {10,8},{1,2},{6,7},{3,4},{8,9},
                         {5,10},{1,6},{2,7},{3,8},{4,9},
                         {6,5},{10,2},{8,1},{7,4},{9,3},
                         {2,6},{5,8},{4,10},{1,9},{3,7},
                         {8,2},{6,4},{9,5},{10,3},{7,1},
                         {4,8},{2,9},{3,6},{5,7},{1,10}
                        });

    private static final BoutOrder POOL11 = new DefaultBoutOrder(11,
             new int[][]{{1,2},{7,8},{4,5},{10,11},{2,3},
                         {8,9},{5,6},{3,1},{9,7},{6,4},
                         {2,5},{8,11},{1,4},{7,10},{5,3},
                         {11,9},{1,6},{4,2},{10,8},{3,6},
                         {5,1},{11,7},{3,4},{9,10},{6,2},
                         {1,7},{3,9},{10,4},{8,2},{5,11},
                         {1,8},{9,2},{3,10},{4,11},{6,7},
                         {9,1},{2,10},{11,3},{7,5},{6,8},
                         {10,1},{11,2},{4,7},{8,5},{6,9},
                         {11,1},{7,3},{4,8},{9,5},{6,10},
                         {2,7},{8,3},{4,9},{10,5},{6,11}
                        });

    private static final BoutOrder POOL12 = new DefaultBoutOrder(12,
             new int[][]{{1,2},{7,8},{4,5},{10,11},{2,3},{8,9},
                         {5,6},{11,12},{3,1},{9,7},{6,4},{12,10},
                         {2,5},{8,11},{1,4},{7,10},{5,3},{11,9},
                         {1,6},{7,12},{4,2},{10,8},{3,6},{9,12},
                         {5,1},{11,7},{3,4},{9,10},{6,2},{12,8},
                         {1,7},{3,9},{10,4},{8,2},{5,11},{12,6},
                         {1,8},{9,2},{3,10},{4,11},{12,5},{6,7},
                         {9,1},{2,10},{11,3},{4,12},{7,5},{6,8},
                         {10,1},{11,2},{12,3},{4,7},{8,5},{6,9},
                         {11,1},{2,12},{7,3},{4,8},{9,5},{6,10},
                         {12,1},{2,7},{8,3},{4,9},{10,5},{6,11}
                        });

    /**
     * Return the default BoutOrder for the given number of players. When
     * there isn't a BoutOrder defined for a given number of players, then
     * an {@link EmptyBoutOrder} will be returned.
     * 
     * @param nrOfPlayers the number of players in the pool.
     * @return A BoutOrder for the given number of players.
     */
    public static BoutOrder getStandardBoutOrder(int nrOfPlayers){
        if(nrOfPlayers==2)
            return POOL2;
        else if(nrOfPlayers==3)
            return POOL3;
        else if(nrOfPlayers==4)
            return POOL4;
        else if(nrOfPlayers==5)
            return POOL5;
        else if(nrOfPlayers==6)
            return POOL6;
        else if(nrOfPlayers==7)
            return POOL7;
        else if(nrOfPlayers==8)
            return POOL8;
        else if(nrOfPlayers==9)
            return POOL9;
        else if(nrOfPlayers==10)
            return POOL10;
        else if(nrOfPlayers==11)
            return POOL11;
        else if(nrOfPlayers==12)
            return POOL12;
        else
            return EmptyBoutOrder.getInstance();
    }

}
