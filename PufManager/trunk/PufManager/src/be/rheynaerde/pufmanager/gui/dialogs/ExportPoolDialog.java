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
import be.rheynaerde.pufmanager.data.util.SummaryValue;
import be.rheynaerde.pufmanager.data.util.SummaryValuesFactory;
import be.rheynaerde.pufmanager.io.PoolHtmlExporter;
import be.rheynaerde.pufmanager.preferences.PufManagerPreferences;
import be.rheynaerde.pufmanager.util.EnumComboBoxModel;
import be.rheynaerde.pufmanager.util.SafeFileOpener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Dialog window which shows some options to export a pool sheet to HTML.
 * 
 * @author nvcleemp
 */
public class ExportPoolDialog extends JDialog{

    private static final ResourceBundle BUNDLE = 
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.dialogs");

    private static final String OK_BUTTON = "OK";
    private static final String CANCEL_BUTTON = "CANCEL";

    private EnumComboBoxModel<ExportOptions> exportSelection = 
            new EnumComboBoxModel<ExportOptions>(ExportOptions.class);
    private JCheckBox includePositions;

    private String lastPressed = OK_BUTTON;
    
    public ExportPoolDialog() {
        this(null);
    }
    
    public ExportPoolDialog(Frame owner) {
        super(owner, BUNDLE.getString("exportpooldialog.title"), true);
        initGui();
    }

    private void initGui(){
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));

        buttonsPanel.add(Box.createHorizontalGlue());

        JButton okButton = new JButton(BUNDLE.getString("exportpooldialog.ok"));
        okButton.addActionListener(
                new ExportDialogButtonActionListener(OK_BUTTON));
        buttonsPanel.add(okButton);

        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton cancelButton = new JButton(
                BUNDLE.getString("exportpooldialog.cancel"));
        cancelButton.addActionListener(
                new ExportDialogButtonActionListener(CANCEL_BUTTON));
        buttonsPanel.add(cancelButton);
        
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
        final JComboBox exportSelectionComboBox = 
                new JComboBox(exportSelection);
        exportSelectionComboBox.setRenderer(
                new DefaultListCellRenderer(){

                    @Override
                    public Component getListCellRendererComponent(
                            JList list, Object value, int index, 
                            boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(
                                list, value, index, isSelected, cellHasFocus);
                        if(value instanceof ExportOptions){
                            setText(
                               BUNDLE.getString(((ExportOptions)value).getKey())
                                    );
                        }
                        return this;
                    }
        
                });
        optionsPanel.add(exportSelectionComboBox);
        includePositions = new JCheckBox(
                BUNDLE.getString("exportpooldialog.includePositions"),true);
        //TODO: replace by summary values selector
        optionsPanel.add(includePositions);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(panel);
        
        pack();
    }

    public void exportPool(CompetitionPool pool, Locale locale){
        setVisible(true);
        ExportOptions chosenOption = exportSelection.getSelectedItem();
        if(lastPressed.equals(OK_BUTTON) && chosenOption!=null){
            try {
                if(includePositions.isSelected()){
                    chosenOption.export(
                            PoolHtmlExporter.exportPool(pool, locale), this.getOwner());
                } else {
                    List<SummaryValue> summaries = 
                            SummaryValuesFactory.getCompactList();
                    summaries.remove(SummaryValuesFactory.POSITION);
                    chosenOption.export(
                            PoolHtmlExporter.exportPool(pool, summaries, locale),
                            this.getOwner());
                }
            } catch (IOException ex) {
                Logger.getLogger(ExportPoolDialog.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private class ExportDialogButtonActionListener implements ActionListener {

        private String button;

        public ExportDialogButtonActionListener(String button) {
            this.button = button;
        }

        public void actionPerformed(ActionEvent e) {
            lastPressed = button;
            setVisible(false);
        }
    }
    
    private static enum ExportOptions {
        COPY_TO_CLIPBOARD("exportpooldialog.clipboard"){
            public void export(String s, Component parent){
                StringSelection contents = new StringSelection(s);
                Clipboard clipboard = 
                        Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(contents, null);
            }
        },
        WRITE_TO_FILE("exportpooldialog.file"){
            //TODO: create file chooser that refers to last used location
            private SafeFileOpener safeFileOpener = 
                    new SafeFileOpener(new JFileChooser(
                            PufManagerPreferences.getInstance()
                                        .getStringPreference(
                                            PufManagerPreferences.Preference.
                                                               EXPORT_DIRECTORY)
                            ));
            
            public void export(String s, Component parent){
                File f = safeFileOpener.getSaveFile(parent);
                if(f != null){
                    try {
                        Writer writer = new FileWriter(f);
                        writer.write(s);
                        writer.close();
                        PufManagerPreferences.getInstance()
                                .setStringPreference(
                                    PufManagerPreferences.Preference.
                                                EXPORT_DIRECTORY,
                                    f.getParent());
                    } catch (IOException ex) {
                        Logger.getLogger(ExportPoolDialog.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            }
        },
        WRITE_TO_STDOUT("exportpooldialog.stdout"){
            public void export(String s, Component parent){
                System.out.println(s);
            }
        };
        
        private String key;
        
        private ExportOptions(String key){
            this.key = key;
        }
        
        public abstract void export(String s, Component parent);
        
        public String getKey(){
            return key;
        }
    }
}
