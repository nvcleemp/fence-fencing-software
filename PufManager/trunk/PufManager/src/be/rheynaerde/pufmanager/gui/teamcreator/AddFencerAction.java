/* AddFencerAction.java
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
import be.rheynaerde.pufmanager.gui.dialogs.FencerDialog;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;

/**
 *
 * @author nvcleemp
 */
public class AddFencerAction extends AbstractAction {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.teamcreator");

    private Competition competition;

    public AddFencerAction(Competition competition) {
        super(BUNDLE.getString("add.new.fencer"));
        this.competition = competition;
    }

    public void actionPerformed(ActionEvent e) {
        FencerDialog dialog = new FencerDialog();
        if(dialog.showDialog())
            competition.addUnassignedFencer(dialog.getNewFencer());
    }

}
