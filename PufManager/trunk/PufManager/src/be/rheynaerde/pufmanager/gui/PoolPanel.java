/* PoolPanel.java
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

import be.rheynaerde.pufmanager.data.DefaultPool;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.PoolResult;
import be.rheynaerde.pufmanager.data.listener.PoolAdapter;
import be.rheynaerde.pufmanager.data.listener.PoolListener;
import be.rheynaerde.pufmanager.data.util.PoolRowHeaderTableModel;
import be.rheynaerde.pufmanager.data.util.PoolTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author nvcleemp
 */
public class PoolPanel extends JPanel {

    private DefaultPool pool;
    private PoolListener poolListener = new PoolAdapter() {

        @Override
        public void fencerAdded(Fencer fencer) {
            calculateRowHeaderColumnWidth();
            calculatePoolTableColumnWidth();
        }

        @Override
        public void fencerMoved(Fencer fencer) {
            calculateRowHeaderColumnWidth();
            calculatePoolTableColumnWidth();
        }

        @Override
        public void fencerRemoved(Fencer fencer) {
            calculateRowHeaderColumnWidth();
            calculatePoolTableColumnWidth();
        }

        @Override
        public void fencersChanged() {
            calculateRowHeaderColumnWidth();
            calculatePoolTableColumnWidth();
        }
    };
    private JTable rowHeader;
    private JTable poolTable;

    public PoolPanel(DefaultPool pool) {
        super(new GridLayout(0, 1));
        this.pool = pool;
        initGui();
        pool.addPoolListener(poolListener);
    }

    protected final void initGui() {
        poolTable = new JTable(new PoolTableModel(pool));
        poolTable.setDefaultRenderer(PoolResult.class, new PoolResultTableCellRenderer());
        poolTable.setDefaultRenderer(Object.class, new PoolTableCellRenderer());
        poolTable.setRowHeight(poolTable.getRowHeight()*2);
        calculatePoolTableColumnWidth();
        JScrollPane poolPane = new JScrollPane(poolTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rowHeader = new JTable(new PoolRowHeaderTableModel(pool)){

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return getPreferredSize();
            }
        };
        rowHeader.setRowHeight(rowHeader.getRowHeight()*2);
        calculateRowHeaderColumnWidth();
        poolPane.setRowHeaderView(rowHeader);
        rowHeader.setSelectionModel(poolTable.getSelectionModel());
        add(poolPane);
    }

    protected final void calculateRowHeaderColumnWidth() {
        rowHeader.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < rowHeader.getColumnCount(); i++) {
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) rowHeader.getColumnModel();
            TableColumn col = colModel.getColumn(i);
            int width = 0;

            TableCellRenderer renderer = col.getHeaderRenderer();
            for (int r = 0; r < rowHeader.getRowCount(); r++) {
                renderer = rowHeader.getCellRenderer(r, i);
                Component comp = renderer.getTableCellRendererComponent(rowHeader, rowHeader.getValueAt(r, i),
                        false, false, r, i);
                width = Math.max(width, comp.getPreferredSize().width);
            }
            col.setPreferredWidth(width + 2);
        }
    }

    protected final void calculatePoolTableColumnWidth() {
        poolTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < poolTable.getColumnCount(); i++) {
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) poolTable.getColumnModel();
            colModel.getColumn(i).setPreferredWidth(poolTable.getRowHeight());
        }
    }

    private static class PoolTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            return this;
        }

    }

    private static class PoolResultTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(row==column)
                setBackground(Color.BLACK);
            else
                setBackground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            return this;
        }

    }
}
