/* PoolResultTableCellEditor.java
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

package be.rheynaerde.pufmanager.gui.pool;

import be.rheynaerde.pufmanager.data.PoolResult;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * 
 */
public class PoolResultTableCellEditor extends AbstractCellEditor
        implements TableCellEditor {

    protected JTextField editorTextField;

    /**
     * 
     */
    protected ActionListener textFieldListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
    };

    /**
     * An integer specifying the number of clicks needed to start editing.
     * Even if <code>clickCountToStart</code> is defined as zero, it
     * will not initiate until a click occurs.
     */
    protected int clickCountToStart = 2;

    protected int maxScore;

    public PoolResultTableCellEditor(int maxScore) {
        this.maxScore = maxScore;
        editorTextField = new JTextField();
        editorTextField.addActionListener(textFieldListener);
    }

    /**
     * Returns a reference to the editor component.
     *
     * @return the editor <code>Component</code>
     */
    public Component getComponent() {
        return editorTextField;
    }

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count  an int specifying the number of clicks needed to start editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    }

    public Object getCellEditorValue() {
        try {
            return parsePoolResult(editorTextField.getText());
        } catch (ParseException ex) {
            Logger.getLogger(PoolResultTableCellEditor.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void setValue(Object value) {
        editorTextField.setText((value != null) ? value.toString() : "");
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        try{
            parsePoolResult(editorTextField.getText());
            fireEditingStopped();
            return true;
        } catch (ParseException parseException){
            return false;
        }
    }

    /** Implements the <code>TableCellEditor</code> interface. */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected,
            int row, int column) {
        setValue(value);
        editorTextField.selectAll();
        return editorTextField;
    }


    public PoolResult parsePoolResult(String s) throws ParseException {
        String text = s.trim();
        if(text.equals(""))
            return null;
        if(text.equalsIgnoreCase("V"))
            return new PoolResult(true, maxScore);
        
        boolean victory = (text.toUpperCase().charAt(0)=='V');
        char firstChar = text.toUpperCase().charAt(0);
        if(Character.isDigit(firstChar)){
            victory = false;
        } else if (firstChar=='D') {
            victory = false;
            text = text.substring(1).trim();
        } else if (firstChar=='V') {
            victory = true;
            text = text.substring(1).trim();
        } else {
            throw new ParseException();
        }

        int score;
        try {
            score = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new ParseException();
        }
        if(score>maxScore){
            throw new ParseException();
        } else if (score < 0){
            throw new ParseException();
        }
        return new PoolResult(victory, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaximumScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public static class ParseException extends Exception {

    }
}

