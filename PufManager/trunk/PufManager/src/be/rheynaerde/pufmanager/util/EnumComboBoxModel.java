/* EnumComboBoxModel.java
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

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * An implementation of ComboBoxModel which takes its values from an enum.
 * 
 * @author nvcleemp
 */
public class EnumComboBoxModel<E extends Enum<E>>  extends AbstractListModel implements ComboBoxModel {

    private Class<E> enumType;
    private E selectedItem = null;
    
    public EnumComboBoxModel(Class<E> enumType){
        this.enumType = enumType;
    }

    public void setSelectedItem(Object anItem) {
        if(anItem == null || enumType.isAssignableFrom(anItem.getClass())){
            selectedItem = (E)anItem;
        }
    }

    public E getSelectedItem() {
        return selectedItem;
    }

    public int getSize() {
        return enumType.getEnumConstants().length;
    }

    public E getElementAt(int index) {
        return enumType.getEnumConstants()[index];
    }
}
