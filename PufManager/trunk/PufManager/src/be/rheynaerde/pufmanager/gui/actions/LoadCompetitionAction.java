/* LoadCompetitionAction.java
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
import be.rheynaerde.pufmanager.gui.PufManagerFrame;
import be.rheynaerde.pufmanager.io.CompetitionLoader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Action that lets the user specify a file and loads a competition object from
 * this file.
 * 
 * @author nvcleemp
 */
public class LoadCompetitionAction extends AbstractAction {

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.actions");

    private PufManagerFrame parent;
    private JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));

    public LoadCompetitionAction(PufManagerFrame parent) {
        super(BUNDLE.getString("load"));
        this.parent = parent;
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Competition file", "xml"));
    }

    public void actionPerformed(ActionEvent e) {
        if(chooser.showOpenDialog(parent)==JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            if(!f.exists() && !f.getName().endsWith(".xml")){
                File tempFile = new File(f.getParentFile(), f.getName() + ".xml");
                if(tempFile.exists()){
                    f = tempFile;
                }
            }
            try {
                Competition competition = CompetitionLoader.loadCompetition(f);
                //store the file from which this competition was loaded
                competition.setSaveFile(f);
                parent.setCompetition(competition);
            } catch (IOException ex) {
                Logger.getLogger(LoadCompetitionAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
