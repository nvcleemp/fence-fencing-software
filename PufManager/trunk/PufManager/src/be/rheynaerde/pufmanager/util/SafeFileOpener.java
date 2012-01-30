/* SafeFileOpener.java
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

package be.rheynaerde.pufmanager.util;

import java.awt.Component;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Class that provides a method to open a file for saving without the
 * risk of overwriting an existing file silently.
 * 
 * @author nvcleemp
 */
public class SafeFileOpener {
    
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.util.safefileopener");

    private JFileChooser chooser;

    public SafeFileOpener(JFileChooser chooser) {
        this.chooser = chooser;
    }
    
    public File getSaveFile(Component parent){
        if(chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            boolean userWantsToOverwrite = false;
            while(f!=null && f.exists() && !userWantsToOverwrite){
                int answer = JOptionPane
                        .showConfirmDialog(parent,
                                        BUNDLE.getString("overwrite.question"),
                                        BUNDLE.getString("overwrite.title"),
                                        JOptionPane.YES_NO_CANCEL_OPTION);
                if(answer == JOptionPane.YES_OPTION){
                    userWantsToOverwrite = true;
                } else if(answer == JOptionPane.CANCEL_OPTION){
                    f = null;
                } else if(chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION){
                    f = chooser.getSelectedFile();
                } else {
                    f = null;
                }
            }
            return f;
        } else {
            return null;
        }
    }
}
