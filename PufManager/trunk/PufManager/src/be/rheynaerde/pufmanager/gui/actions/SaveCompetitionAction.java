/* SaveCompetitionAction.java
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
import be.rheynaerde.pufmanager.io.CompetitionSaver;

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
 * Action that lets the user specify a file and stores a competition object as
 * an XML file in this file.
 * 
 * @author nvcleemp
 */
public class SaveCompetitionAction extends AbstractAction {

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.actions");

    private JFrame parent;
    private Competition competition;
    private boolean forceNewFile;
    private JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));

    public SaveCompetitionAction(JFrame parent, Competition competition) {
        this(parent, competition, false);
    }
    
    public SaveCompetitionAction(JFrame parent, Competition competition, boolean forceNewFile) {
        super(forceNewFile ? BUNDLE.getString("save.as") : BUNDLE.getString("save"));
        this.parent = parent;
        this.competition = competition;
        this.forceNewFile = forceNewFile;
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Competition file", "xml"));
    }

    public void actionPerformed(ActionEvent e) {
        File f = null;
        if(!forceNewFile){
            f = competition.getSaveFile();
        }
        if(f == null){
            f = askUserForFile();
        }
        if(f == null){
            //user canceled save dialog
            return;
        }
        try {
            CompetitionSaver.exportCompetition(competition, f);
            //if save was succesful we store the file
            competition.setSaveFile(f);
        } catch (IOException ex) {
            Logger.getLogger(SaveCompetitionAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private File askUserForFile(){
        if(chooser.showSaveDialog(parent)==JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            if(!f.exists() && !f.getName().endsWith(".xml")){
                File tempFile = new File(f.getParentFile(), f.getName() + ".xml");
                if(!tempFile.exists()){
                    f = tempFile;
                }
            }
            //TODO: show warning if f exists
            return f;
        }
        return null;
    }
    
}
