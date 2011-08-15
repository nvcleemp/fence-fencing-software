/* CompetitionLoader.java
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

package be.rheynaerde.pufmanager.io;

import be.rheynaerde.pufmanager.data.Competition;
import be.rheynaerde.pufmanager.data.CompetitionSettings;
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.PoolResult;
import be.rheynaerde.pufmanager.data.Team;
import be.rheynaerde.pufmanager.roundgenerator.FixedRoundGenerator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author nvcleemp
 */
public class CompetitionLoader {

    private CompetitionLoader() {
        //don't instantiate this class
    }

    public static Competition loadCompetition(File f) throws IOException {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(f);
        } catch (JDOMException ex) {
            Logger.getLogger(CompetitionLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Error while reading file", ex);
        }
        Element root = document.getRootElement();
        TemporaryCompetition competition = new TemporaryCompetition();
        
        //settings
        CompetitionSettings settings = getSettingsFromElement(root.getChild("settings"));
        
        //fencers
        Element fencersElement = root.getChild("fencers");
        Map<String, Fencer> fencersMap = new HashMap<String, Fencer>();
        if(fencersElement!=null){
            getTeamsFromElement(fencersElement.getChild("teams"), competition, fencersMap);
            Element unassignedFencers = fencersElement.getChild("unassigned");
            if(unassignedFencers!=null){
                for (Object object : unassignedFencers.getChildren("fencer")) {
                    competition.unassignedFencers.add(getFencerFromElement((Element)object, fencersMap));
                }
            }
        }
        
        //rounds
        Element roundsElement = root.getChild("rounds");
        if(roundsElement!=null){
            competition.frg = new FixedRoundGenerator(roundsElement.getChildren("round").size());
            for (Object object : roundsElement.getChildren("round")) {
                getRoundFromElement((Element)object, competition, competition.frg);
            }
        }
        
        //pool
        Element poolElement = root.getChild("pool");
        if(poolElement!=null){
            for (Object object : poolElement.getChildren("poolresult")) {
                getPoolResultFromElement((Element)object, competition, fencersMap);
            }
        }
        
        return Competition.constructCompetition(competition.frg,
                                                competition.unassignedFencers,
                                                competition.teams,
                                                competition.poolResults,
                                                settings);
    }
    
    private static CompetitionSettings getSettingsFromElement(Element settingsElement){
        CompetitionSettings settings = new CompetitionSettings();
        if(settingsElement!=null){
            settings.setTitle(getStringFromValueElement(settingsElement.getChild("title"), ""));
            settings.setSubtitle(getStringFromValueElement(settingsElement.getChild("subtitle"), ""));
            settings.setMaximumScore(getIntFromValueElement(settingsElement.getChild("max"), 5));
            settings.setLocale(getLocaleFromValueElement(settingsElement.getChild("locale"), Locale.getDefault()));
        }
        return settings;
    }
    
    private static String getStringFromValueElement(Element valueElement, String defaultValue){
        if(valueElement==null || valueElement.getAttributeValue("value")==null){
            return defaultValue;
        } else {
            return valueElement.getAttributeValue("value");
        }
    }
    
    private static int getIntFromValueElement(Element valueElement, int defaultValue){
        if(valueElement==null || valueElement.getAttributeValue("value")==null){
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(valueElement.getAttributeValue("value"));
            } catch (NumberFormatException numberFormatException) {
                Logger.getLogger(CompetitionLoader.class.getName()).log(Level.SEVERE, null, numberFormatException);
                return defaultValue;
            }
        }
    }
    
    private static Locale getLocaleFromValueElement(Element valueElement, Locale defaultValue){
        if(valueElement==null || valueElement.getAttributeValue("value")==null){
            return defaultValue;
        } else {
            String locale = valueElement.getAttributeValue("value");
            String[] parts = locale.split("_");
            String language = (parts.length > 0 ? parts[0] : null);
            String country = (parts.length > 1 ? parts[1] : "");
            String variant = (parts.length > 2 ? parts[2] : "");
            if(language==null){
                Logger.getLogger(CompetitionLoader.class.getName()).log(Level.SEVERE, "No language specified");
                return defaultValue;
            }
            return new Locale(language, country, variant);
        }
    }
    
    private static void getTeamsFromElement(Element teamsElement, TemporaryCompetition comp, Map<String, Fencer> fencersMap){
        if(teamsElement==null) return;
        
        for (Object object : teamsElement.getChildren("team")) {
            comp.teams.add(getTeamFromElement((Element)object, comp, fencersMap));
        }
    }
    
    private static Team getTeamFromElement(Element teamElement, TemporaryCompetition comp, Map<String, Fencer> fencersMap){
        if(teamElement==null){
            throw new IllegalArgumentException("Can't construct team from null");
        }
        String id = teamElement.getAttributeValue("id");
        int size = Integer.parseInt(teamElement.getAttributeValue("size"));
        String name = teamElement.getAttributeValue("name");
        if(id==null){
            throw new IllegalArgumentException("The id-field is required for a team");
        }
        
        List<Fencer> fencers = new ArrayList<Fencer>();
        for (Object object : teamElement.getChildren("fencer")) {
            fencers.add(getFencerFromElement((Element)object, fencersMap));
        }
        
        Team team = new Team(name, fencers);
        team.setTargetSize(size);
        comp.teamsMap.put(id, team);
        return team;
    }
    
    private static Fencer getFencerFromElement(Element fencerElement, Map<String, Fencer> fencersMap){
        if(fencerElement==null){
            throw new IllegalArgumentException("Can't construct fencer from null");
        }
        String id = fencerElement.getAttributeValue("id");
        String name = fencerElement.getAttributeValue("name");
        String club = fencerElement.getAttributeValue("club");
        if(id==null){
            throw new IllegalArgumentException("Fencer without id");
        } else if(name==null){
            throw new IllegalArgumentException("Fencer without identity");
        } else {
            final Fencer fencer = new Fencer(name, club, id);
            fencersMap.put(id, fencer);
            return fencer;
        }
    }
    
    private static void getRoundFromElement(Element roundElement, TemporaryCompetition comp, FixedRoundGenerator frg){
        if(roundElement==null){
            throw new IllegalArgumentException("Can't construct round from null");
        }
        int roundNumber = Integer.parseInt(roundElement.getAttributeValue("number"));
        boolean includeInternals = Boolean.parseBoolean(roundElement.getAttributeValue("internal"));
        frg.setIncludeInternalBouts(roundNumber, includeInternals);
        Element matchesElement = roundElement.getChild("matches");
        if(matchesElement!=null){
            for (Object object : matchesElement.getChildren("match")) {
                getMatchFromElement((Element)object, comp, frg, roundNumber);
            }
        }
        Element singleTeamsElement = roundElement.getChild("singleteam");
        if(singleTeamsElement!=null){
            for (Object object : matchesElement.getChildren("team")) {
                Element teamElement = (Element)object;
                String id = teamElement.getAttributeValue("id");
                //TODO: null checks
                frg.addRestingTeam(roundNumber, comp.teamsMap.get(id));
            }
        }
    }
    
    private static void getMatchFromElement(Element matchElement, TemporaryCompetition comp, FixedRoundGenerator frg, int roundNumber){
        if(matchElement==null){
            throw new IllegalArgumentException("Can't construct match from null");
        }
        Element team1Element = matchElement.getChild("team1");
        Element team2Element = matchElement.getChild("team2");
        if(team1Element==null || team2Element==null){
            throw new IllegalArgumentException("A match needs two teams");
        }
        String id1 = team1Element.getAttributeValue("id");
        String id2 = team2Element.getAttributeValue("id");
        if(id1==null || id2==null){
            throw new IllegalArgumentException("The id-attributes are mandatory");
        }
        Team team1 = comp.teamsMap.get(id1);
        Team team2 = comp.teamsMap.get(id2);
        if(team1==null || team2==null){
            throw new IllegalArgumentException("ID doesn't correspond to an existing team");
        }
        frg.addMatch(roundNumber, team1, team2);
    }
    
    private static void getPoolResultFromElement(Element poolResultElement, TemporaryCompetition competition, Map<String, Fencer> fencersMap){
        if(poolResultElement==null){
            throw new IllegalArgumentException("Can't construct pool result from null");
        }
        Fencer fencer1 = fencersMap.get(poolResultElement.getAttributeValue("fencer1"));
        Fencer fencer2 = fencersMap.get(poolResultElement.getAttributeValue("fencer2"));
        if(fencer1==null || fencer2==null){
            throw new IllegalArgumentException("Unknown fencer");
        }
        int score1 = Integer.parseInt(poolResultElement.getAttributeValue("score1"));
        int score2 = Integer.parseInt(poolResultElement.getAttributeValue("score2"));
        boolean victory1 = poolResultElement.getAttributeValue("fencer1").equals(poolResultElement.getAttributeValue("winner"));
        boolean victory2 = poolResultElement.getAttributeValue("fencer2").equals(poolResultElement.getAttributeValue("winner"));
        if(victory1==victory2){
            throw new IllegalArgumentException("Bout without clear winner");
        }
        competition.poolResults.add(new Competition.WrappedPoolResult(fencer1, fencer2, new PoolResult(victory1, score1)));
        competition.poolResults.add(new Competition.WrappedPoolResult(fencer2, fencer1, new PoolResult(victory2, score2)));
    }

    private static final class TemporaryCompetition {
        private List<Team> teams = new ArrayList<Team>();
        private Map<String, Team> teamsMap = new HashMap<String, Team>();
        private List<Fencer> unassignedFencers = new ArrayList<Fencer>();
        private FixedRoundGenerator frg;
        private List<Competition.WrappedPoolResult> poolResults = new ArrayList<Competition.WrappedPoolResult>();
    }
}
