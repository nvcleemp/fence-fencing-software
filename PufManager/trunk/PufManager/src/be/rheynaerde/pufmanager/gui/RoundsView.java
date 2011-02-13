/* RoundsView.java
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

package be.rheynaerde.pufmanager.gui;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.listener.CompetitionAdapter;
import be.rheynaerde.pufmanager.data.listener.CompetitionListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 *
 * @author nvcleemp
 */
public class RoundsView extends JPanel implements Scrollable{

    private Competition competition;

    private CompetitionListener competitionListener = new CompetitionAdapter() {

        @Override
        public void roundsChanged() {
            buildView();
        }
    };

    public RoundsView(Competition competition) {
        super(new GridLayout(0, 1, 5, 5));
        this.competition = competition;
        competition.addListener(competitionListener);
        buildView();
    }

    private void buildView(){
        removeAll();
        for (int i = 0; i < competition.getRoundCount(); i++) {
            final RoundPanel roundPanel = new RoundPanel(competition.getRound(i));
            roundPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(roundPanel);
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if(visibleRect!=null
                && (orientation==SwingConstants.HORIZONTAL
                    || orientation==SwingConstants.VERTICAL)){
            int roundPanel = locationToRoundPanel(visibleRect.y);
            if(roundPanel==-1){
                return 0;
            } else {
                if (direction > 0) { // i.e. south
                    if(roundPanel==getComponentCount()-1){
                        //last round is partially visible: jump to end of this
                        //round
                        Rectangle r = getComponent(roundPanel).getBounds();
                        return (r == null) ? 0 : (r.y+r.height - visibleRect.y);
                    } else {
                        //otherwise jump to start of next round
                        Rectangle r = getComponent(roundPanel+1).getBounds();
                        return (r == null) ? 0 : (r.y - visibleRect.y);
                    }
                } else { // i.e. north
                    //rectangle of round closest to the top of the viewport
                    Rectangle r = getComponent(roundPanel).getBounds();

                    if ((r.y == visibleRect.y) && (roundPanel == 0))  {
                        //the first round is completely visible, so we can't
                        //scroll north
                        return 0;
                    } else if (r.y == visibleRect.y) {
                        //the top round in the viewport is completely
                        //visible, so jump to the round before that one
                        Rectangle prevR = getComponent(roundPanel-1).getBounds();

                        if (prevR == null || prevR.y >= r.y) {
                            return 0;
                        } else {
                            return visibleRect.y - prevR.y;
                        }
                    } else {
                        //the top round is partially visible: scroll to make
                        //it completely visible
                        return visibleRect.y - r.y;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if(visibleRect!=null
                && (orientation==SwingConstants.HORIZONTAL
                    || orientation==SwingConstants.VERTICAL)){
            //the round near the top of the viewport
            int topRoundPanel = locationToRoundPanel(visibleRect.y);

            //the round near the bottom of the viewport
            int bottomRoundPanel = locationToRoundPanel(visibleRect.y + visibleRect.height);

            if(topRoundPanel==-1 || bottomRoundPanel==-1){
                return 0;
            } else {
                if (direction > 0) { // i.e. south
                    //bottom round panel is placed at the top of the viewport
                    //except when it is already completely visible, then the
                    //next round is taken
                    Rectangle r = getComponent(bottomRoundPanel).getBounds();
                    if(r==null){
                        return 0;
                    } else if(bottomRoundPanel == getComponentCount()-1){
                        //just jump to end of this round
                        return (r.y + r.height - visibleRect.height) - visibleRect.y;
                    } else if(visibleRect.y + visibleRect.height >= r.y + r.height
                                || visibleRect.height < r.height) {
                        //jump to the next round
                        Rectangle nextR = getComponent(bottomRoundPanel+1).getBounds();
                        if(nextR==null){
                            return 0;
                        } else {
                            return nextR.y - visibleRect.y;
                        }
                    } else {
                        //this is now guaranteed to be positive
                        return r.y - visibleRect.y;
                    }
                } else { // i.e. north
                    //TODO
                    //if the top round panel is completely visible: it is placed
                    //immediately after the viewport, otherwise it becomes the
                    //bottom round panel

                    //rectangle of round closest to the top of the viewport
                    Rectangle r = getComponent(topRoundPanel).getBounds();

                    if ((r.y == visibleRect.y) && (topRoundPanel == 0))  {
                        //the first round is completely visible, so we can't
                        //scroll north
                        return 0;
                    } else if (r.y == visibleRect.y) {
                        //the top round in the viewport is completely
                        //visible, so we place it immediately after the viewport

                        if(visibleRect.y < visibleRect.height)
                            return visibleRect.y;
                        else
                            return visibleRect.height;

                    } else {
                        //the top round is partially visible: scroll to make
                        //it the bottom round
                        if(visibleRect.height < r.height){
                            return visibleRect.y - r.y;
                        } else {
                            //this is now guaranteed to be positive
                            return (visibleRect.y +visibleRect.height - r.height) - r.y;
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int locationToRoundPanel(int y){
        if(getComponentCount()==0)
            return -1;
        else {
            int roundPanel = 0;
            int componentCount = getComponentCount();
            while(roundPanel<componentCount && getComponent(roundPanel).getY() <= y){
                roundPanel++;
            }
            return roundPanel-1;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
