/* PufBoutOrder.java
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
public class PufBoutOrder {

    private PufBoutOrder() {
        //private
    }

    //nothing better is possible when two teams of 2 meet
    private static final BoutOrder TEAM_POOL2_2 = new TeamBoutOrder(2,2,
             new int[][]{{1,3},{2,4},
                         {2,3},{1,4}
                        });

    private static final BoutOrder TEAM_POOL2_3 = new TeamBoutOrder(2,3,
             new int[][]{{1,3},{2,4},
                         {1,5},{3,2},
                         {4,1},{5,2}
                        });

    private static final BoutOrder TEAM_POOL2_4 = new TeamBoutOrder(2,4,
             new int[][]{{1,3},{2,4},
                         {5,1},{6,2},
                         {4,1},{3,2},
                         {1,6},{2,5}
                        });

    private static final BoutOrder TEAM_POOL3_2 = new TeamBoutOrder(3,2,
             new int[][]{{1,4},{2,5},{3,4},
                         {5,1},{4,2},{5,3}
                        });

    private static final BoutOrder TEAM_POOL3_3 = new TeamBoutOrder(3,3,
             new int[][]{{3,6},{5,1},{2,4},
                         {6,1},{3,4},{5,2},
                         {1,4},{6,2},{3,5}
                        });

    private static final BoutOrder TEAM_POOL3_4 = new TeamBoutOrder(3,4,
             new int[][]{{1,4},{2,5},{3,6},
                         {1,7},{4,2},{5,3},
                         {6,1},{7,2},{4,3},
                         {5,1},{6,2},{7,3}
                        });

    private static final BoutOrder TEAM_POOL4_2 = new TeamBoutOrder(4,2,
             new int[][]{{1,5},{6,2},{5,3},{4,6},
                         {2,5},{6,1},{5,4},{3,6}
                        });

    private static final BoutOrder TEAM_POOL4_3 = new TeamBoutOrder(4,3,
             new int[][]{{1,5},{2,6},{3,7},{4,5},
                         {6,1},{7,2},{5,3},{6,4},
                         {7,1},{5,2},{6,3},{7,4}
                        });

    private static final BoutOrder TEAM_POOL4_4 = new TeamBoutOrder(4,4,
             new int[][]{{3,8},{4,6},{1,7},{2,5},
                         {6,3},{8,1},{5,4},{7,2},
                         {1,6},{3,5},{2,8},{4,7},
                         {5,1},{6,2},{7,3},{8,4}
                        });

    private static final BoutOrder[][] TEAM_BOUT_ORDERS =
            {
                {TEAM_POOL2_2, TEAM_POOL2_3, TEAM_POOL2_4},
                {TEAM_POOL3_2, TEAM_POOL3_3, TEAM_POOL3_4},
                {TEAM_POOL4_2, TEAM_POOL4_3, TEAM_POOL4_4}
            };

    private static final int OFFSET = 2;

    public static BoutOrder getTeamBoutOrder(int nrOfPlayersTeamA, int nrOfPlayersTeamB){
        if(nrOfPlayersTeamA-OFFSET < 0 ||
                nrOfPlayersTeamA-OFFSET >= TEAM_BOUT_ORDERS.length ||
                nrOfPlayersTeamB-OFFSET < 0 ||
                nrOfPlayersTeamB-OFFSET >=
                            TEAM_BOUT_ORDERS[nrOfPlayersTeamA-OFFSET].length)
            return EmptyBoutOrder.getInstance();
        else
            return TEAM_BOUT_ORDERS[nrOfPlayersTeamA-OFFSET][nrOfPlayersTeamB-OFFSET];
    }
}
