/* BoutOrders.java
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
 * Each object in this enum serves as a factory for BoutOrder objects.
 *
 * @author nvcleemp
 */
public enum BoutOrders {
    BF{
        @Override
        public BoutOrder getBoutOrder(int nrOfPlayers) {
            return BfBoutOrder.getBFBoutOrder(nrOfPlayers);
        }
    },
    FIE{
        @Override
        public BoutOrder getBoutOrder(int nrOfPlayers) {
            return FieBoutOrder.getFIEBoutOrder(nrOfPlayers);
        }
    },
    STANDARD{
        @Override
        public BoutOrder getBoutOrder(int nrOfPlayers) {
            return StandardBoutOrder.getStandardBoutOrder(nrOfPlayers);
        }
    },
    NONE{
        @Override
        public BoutOrder getBoutOrder(int nrOfPlayers) {
            return EmptyBoutOrder.getInstance();
        }
    };

    /**
     * Return a BoutOrder for the given number of players.
     * @param nrOfPlayers the number of players in the pool.
     * @return A BoutOrder for the given number of players.
     */
    public abstract BoutOrder getBoutOrder(int nrOfPlayers);
}
