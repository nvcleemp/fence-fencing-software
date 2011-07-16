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

import be.rheynaerde.pufmanager.data.Match;
import be.rheynaerde.pufmanager.data.Round;
import be.rheynaerde.pufmanager.data.Team;
import be.rheynaerde.pufmanager.gui.dialogs.PoolDialog;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nvcleemp
 */
public class RoundPanel extends JPanel{

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.resources");

    private Round round;

    public RoundPanel(Round round) {
        super(new GridLayout(1, 0, 0, 0));
        this.round = round;
        initGui();
    }

    private void initGui(){
        JLabel roundNumber = new JLabel(Integer.toString(round.getRoundNumber()+1));
        roundNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        JLabel labelMatches = new JLabel(BUNDLE.getString("roundpanel.matchlist.label"));
        labelMatches.setVerticalAlignment(JLabel.TOP);
        final JList matchesList = new JList(round.getMatchesModel());
        matchesList.setOpaque(false);
        matchesList.setCellRenderer(new MatchListCellRenderer());
        matchesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        matchesList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=2){
                    if(matchesList.getSelectedIndex() >= 0 &&
                            matchesList.getSelectedIndex() <
                                        round.getMatches().size()){
                        Match match = round.getMatches()
                                .get(matchesList.getSelectedIndex());
                        PoolDialog.showPoolDialog(match, round
                                .getCompetition().getCompetitionPool());
                    }
                }
            }
        });
        
        JLabel labelResting = new JLabel(BUNDLE.getString("roundpanel.restingteamlist.label"));
        labelResting.setVerticalAlignment(JLabel.TOP);
        final JList restingList = new JList(round.getRestingTeamsModel());
        if(round.includeInternalBouts()){
            restingList.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()>=2){
                        if(restingList.getSelectedIndex() >= 0 &&
                                restingList.getSelectedIndex() <
                                            round.getRestingTeams().size()){
                            Team team = round.getRestingTeams()
                                    .get(restingList.getSelectedIndex());
                            PoolDialog.showPoolDialog(team, round
                                    .getCompetition().getCompetitionPool());
                        }
                    }
                }
            });
        }
        restingList.setOpaque(false);
        restingList.setCellRenderer(new RoundsViewListCellRenderer());
        restingList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane matchesListScrollPane = new JScrollPane(matchesList);
        JScrollPane restingListScrollPane = new JScrollPane(restingList);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                    .addComponent(roundNumber)
                    .addComponent(labelMatches)
                    .addComponent(matchesListScrollPane)
                    .addComponent(labelResting)
                    .addComponent(restingListScrollPane)
                );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup()
                    .addComponent(roundNumber)
                    .addComponent(labelMatches)
                    .addComponent(matchesListScrollPane)
                    .addComponent(labelResting)
                    .addComponent(restingListScrollPane)
                );

        groupLayout.linkSize(labelMatches, labelResting);
        groupLayout.linkSize(matchesListScrollPane, restingListScrollPane);

        setLayout(groupLayout);

    }

}
