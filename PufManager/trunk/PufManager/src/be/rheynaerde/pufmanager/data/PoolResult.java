/* PoolResult.java
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

package be.rheynaerde.pufmanager.data;

/**
 *
 * @author nvcleemp
 */
public class PoolResult {

    private boolean victory;
    private int score;

    public PoolResult(boolean victory, int score) {
        this.victory = victory;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public boolean isVictory() {
        return victory;
    }

    @Override
    public String toString() {
        return (victory ? "V" : "D") + Integer.toString(score);
    }


}
