/* SingleTeamPanel.java
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

package be.rheynaerde.pufmanager.gui.teamcreator;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Team;
import be.rheynaerde.pufmanager.data.listener.TeamListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
class SingleTeamPanel extends JPanel implements TeamListener{

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.teamcreator");

    private Team team;
    private Competition competition;
    private ListSelectionModel unassignedFencerSelectionModel;
    private ListSelectionModel teamFencerSelectionModel;

    private TeamFencersModel fencersModel = new TeamFencersModel();

    public SingleTeamPanel(Team team, Competition competition, ListSelectionModel unassignedFencerSelectionModel) {
        this.team = team;
        this.competition = competition;
        this.unassignedFencerSelectionModel = unassignedFencerSelectionModel;
        initGui();
        team.addListener(this);
    }

    private void initGui(){
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                        )
                    )
                );

        JList list = new JList(fencersModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        teamFencerSelectionModel = list.getSelectionModel();

        add(getButtonsPanel());
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(new JScrollPane(list));
    }

    private JPanel getButtonsPanel(){
        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
        panel.add(new JButton(new AddFencerAction()));
        panel.add(new JButton(new RemoveFencerAction()));
        panel.add(new JButton(new RemoveTeamAction()));
        return panel;
    }

    public void fencerAdded(Fencer fencer, int index) {
        fencersModel.fencerAdded(index);
    }

    public void fencerRemoved(Fencer fencer, int index) {
        fencersModel.fencerRemoved(index);
    }

    public void nameChanged(String oldName, String newName) {
        //
    }

    public void targetSizeChanged(int oldSize, int newSize) {
        //
    }

    private class AddFencerAction extends AbstractAction implements ListSelectionListener{

        public AddFencerAction() {
            super(BUNDLE.getString("team.panel.add"));
            unassignedFencerSelectionModel.addListSelectionListener(this);
            setEnabled();
        }

        public void actionPerformed(ActionEvent e) {
            List<Fencer> fencers = new ArrayList<Fencer>();
            for (int i = unassignedFencerSelectionModel.getMinSelectionIndex();
                    i <= unassignedFencerSelectionModel.getMaxSelectionIndex();
                    i++) {
                if(unassignedFencerSelectionModel.isSelectedIndex(i)){
                    fencers.add(competition.getUnassignedFencer(i));
                }
            }
            for (Fencer fencer : fencers) {
                team.addFencer(fencer);
                competition.removeUnassignedFencer(fencer);
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            setEnabled();
        }

        private void setEnabled() {
            setEnabled(!unassignedFencerSelectionModel.isSelectionEmpty());
        }

    }

    private class RemoveFencerAction extends AbstractAction implements ListSelectionListener{

        public RemoveFencerAction() {
            super(BUNDLE.getString("team.panel.remove"));
            teamFencerSelectionModel.addListSelectionListener(this);
            setEnabled();
        }

        public void actionPerformed(ActionEvent e) {
            List<Fencer> fencers = new ArrayList<Fencer>();
            for (int i = teamFencerSelectionModel.getMinSelectionIndex();
                    i <= teamFencerSelectionModel.getMaxSelectionIndex();
                    i++) {
                if(teamFencerSelectionModel.isSelectedIndex(i)){
                    fencers.add(team.getFencer(i));
                }
            }
            for (Fencer fencer : fencers) {
                team.removeFencer(fencer);
                competition.addUnassignedFencer(fencer);
            }

        }

        public void valueChanged(ListSelectionEvent e) {
            setEnabled();
        }

        private void setEnabled() {
            setEnabled(!teamFencerSelectionModel.isSelectionEmpty());
        }

    }

    private class RemoveTeamAction extends AbstractAction {

        public RemoveTeamAction() {
            super(BUNDLE.getString("team.panel.removeteam"));
        }

        public void actionPerformed(ActionEvent e) {
            competition.removeTeam(team);
        }

    }

    private final class TeamFencersModel extends AbstractListModel {

        public int getSize() {
            return team.getTeamSize();
        }

        public Object getElementAt(int index) {
            return team.getFencer(index);
        }

        public void fencerAdded(int index){
            fireIntervalAdded(this, index, index);
        }

        public void fencerRemoved(int index){
            fireIntervalRemoved(this, index, index);
        }

    }

}
