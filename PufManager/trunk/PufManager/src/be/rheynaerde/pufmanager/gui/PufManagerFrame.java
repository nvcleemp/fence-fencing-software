/* PufManagerFrame.java
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
import be.rheynaerde.pufmanager.gui.actions.CreateRoundsPdfAction;
import be.rheynaerde.pufmanager.gui.actions.ImportTextFile;
import be.rheynaerde.pufmanager.gui.teamcreator.TeamCreator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author nvcleemp
 */
public class PufManagerFrame extends JFrame {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.resources");

    private Competition competition;

    public PufManagerFrame(Competition competition) throws HeadlessException {
        super(MessageFormat.format(BUNDLE.getString("pufmanager.title"),
                competition.getSettings().getTitle(),
                competition.getSettings().getSubtitle()));
        this.competition = competition;
        initGui();
        pack();
    }

    private void initGui(){
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab(BUNDLE.getString("pufmanager.tabs.teams"), new TeamCreator(competition));
        tabs.addTab(BUNDLE.getString("pufmanager.tabs.rounds"), new JScrollPane(new RoundsView(competition),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        add(tabs, BorderLayout.CENTER);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JButton(new CreateRoundsPdfAction(competition)));
        panel.add(new JButton(new ImportTextFile(this, competition)));
        add(panel, BorderLayout.SOUTH);
    }
}
