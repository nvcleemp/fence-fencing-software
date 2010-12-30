/* Demo.java
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

package be.rheynaerde.poolsheets;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Demonstration of the basic functionality of the different types of
 * pool sheets.
 *
 * @author nvcleemp
 */
public class Demo {
    public static void main(String[] args) {
        try{
            File f = new File("ClubPoolSheet-test.pdf");
            ClubPoolSheet sps = new ClubPoolSheet(20);
            sps.export(new FileOutputStream(f));
        } catch(Exception e) {
            e.printStackTrace();
        }
        //
        try{
            File f = new File("PufTeamPoolSheet-test.pdf");
            PufTeamPoolSheet sps = new PufTeamPoolSheet(4, 4);
            sps.export(new FileOutputStream(f));
        } catch(Exception e) {
            e.printStackTrace();
        }
        //
        try{
            File f = new File("StandardPoolSheet-test.pdf");
            StandardPoolSheet sps = new StandardPoolSheet(10);
            sps.export(new FileOutputStream(f));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
