/* BrowseComponent.java
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Component which simulates the behaviour of the browse-component in HTML.
 * 
 * @author nvcleemp
 */
public class BrowseComponent extends JPanel {
    
    //
    private final JTextField directoryTextField = new JTextField(10);
    
    //
    private final JFileChooser fileChooser;
    
    //
    private final Action browseAction;

    public BrowseComponent(File startFile) {
        this(startFile, "Browse", FileSelectionMode.FILES_AND_DIRECTORIES);
    }

    public BrowseComponent(File startFile, String text, FileSelectionMode mode) {
        directoryTextField.setEditable(false);
        if(startFile!=null){
            try {
                directoryTextField.setText(startFile.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(BrowseComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            directoryTextField.setText("");
        }
        fileChooser = new JFileChooser(startFile);
        fileChooser.setMultiSelectionEnabled(false);
        mode.configureBrowseComponent(this);
        
        browseAction = new AbstractAction(text) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (fileChooser.showOpenDialog(BrowseComponent.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            directoryTextField.setText(fileChooser.getSelectedFile().getCanonicalPath());
                            fireStateChanged();
                        } catch (IOException ex) {
                            Logger.getLogger(BrowseComponent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
        initGui();
    }

    public File getFile() {
        if(directoryTextField.getText().trim().length()==0){
            return null;
        } else {
            return new File(directoryTextField.getText());
        }
    }

    private void initGui() {
        setLayout(new BorderLayout());
        add(directoryTextField, BorderLayout.CENTER);
        add(new JButton(browseAction), BorderLayout.EAST);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        directoryTextField.setEnabled(enabled);
        browseAction.setEnabled(enabled);
    }
    
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    private ChangeEvent changeEvent = new ChangeEvent(this);
    
    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener){
        listeners.remove(listener);
    }
    
    private void fireStateChanged(){
        for (ChangeListener l : listeners) {
            l.stateChanged(changeEvent);
        }
    }
    
    public static enum FileSelectionMode{
        FILES_ONLY(JFileChooser.FILES_ONLY), DIRECTORIES_ONLY(JFileChooser.DIRECTORIES_ONLY), FILES_AND_DIRECTORIES(JFileChooser.FILES_AND_DIRECTORIES);

        private final int mode;
        
        private FileSelectionMode(int mode){
            this.mode = mode;
        }
        
        public void configureBrowseComponent(BrowseComponent browseComponent){
            browseComponent.fileChooser.setFileSelectionMode(mode);
        }
    }
    
}
