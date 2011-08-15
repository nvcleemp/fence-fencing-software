/* PufManager.java
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

package be.rheynaerde.pufmanager;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.CompetitionSettings;
import be.rheynaerde.pufmanager.gui.PufManagerFrame;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class PufManager {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.defaults");

    private PufManager() {
        //do not instantiate
    }
    
    public static void main(String[] args) {
        CompetitionSettings settings = new CompetitionSettings();
        settings.setTitle(BUNDLE.getString("default.title"));
        settings.setSubtitle(DateFormat.getDateInstance().format(new Date()));
        settings.setLocale(Locale.getDefault());

        final Competition competition = new Competition(settings);
        JFrame frame = new PufManagerFrame(competition);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
