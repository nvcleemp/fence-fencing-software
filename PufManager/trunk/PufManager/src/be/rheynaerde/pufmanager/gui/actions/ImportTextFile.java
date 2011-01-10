/* ImportTextFile.java
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
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.gui.teamcreator.TeamCreator;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class ImportTextFile extends AbstractAction {
    
    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.actions");

    private JFrame parent;
    private Competition competition;
    private JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));

    public ImportTextFile(JFrame parent, Competition competition) {
        super(BUNDLE.getString("import.text.file"));
        this.parent = parent;
        this.competition = competition;
    }

    public void actionPerformed(ActionEvent e) {
        if(chooser.showOpenDialog(parent)==JFileChooser.APPROVE_OPTION){
            try {
                Scanner scanner = new Scanner(chooser.getSelectedFile());
                while(scanner.hasNextLine()){
                    String nextLine = scanner.nextLine().trim();
                    if(!nextLine.equals(""))
                        competition.addUnassignedFencer(new Fencer(nextLine));
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TeamCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
