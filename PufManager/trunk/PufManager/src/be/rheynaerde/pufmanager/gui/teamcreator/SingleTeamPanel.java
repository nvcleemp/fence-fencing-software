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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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

    private JTextField teamNameTextField;
    private JFormattedTextField teamTargetSizeTextField;

    private TeamFencersModel fencersModel = new TeamFencersModel();

    private Color defaultBackground;

    public SingleTeamPanel(Team team, Competition competition, ListSelectionModel unassignedFencerSelectionModel) {
        this.team = team;
        this.competition = competition;
        this.unassignedFencerSelectionModel = unassignedFencerSelectionModel;
        initGui();
        defaultBackground = getBackground();
        checkTeamSize();
        team.addListener(this);
    }

    private void initGui(){
        setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                        )
                    )
                );

        JList list = new JList(fencersModel){

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        list.setOpaque(false);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setCellRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setOpaque(isSelected);
                return this;
            }
        });
        teamFencerSelectionModel = list.getSelectionModel();
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton addFencerButton = new JButton(new AddFencerAction());
        JButton removeFencerButton = new JButton(new RemoveFencerAction());
        JButton removeTeamButton = new JButton(new RemoveTeamAction());

        JLabel teamNameLabel = new JLabel(BUNDLE.getString("team.panel.name"));
        teamNameTextField = new JTextField(team.getTeamName());
        teamNameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                team.setTeamName(teamNameTextField.getText().trim());
            }
        });
        JLabel teamSizeLabel = new JLabel(BUNDLE.getString("team.panel.size"));
        teamTargetSizeTextField = new JFormattedTextField(team.getTargetSize());
        teamTargetSizeTextField.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        teamTargetSizeTextField.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                team.setTargetSize((Integer)teamTargetSizeTextField.getValue());
            }
        });
        teamTargetSizeTextField.setColumns(3);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                    .addGroup(
                        groupLayout.createSequentialGroup()
                            .addComponent(teamNameLabel)
                            .addComponent(teamNameTextField)
                            .addComponent(teamSizeLabel)
                            .addComponent(teamTargetSizeTextField)
                    )
                    .addGroup(
                        groupLayout.createSequentialGroup()
                            .addGroup(
                                groupLayout.createParallelGroup()
                                    .addComponent(addFencerButton)
                                    .addComponent(removeFencerButton)
                                    .addComponent(removeTeamButton)
                            )
                            .addComponent(listScrollPane)
                    )
                );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                    .addGroup(
                        groupLayout.createParallelGroup()
                            .addComponent(teamNameLabel)
                            .addComponent(teamNameTextField)
                            .addComponent(teamSizeLabel)
                            .addComponent(teamTargetSizeTextField)
                    )
                    .addGroup(
                        groupLayout.createParallelGroup()
                            .addGroup(
                                groupLayout.createSequentialGroup()
                                    .addComponent(addFencerButton)
                                    .addComponent(removeFencerButton)
                                    .addComponent(removeTeamButton)
                            )
                            .addComponent(listScrollPane)
                    )
                );

        groupLayout.linkSize(addFencerButton, removeFencerButton, removeTeamButton);

        setLayout(groupLayout);
    }

    public void fencerAdded(Fencer fencer, int index) {
        fencersModel.fencerAdded(index);
        checkTeamSize();
    }

    public void fencerRemoved(Fencer fencer, int index) {
        fencersModel.fencerRemoved(index);
        checkTeamSize();
    }

    public void nameChanged(String oldName, String newName) {
        if(!teamNameTextField.getText().trim().equals(newName)){
            teamNameTextField.setText(newName);
        }
    }

    public void targetSizeChanged(int oldSize, int newSize) {
        checkTeamSize();
    }

    private void checkTeamSize(){
        if(team.getTeamSize()<team.getTargetSize())
            setBackground(defaultBackground);
        else if(team.getTeamSize()==team.getTargetSize())
            setBackground(Color.GREEN);
        else
            setBackground(Color.RED);
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
