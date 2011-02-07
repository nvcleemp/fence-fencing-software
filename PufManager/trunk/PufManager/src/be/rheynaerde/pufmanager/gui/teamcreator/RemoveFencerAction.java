/* RemoveFencerAction.java
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
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.util.UnassignedFencersListModel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class RemoveFencerAction extends AbstractAction {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.teamcreator");

    private Competition competition;
    private ListSelectionModel selectionModel;
    private UnassignedFencersListModel listModel;

    private ListSelectionListener selectionListener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent e) {
            setEnabled();
        }
    };

    public RemoveFencerAction(Competition competition, ListSelectionModel selectionModel, UnassignedFencersListModel listModel) {
        super(BUNDLE.getString("remove.fencers"));
        this.competition = competition;
        this.selectionModel = selectionModel;
        this.listModel = listModel;
        selectionModel.addListSelectionListener(selectionListener);
        setEnabled();
    }

    public void actionPerformed(ActionEvent e) {
        List<Fencer> selectedFencers = new ArrayList<Fencer>();
        for (int i = selectionModel.getMinSelectionIndex(); i <= selectionModel.getMaxSelectionIndex(); i++) {
            if(selectionModel.isSelectedIndex(i))
                selectedFencers.add(listModel.getElementAt(i));
        }
        for (Fencer fencer : selectedFencers) {
            competition.removeUnassignedFencer(fencer);
        }
    }

    private void setEnabled(){
        setEnabled(!selectionModel.isSelectionEmpty());
    }

}
