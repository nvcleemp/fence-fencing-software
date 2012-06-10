/* ExportPoolDialog.java
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

import be.rheynaerde.pufmanager.data.CompetitionPool;
import be.rheynaerde.pufmanager.gui.models.RankingTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Dialog window which displays the ranking of a pool.
 * 
 * @author nvcleemp
 */
public class RankingDialog extends JDialog{

    private static final ResourceBundle BUNDLE = 
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.dialogs");
    
    public RankingDialog(CompetitionPool competitionPool) {
        this(null, competitionPool);
    }
    
    public RankingDialog(Frame owner, CompetitionPool competitionPool) {
        super(owner, BUNDLE.getString("rankingdialog.title"), true);
        initGui(competitionPool);
    }

    private void initGui(CompetitionPool competitionPool){
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));

        buttonsPanel.add(Box.createHorizontalGlue());

        JButton okButton = new JButton(BUNDLE.getString("rankingdialog.ok"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(okButton);
        
        final JTable rankingTable = new JTable(new RankingTableModel(competitionPool));
        rankingTable.setDefaultRenderer(Object.class, new RankingCellRenderer());
        rankingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //Set the width for the column containing the numbers.
        int width = rankingTable.getDefaultRenderer(Object.class)
                .getTableCellRendererComponent(
                    rankingTable, 999, false, false, 0, 0)
                .getPreferredSize().width;
        rankingTable.getColumnModel().getColumn(0).setPreferredWidth(width);
        rankingTable.getColumnModel().getColumn(0).setMaxWidth(width);
        
        //Set the width for the column containing the names.
        //Since the pool usually contains not that much people,
        //we just find the longest name and set that as minimum.
        width = 100;
        for (int i = 0; i < competitionPool.getPoolSize(); i++) {
            int currentWidth = rankingTable.getDefaultRenderer(Object.class)
                .getTableCellRendererComponent(
                    rankingTable, competitionPool.getFencerAt(i).getName(),
                    false, false, 0, 1)
                .getPreferredSize().width;
            if(currentWidth>width)
                width = currentWidth;
        }
        rankingTable.getColumnModel().getColumn(1).setMinWidth(width+50);
        
        
        JPanel rankingPanel = new JPanel(new BorderLayout(0, 10));
        rankingPanel.add(rankingTable, BorderLayout.NORTH);
        rankingPanel.add(
                new JButton(new CopyToClipboardAction(competitionPool)),
                BorderLayout.SOUTH);
        
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(rankingPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(panel);
        
        pack();
    }
    
    private static class CopyToClipboardAction extends AbstractAction {
        
        private final CompetitionPool competitionPool;

        public CopyToClipboardAction(CompetitionPool competitionPool) {
            super(BUNDLE.getString("rankingdialog.clipboard"));
            this.competitionPool = competitionPool;
        }

        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < competitionPool.getPoolSize(); i++) {
                sb.append(i+1).append(") ")
                       .append(competitionPool.getPositions().get(i).getName())
                       .append("\n");
            }
            StringSelection contents = new StringSelection(sb.toString());
            Clipboard clipboard = 
                    Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(contents, null);
        }
        
    }
    
    /**
     * Extension to the default renderer which shades the first, second and 
     * third row in the colours gold, silver and bronze respectively.
     */
    private static class RankingCellRenderer extends DefaultTableCellRenderer {
        
        Color[] colours = {Color.YELLOW, Color.LIGHT_GRAY, Color.ORANGE.darker()};

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(!isSelected && row < 3){
                setBackground(colours[row]);
            }
            return this;
        }
        
    }
}
