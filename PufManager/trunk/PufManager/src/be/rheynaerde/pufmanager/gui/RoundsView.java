/* RoundsView.java
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

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Team;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class RoundsView extends JPanel implements CompetitionListener{

    private Competition competition;

    public RoundsView(Competition competition) {
        super(new GridLayout(0, 1, 0, 5));
        setBackground(Color.BLACK);
        this.competition = competition;
        competition.addListener(this);
        buildView();
    }

    private void buildView(){
        removeAll();
        for (int i = 0; i < competition.getRoundCount(); i++) {
            add(new RoundPanel(competition.getRound(i)));
        }
    }

    public void teamAdded(Team team, int index) {
        //
    }

    public void teamRemoved(Team team, int index) {
        //
    }

    public void unassignedFencerAdded(Fencer fencer, int index) {
        //
    }

    public void unassignedFencerRemoved(Fencer fencer, int index) {
        //
    }

    public void roundsChanged() {
        buildView();
    }
}
