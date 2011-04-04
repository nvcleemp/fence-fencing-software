/* SettingsDialog.java
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

import be.rheynaerde.pufmanager.data.CompetitionSettings;
import be.rheynaerde.pufmanager.gui.settings.SettingsPanel;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public final class SettingsDialog {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.dialogs");

    private SettingsDialog() {
    }

    public static void showSettingsDialog(JFrame parent, CompetitionSettings settings) {
        final JDialog dialog = new JDialog(parent, BUNDLE.getString("settingsdialog.title"), true);
        SettingsPanel settingsPanel = new SettingsPanel(settings);
        settingsPanel.addSettingsPanelListener(new SettingsPanel.SettingsPanelListener() {
            public void settingsSaved() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        dialog.setContentPane(settingsPanel);
        dialog.pack();
        dialog.setVisible(true);
    }
}
