/* CompetitionPool.java
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

package be.rheynaerde.pufmanager.data;

import be.rheynaerde.pufmanager.data.CompetitionSettings.Setting;
import be.rheynaerde.pufmanager.data.listener.CompetitionAdapter;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;
import be.rheynaerde.pufmanager.data.listener.CompetitionSettingsListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class CompetitionPool extends DefaultPool{

    private final Competition competition;

    private final CompetitionListener competitionListener = new CompetitionAdapter() {

        @Override
        public void fencersChanged() {
            updateFencers();
        }
    };

    private final CompetitionSettingsListener competitionSettingsListener =
            new CompetitionSettingsListener() {

        public void settingChanged
                (CompetitionSettings competitionSettings,
                    Setting setting) {
            if(Setting.MAXIMUM_SCORE.equals(setting)){
                setMaximumScore(competitionSettings.getMaximumScore());
            }
        }
    };

    public CompetitionPool(Competition competition) {
        this.competition = competition;
        competition.addListener(competitionListener);
        competition.getSettings().addListener(competitionSettingsListener);
        updateFencers();
    }

    private void updateFencers(){
        List<Fencer> newList = competition.getFencers();
        for (Fencer fencer : newList) {
            addFencer(fencer, true);
        }
        List<Fencer> oldList = new ArrayList<Fencer>(fencers);
        oldList.removeAll(newList);
        for (Fencer fencer : oldList) {
            removeFencer(fencer, true);
        }
        fireFencersChanged();
    }

    public void rearrangeFencers(){
        List<Fencer> newList = competition.getFencers();
        for (Fencer fencer : newList) {
            addFencer(fencer, true);
        }
        List<Fencer> oldList = new ArrayList<Fencer>(fencers);
        oldList.removeAll(newList);
        for (Fencer fencer : oldList) {
            removeFencer(fencer, true);
        }
        fencers = competition.getFencers();
        fireFencersChanged();
    }

    @Override
    public void addFencer(Fencer fencer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeFencer(Fencer fencer) {
        throw new UnsupportedOperationException();
    }

}
