/* PufManagerPreferences.java
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

package be.rheynaerde.pufmanager.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Class to handle PufManager preferences management. When the preferences are 
 * not set by the user, a default value is taken.
 */
public class PufManagerPreferences {
    
    //This class is based upon GrinvinPreferences from the GrInvIn-project
    
    //
    public enum Preference {
        CURRENT_DIRECTORY("gui.currentdir", System.getProperty("user.dir")),
        EXPORT_DIRECTORY("gui.exportdir", System.getProperty("user.dir"));
        
        private String id;
        
        private String defaultValue;
        
        Preference(String id, String defaultValue) {
            this.id = id;
            this.defaultValue = defaultValue;
        }
        
        protected String getId() {
            return id;
        }
        
        protected String getDefaultValue() {
            return defaultValue;
        }
    }
    
    //
    private static final String OUR_NODE_NAME = 
            "/be/rheynaerde/pufmanager/preferences";
    
    //
    private final Preferences userPreferences = 
            Preferences.userRoot().node(OUR_NODE_NAME);
            
    //
    private List<PufManagerPreferencesListener> listeners;
        
    //
    private final static PufManagerPreferences INSTANCE = 
            new PufManagerPreferences();
    
    //
    private PufManagerPreferences() {
        listeners = new ArrayList<PufManagerPreferencesListener>();
    }
    
    public static PufManagerPreferences getInstance() {
        return INSTANCE;
    }
    
    //
    public int getIntPreference(Preference key) {
        throw new RuntimeException("No default value available for: " + key);
    }
    
    //
    public void setIntPreference(Preference key, int value) {
        userPreferences.putInt(key.getId(), value);
        firePreferenceChanged(key);
    }
    
    //
    public String getStringPreference(Preference key) {
        return userPreferences.get(key.getId(), key.getDefaultValue());
    }
    
    //
    public void setStringPreference(Preference key, String value) {
        userPreferences.put(key.getId(), value);
        firePreferenceChanged(key);
    }
    
    //
    public void addListener(PufManagerPreferencesListener listener) {
        listeners.add(listener);
    }
    
    //
    private void firePreferenceChanged(Preference preference) {
        for(PufManagerPreferencesListener listener : listeners)
            listener.preferenceChanged(preference);
    }
    
}
