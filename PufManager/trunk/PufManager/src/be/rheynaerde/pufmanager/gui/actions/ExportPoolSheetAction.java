/* ExportPoolSheetAction.java
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

package be.rheynaerde.pufmanager.gui.actions;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.gui.dialogs.ExportPoolDialog;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 * Action to export a pool sheet through an {@link ExportPoolDialog}.
 * 
 * @author nvcleemp
 */
public class ExportPoolSheetAction extends AbstractAction{

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.actions");

    private Competition competition;
    private ExportPoolDialog exportDialog;

    public ExportPoolSheetAction(Competition competition, JFrame parent) {
        super(BUNDLE.getString("export.poolsheet"));
        this.competition = competition;
        exportDialog = new ExportPoolDialog(parent);
    }

    public void actionPerformed(ActionEvent e) {
        exportDialog.exportPool(competition.getCompetitionPool(),
                                competition.getSettings().getLocale());
    }
    
}
