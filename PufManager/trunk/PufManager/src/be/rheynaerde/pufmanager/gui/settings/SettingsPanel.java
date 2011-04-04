/* SettingsPanel.java
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

package be.rheynaerde.pufmanager.gui.settings;

import be.rheynaerde.pufmanager.data.CompetitionSettings;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author nvcleemp
 */
public class SettingsPanel extends JPanel {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.settings");

    private CompetitionSettings settings;
    private JTextField titleField;
    private JTextField subtitleField;
    private JComboBox langBox;
    private JFormattedTextField maxScoreField;

    public SettingsPanel(CompetitionSettings settings) {
        this.settings = settings;
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel titleLabel = new JLabel(BUNDLE.getString("setting.title.label"));
        JLabel subtitleLabel = new JLabel(BUNDLE.getString("setting.subtitle.label"));
        JLabel langLabel = new JLabel(BUNDLE.getString("setting.lang.label"));
        JLabel maxScoreLabel = new JLabel(BUNDLE.getString("setting.max.score.label"));

        titleField = new JTextField(settings.getTitle());
        subtitleField = new JTextField(settings.getSubtitle());
        langBox = new JComboBox(getAvailableLanguages());
        maxScoreField = new JFormattedTextField(settings.getMaximumScore());

        JButton saveButton = new JButton(new SaveAction());

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(titleLabel)
                        .addComponent(subtitleLabel)
                        .addComponent(langLabel)
                        .addComponent(maxScoreLabel)
                        .addComponent(saveButton)
                    )
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(titleField)
                        .addComponent(subtitleField)
                        .addComponent(langBox)
                        .addComponent(maxScoreField)
                    )
                );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addGroup(
                        layout.createParallelGroup()
                            .addComponent(titleLabel)
                            .addComponent(titleField)
                        )
                    .addGroup(
                        layout.createParallelGroup()
                            .addComponent(subtitleLabel)
                            .addComponent(subtitleField)
                        )
                    .addGroup(
                        layout.createParallelGroup()
                            .addComponent(langLabel)
                            .addComponent(langBox)
                        )
                    .addGroup(
                        layout.createParallelGroup()
                            .addComponent(maxScoreLabel)
                            .addComponent(maxScoreField)
                        )
                    .addComponent(saveButton)

                );

        langBox.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                try {
                    setText(BUNDLE.getString(value.toString()));
                } catch (MissingResourceException e) {
                    setText(BUNDLE.getString("unknown.language"));
                }
                return this;
            }
        });
    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            super(BUNDLE.getString("setting.save"));
        }

        public void actionPerformed(ActionEvent e) {
            settings.setTitle(titleField.getText());
            settings.setSubtitle(subtitleField.getText());
            settings.setLocale((Locale)langBox.getModel().getSelectedItem());
            settings.setMaximumScore((Integer)maxScoreField.getValue());
            fireSettingsSaved();
        }

    }

    private List<SettingsPanelListener> listeners = new ArrayList<SettingsPanelListener>();

    public void addSettingsPanelListener(SettingsPanelListener listener){
        listeners.add(listener);
    }

    public void removeSettingsPanelListener(SettingsPanelListener listener){
        listeners.remove(listener);
    }

    private void fireSettingsSaved(){
        for (SettingsPanelListener l : listeners) {
            l.settingsSaved();
        }
    }
    
    private static final Locale[] availableLanguages = 
                                    {Locale.ENGLISH, new Locale("nl")};

    private static Locale[] getAvailableLanguages(){
        return Arrays.copyOf(availableLanguages, availableLanguages.length);
        //TODO: don't hardcode these languages,
        //but get them from a configuration file!
    }

    public static interface SettingsPanelListener {
        void settingsSaved();
    }

}
