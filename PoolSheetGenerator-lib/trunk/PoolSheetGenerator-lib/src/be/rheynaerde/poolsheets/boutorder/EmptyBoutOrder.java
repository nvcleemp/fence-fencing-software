/* EmptyBoutOrder.java
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
 * An implementation of BoutOrder that contains no bouts.
 * 
 * @author nvcleemp
 */
public class EmptyBoutOrder implements BoutOrder{

    private static final EmptyBoutOrder INSTANCE = new EmptyBoutOrder();

    public static EmptyBoutOrder getInstance(){
        return INSTANCE;
    }

    private EmptyBoutOrder(){
        //nothing to do
    }

    public int getNrOfPlayers() {
        return 0;
    }

    public int getNrOfBouts() {
        return 0;
    }

    public int getFirstPlayerOfBout(int bout) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSecondPlayerOfBout(int bout) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
