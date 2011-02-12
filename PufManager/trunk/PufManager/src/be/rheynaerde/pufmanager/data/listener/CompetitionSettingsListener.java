/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.rheynaerde.pufmanager.data.listener;

import be.rheynaerde.pufmanager.data.CompetitionSettings;

/**
 *
 * @author nvcleemp
 */
public interface CompetitionSettingsListener {

    void settingChanged(CompetitionSettings.Setting setting);
}
