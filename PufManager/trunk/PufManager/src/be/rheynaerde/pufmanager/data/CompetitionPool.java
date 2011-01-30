/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.rheynaerde.pufmanager.data;

import be.rheynaerde.pufmanager.data.listener.CompetitionAdapter;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;
import java.util.ArrayList;
import java.util.Collections;
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

    public CompetitionPool(Competition competition) {
        this.competition = competition;
        competition.addListener(competitionListener);
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
