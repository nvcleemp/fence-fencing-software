/* TeamCreator.java
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
import be.rheynaerde.pufmanager.data.listener.CompetitionAdapter;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;
import be.rheynaerde.pufmanager.data.util.UnassignedFencersListModel;
import be.rheynaerde.pufmanager.gui.dialogs.FencerDialog;
import be.rheynaerde.pufmanager.util.ScrollablePanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author nvcleemp
 */
public class TeamCreator extends JPanel {
    
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.teamcreator");

    private static int teamCounter = 1;

    private Competition competition;

    private CompetitionListener competitionListener = new CompetitionAdapter() {

        @Override
        public void teamAdded(Team team, int index) {
            addTeamPanel(team);
        }

        @Override
        public void teamRemoved(Team team, int index) {
            removeTeamPanel(team);
        }
    };

    private final UnassignedFencersListModel unassignedFencersModel;
    private ListSelectionModel unassignedFencersSelectionModel;

    private JPanel teamsPanel = new ScrollablePanel(new GridLayout(0, 1, 5, 5));
    private Map<Team, SingleTeamPanel> teamToPanel = new HashMap<Team, SingleTeamPanel>();

    public TeamCreator(Competition competition) {
        this.competition = competition;
        competition.addListener(competitionListener);
        unassignedFencersModel = new UnassignedFencersListModel(competition);
        initGui();
    }

    private void initGui(){

        JPanel unassignedFencersPanel = createUnassignedFencersPanel();
        JPanel teamsSectionPanel = createTeamsSection();
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addComponent(unassignedFencersPanel)
                .addComponent(teamsSectionPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addComponent(unassignedFencersPanel)
                .addComponent(teamsSectionPanel)
        );

        setLayout(layout);

    }

    private JPanel createTeamsSection() {

        for (int i = 0; i < competition.getTeamCount(); i++) {
            addTeamPanel(competition.getTeam(i));
        }
        
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBorder(getTitledBorder(BUNDLE.getString("teamcreator.teamssection.title")));
        JScrollPane scrollPane = new JScrollPane(teamsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(new JButton(new AbstractAction(BUNDLE.getString("add.new.team")) {

            public void actionPerformed(ActionEvent e) {
                competition.addTeam(new Team(String.format(BUNDLE.getString("new.team.name"), teamCounter++)));
            }
        }), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createUnassignedFencersPanel(){
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBorder(getTitledBorder(BUNDLE.getString("teamcreator.unassigned.title")));
        final JList list = new JList(unassignedFencersModel);
        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=2){
                    if(list.getSelectedIndex() >= 0 &&
                            list.getSelectedIndex() < unassignedFencersModel.getSize()){
                        Fencer fencer = unassignedFencersModel.getElementAt(list.getSelectedIndex());
                        
                        FencerDialog dialog = new FencerDialog();
                        dialog.setData(fencer);
                        if(dialog.showDialog())
                            dialog.getData(fencer);
                    }
                }
            }
        });
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        unassignedFencersSelectionModel = list.getSelectionModel();
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1));
        buttonsPanel.add(new JButton(new AddFencerAction(competition)));
        buttonsPanel.add(new JButton(new RemoveFencerAction(competition, list.getSelectionModel(), unassignedFencersModel)));
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addTeamPanel(Team team){
        if(teamToPanel.containsKey(team))
            throw new IllegalStateException("This team is already on the panel");

        SingleTeamPanel teamPanel = new SingleTeamPanel(team, competition, unassignedFencersSelectionModel);
        teamsPanel.add(teamPanel);
        teamToPanel.put(team, teamPanel);
        teamsPanel.revalidate();
        teamsPanel.repaint();
        if(competition.getTeamCount()==1){
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    SwingUtilities.getWindowAncestor(teamsPanel).pack();
                }
            });
        }
    }

    private void removeTeamPanel(Team team){
        SingleTeamPanel panel = teamToPanel.remove(team);
        if(panel==null){
            throw new IllegalStateException("This team wasn't on the panel");
        } else {
            teamsPanel.remove(panel);
            teamsPanel.revalidate();
            teamsPanel.repaint();
        }
    }

    private static Border getTitledBorder(String title){
        return BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        title
                        ),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    );
    }
    
}
