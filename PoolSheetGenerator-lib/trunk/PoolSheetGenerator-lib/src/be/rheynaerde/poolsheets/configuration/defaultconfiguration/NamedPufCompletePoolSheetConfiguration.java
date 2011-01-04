/* NamedPufCompletePoolSheetConfiguration.java
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

package be.rheynaerde.poolsheets.configuration.defaultconfiguration;

import com.itextpdf.text.Image;

import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public class NamedPufCompletePoolSheetConfiguration extends DefaultPufCompletePoolSheetConfiguration {

    private String[][] teams = new String[2][];

    public NamedPufCompletePoolSheetConfiguration(String[] teamA, String[] teamB, float squareCellSize, Image image, Locale locale) {
        this(teamA, teamB, squareCellSize, image, locale, null, null);
    }

    public NamedPufCompletePoolSheetConfiguration(String[] teamA, String[] teamB, float squareCellSize, Image image, Locale locale, String title, String subtitle) {
        super(teamA.length, teamB.length, squareCellSize, image, locale, title, subtitle);
        this.teams[0] = teamA;
        this.teams[1] = teamB;
    }

    @Override
    public String getNamePlayer(int team, int i) {
        return teams[team-1][i-1];
    }

}
