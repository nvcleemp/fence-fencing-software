/* PoolDialog.java
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

package be.rheynaerde.pufmanager.gui.dialogs;

import be.rheynaerde.pufmanager.data.DelegatePool;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Match;
import be.rheynaerde.pufmanager.data.Pool;
import be.rheynaerde.pufmanager.data.Team;
import be.rheynaerde.pufmanager.gui.PoolPanel;
import be.rheynaerde.pufmanager.gui.TeamsPoolPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class PoolDialog {

    private PoolDialog() {
        //private constructor
    }

    public static void showPoolDialog(Team team, Pool pool) {
        List<Fencer> fencers = new ArrayList<Fencer>();
        for (int i = 0; i < team.getTeamSize(); i++) {
            fencers.add(team.getFencer(i));
        }
        showPoolDialog(null, team.getTeamName(), new DelegatePool(pool, fencers));
    }

    public static void showPoolDialog(Match match, Pool pool) {
        List<Fencer> fencers = new ArrayList<Fencer>();
        for (int i = 0; i < match.getFirstTeam().getTeamSize(); i++) {
            fencers.add(match.getFirstTeam().getFencer(i));
        }
        for (int i = 0; i < match.getSecondTeam().getTeamSize(); i++) {
            fencers.add(match.getSecondTeam().getFencer(i));
        }
        String title = String.format("%s vs %s", 
                                match.getFirstTeam().getTeamName(),
                                match.getSecondTeam().getTeamName());
        if(match.getRound().includeInternalBouts()){
            showPoolDialog(null, title, new DelegatePool(pool, fencers));
        } else {
            showTeamsPoolDialog(null, title, new DelegatePool(pool, fencers), match.getFirstTeam(), match.getSecondTeam());
        }
    }


    public static void showPoolDialog(JFrame parent, String title, Pool pool) {
        JDialog dialog = new JDialog(parent, title);
        dialog.setContentPane(new PoolPanel(pool));
        dialog.pack();
        dialog.setVisible(true);
    }
    
    public static void showTeamsPoolDialog(JFrame parent, String title, Pool pool, Team team1, Team team2) {
        JDialog dialog = new JDialog(parent, title);
        dialog.setContentPane(new TeamsPoolPanel(pool, team1, team2));
        dialog.pack();
        dialog.setVisible(true);
    }


}
