/* MatchListCellRenderer.java
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

package be.rheynaerde.pufmanager.gui;

import be.rheynaerde.pufmanager.data.Match;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author nvcleemp
 */
public class MatchListCellRenderer extends DefaultListCellRenderer {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.resources");

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, false, cellHasFocus);
        if(value instanceof Match){
            Match m = (Match)value;
            setText(MessageFormat.format(BUNDLE.getString("match.listrenderer.text"),
                                    m.getFirstTeam().getTeamName(),
                                    m.getSecondTeam().getTeamName()));
        }
        return this;
    }

}
