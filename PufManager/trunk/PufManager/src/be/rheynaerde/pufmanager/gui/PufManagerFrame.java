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
import be.rheynaerde.pufmanager.data.CompetitionSettings;
import be.rheynaerde.pufmanager.data.CompetitionSettings.Setting;
import be.rheynaerde.pufmanager.data.listener.CompetitionSettingsListener;
import be.rheynaerde.pufmanager.gui.actions.CreateRoundsPdfAction;
import be.rheynaerde.pufmanager.gui.actions.ExportPoolSheetAction;
import be.rheynaerde.pufmanager.gui.actions.ShowRankingAction;
import be.rheynaerde.pufmanager.gui.actions.ImportTextFile;
import be.rheynaerde.pufmanager.gui.actions.LoadCompetitionAction;
import be.rheynaerde.pufmanager.gui.actions.SaveCompetitionAction;
import be.rheynaerde.pufmanager.gui.dialogs.SettingsDialog;
import be.rheynaerde.pufmanager.gui.teamcreator.TeamCreator;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.Scrollable;

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
        this.competition.getSettings().addListener(new CompetitionSettingsListener() {

            public void settingChanged(CompetitionSettings competitionSettings, Setting setting) {
                if(Setting.SUBTITLE.equals(setting) || Setting.TITLE.equals(setting)){
                    setTitle(MessageFormat.format(BUNDLE.getString("pufmanager.title"),
                PufManagerFrame.this.competition.getSettings().getTitle(),
                PufManagerFrame.this.competition.getSettings().getSubtitle()));
                }
            }
        });
        initGui();
        pack();
    }

    private void initGui(){
        initMenuBar();
        setLayout(new GridLayout(1,1));
        FrameTrackingTabbedPane tabs = new FrameTrackingTabbedPane();
        tabs.addTab(BUNDLE.getString("pufmanager.tabs.teams"), new TeamCreator(competition));
        tabs.addTab(BUNDLE.getString("pufmanager.tabs.rounds"), new JScrollPane(new RoundsView(competition),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        tabs.addTab(BUNDLE.getString("pufmanager.tabs.pool"), new PoolPanel(competition.getCompetitionPool()));
        final JScrollPane scrollPane = new JScrollPane(tabs);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);
    }

    private void initMenuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu(BUNDLE.getString("pufmanager.menu.file"));
        fileMenu.add(new JMenuItem(new LoadCompetitionAction(this)));
        fileMenu.add(new JMenuItem(new SaveCompetitionAction(this, competition)));
        fileMenu.add(new JMenuItem(new SaveCompetitionAction(this, competition, true)));
        fileMenu.addSeparator();
        fileMenu.add(new ImportTextFile(this, competition));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(BUNDLE.getString("pufmanager.menu.file.close")));
        bar.add(fileMenu);
        JMenu exportMenu = new JMenu(BUNDLE.getString("pufmanager.menu.export"));
        exportMenu.add(new CreateRoundsPdfAction(competition, this));
        exportMenu.add(new ExportPoolSheetAction(competition, this));
        exportMenu.add(new ShowRankingAction(competition, this));
        bar.add(exportMenu);
        JMenu actionsMenu = new JMenu(BUNDLE.getString("pufmanager.menu.actions"));
        actionsMenu.add(new AbstractAction(BUNDLE.getString("pufmanager.menu.actions.rearrange")) {
            public void actionPerformed(ActionEvent e) {
                competition.getCompetitionPool().rearrangeFencers();
            }
        });
        actionsMenu.add(new AbstractAction(BUNDLE.getString("pufmanager.menu.actions.settings")) {
            public void actionPerformed(ActionEvent e) {
                SettingsDialog.showSettingsDialog(PufManagerFrame.this, competition.getSettings());
            }
        });
        bar.add(actionsMenu);
        setJMenuBar(bar);
    }
    
    private void resetGui(){
        setVisible(false);
        setTitle(MessageFormat.format(BUNDLE.getString("pufmanager.title"),
                competition.getSettings().getTitle(),
                competition.getSettings().getSubtitle()));
        this.competition.getSettings().addListener(new CompetitionSettingsListener() {

            public void settingChanged(CompetitionSettings competitionSettings, Setting setting) {
                if(Setting.SUBTITLE.equals(setting) || Setting.TITLE.equals(setting)){
                    setTitle(MessageFormat.format(BUNDLE.getString("pufmanager.title"),
                PufManagerFrame.this.competition.getSettings().getTitle(),
                PufManagerFrame.this.competition.getSettings().getSubtitle()));
                }
            }
        });
        getContentPane().removeAll();
        initGui();
        pack();
        setVisible(true);
    }

    public void setCompetition(Competition competition) {
        if(competition!=null && !competition.equals(this.competition)){
            this.competition = competition;
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    resetGui();
                }
            });
        }
    }
    
    private static class FrameTrackingTabbedPane extends JTabbedPane implements Scrollable {

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 0;
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 0;
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        public boolean getScrollableTracksViewportHeight() {
            return true;
        }
        
    }
}
