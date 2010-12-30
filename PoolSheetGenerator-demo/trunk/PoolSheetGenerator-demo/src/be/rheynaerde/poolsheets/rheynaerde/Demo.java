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

package be.rheynaerde.poolsheets.rheynaerde;

import be.rheynaerde.poolsheets.StandardPoolSheet;
import be.rheynaerde.poolsheets.configuration.defaultconfiguration.DefaultStandardPoolSheetConfiguration;

import com.itextpdf.text.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Locale;

/**
 * Demonstration of some pool sheets adapted to the house style of a club.
 * 
 * @author nvcleemp
 */
public class Demo {

    private static final URL LOGO_URL =
            Demo.class.getResource(
                "/be/rheynaerde/poolsheets/rheynaerde/rheynaerde-logo.jpg");

    private static final Locale DUTCH = new Locale("nl");

    private Demo(){
        //private constructor
    }

    public static void main(String[] args) {
        try{
            File f = new File("rheynaerde-demo.pdf");
            StandardPoolSheet sps =
                    new StandardPoolSheet(9, Image.getInstance(LOGO_URL));
            sps.export(new FileOutputStream(f));
            f = new File("rheynaerde-demo_nl.pdf");
            sps = new StandardPoolSheet(
                    new DefaultStandardPoolSheetConfiguration(
                        8, 20f,Image.getInstance(LOGO_URL), DUTCH));
            sps.export(new FileOutputStream(f));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
