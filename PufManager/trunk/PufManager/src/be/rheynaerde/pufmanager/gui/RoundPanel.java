/* RoundPanel.java
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

import be.rheynaerde.pufmanager.data.Round;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class RoundPanel extends JPanel{

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.resources");

    private Round round;

    public RoundPanel(Round round) {
        super(new GridLayout(1, 0, 2, 0));
        this.round = round;
        initGui();
    }

    private void initGui(){
        JLabel label = new JLabel(BUNDLE.getString("roundpanel.matchlist.label"));
        label.setVerticalAlignment(JLabel.TOP);
        add(label);
        JList matchesList = new JList(round.getMatchesModel());
        matchesList.setCellRenderer(new MatchListCellRenderer());
        add(matchesList);
        label = new JLabel(BUNDLE.getString("roundpanel.restingteamlist.label"));
        label.setVerticalAlignment(JLabel.TOP);
        add(label);
        add(new JList(round.getRestingTeamsModel()));
    }

}
