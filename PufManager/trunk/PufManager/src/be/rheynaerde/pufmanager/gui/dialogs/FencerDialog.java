/* FencerDialog.java
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

import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.util.SelectOnFocus;
import java.awt.Dimension;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author nvcleemp
 */
public class FencerDialog extends JDialog {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.dialogs");

    private static final String OK_BUTTON = "OK";
    private static final String CANCEL_BUTTON = "CANCEL";

    private JTextField name = new JTextField();
    private JTextField club = new JTextField();
    private JTextField id = new JTextField();

    private String lastPressed = OK_BUTTON;

    public FencerDialog() {
        this(null);
    }
    public FencerDialog(Frame owner) {
        super(owner, BUNDLE.getString("fencerdialog.title"), true);
        initGui();
    }

    private void initGui(){
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));

        buttonsPanel.add(Box.createHorizontalGlue());

        JButton okButton = new JButton(BUNDLE.getString("fencerdialog.ok"));
        okButton.addActionListener(new FencerDialogButtonActionListener(OK_BUTTON));
        buttonsPanel.add(okButton);

        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton cancelButton = new JButton(BUNDLE.getString("fencerdialog.cancel"));
        cancelButton.addActionListener(new FencerDialogButtonActionListener(CANCEL_BUTTON));
        buttonsPanel.add(cancelButton);

        

        JLabel nameLabel = new JLabel(BUNDLE.getString("fencerdialog.name") + ":");
        nameLabel.setLabelFor(name);
        JLabel clubLabel = new JLabel(BUNDLE.getString("fencerdialog.club") + ":");
        clubLabel.setLabelFor(club);
        JLabel idLabel = new JLabel(BUNDLE.getString("fencerdialog.id") + ":");
        idLabel.setLabelFor(id);

        name.setColumns(20);
        club.setColumns(20);
        id.setColumns(20);
        SelectOnFocus.addTextComponent(name);
        SelectOnFocus.addTextComponent(club);
        SelectOnFocus.addTextComponent(id);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 0, 2);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(nameLabel, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER; //take whatever remains in row
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(name, gbc);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(clubLabel, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(club, gbc);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(idLabel, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(id, gbc);

        add(buttonsPanel, gbc);
        
        pack();
    }

    /**
     * Creates a new Fencer using the data entered in the form.
     * @return
     */
    public Fencer getNewFencer(){
        String fencerName = name.getText().trim();
        String fencerClub = null;
        String fencerId = null;
        if(!club.getText().trim().equals("")){
            fencerClub = club.getText().trim();
        }
        if(!id.getText().trim().equals("")){
            fencerId = id.getText().trim();
        }
        return new Fencer(fencerName, fencerClub, fencerId);
    }

    public void setData(Fencer fencer){
        name.setText(fencer.getName());
        club.setText(fencer.getClub()==null ? "" : fencer.getClub());
        id.setText(fencer.getId()==null ? "" : fencer.getId());
    }

    public Fencer getData(Fencer fencer){
        fencer.setData(
                name.getText(), 
                club.getText().trim().equals("")? null : club.getText());
        fencer.setId(id.getText().trim().equals("")? null : id.getText());
        return fencer;
    }

    public boolean showDialog(){
        setVisible(true);
        return lastPressed.equals(OK_BUTTON);
    }

    private class FencerDialogButtonActionListener implements ActionListener {

        private String button;

        public FencerDialogButtonActionListener(String button) {
            this.button = button;
        }

        public void actionPerformed(ActionEvent e) {
            lastPressed = button;
            setVisible(false);
        }
    }
}
