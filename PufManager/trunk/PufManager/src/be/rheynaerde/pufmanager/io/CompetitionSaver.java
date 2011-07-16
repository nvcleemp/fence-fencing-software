/* CompetitionSaver.java
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
import be.rheynaerde.pufmanager.data.Fencer;
import be.rheynaerde.pufmanager.data.Match;
import be.rheynaerde.pufmanager.data.Round;
import be.rheynaerde.pufmanager.data.Team;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Utility class that provides methods to save a competition to an XML file.
 * TODO: document this XML format.
 * @author nvcleemp
 */
public class CompetitionSaver {

    private CompetitionSaver() {
        //don't instantiate this class
    }

    public static void exportCompetition(Competition competition, File f) throws IOException{
        Document document = new Document(competitionToElement(competition));
        OutputStream out = new FileOutputStream(f);
        new XMLOutputter(Format.getPrettyFormat()).output(document, out);
    }

    private static Element competitionToElement(Competition competition){
        Element competitionElement = new Element("competition");

        //fencers
        Element fencersElement = new Element("fencers");
        Element teamsElement = new Element("teams");
        Map<Team, Integer> teamIds = new HashMap<Team, Integer>();
        for (int i=0; i<competition.getTeamCount(); i++) {
            teamIds.put(competition.getTeam(i), i);
            teamsElement.addContent(teamToElement(competition.getTeam(i), i));
        }
        fencersElement.addContent(teamsElement);
        Element unassignedFencersElement = new Element("unassigned");
        for (int i=0; i<competition.getNumberOfUnassignedFencers(); i++) {
            unassignedFencersElement.addContent(fencerToElement(competition.getUnassignedFencer(i)));
        }
        fencersElement.addContent(unassignedFencersElement);
        competitionElement.addContent(fencersElement);

        //rounds
        Element roundsElement = new Element("rounds");
        for (int i=0; i<competition.getRoundCount(); i++) {
            roundsElement.addContent(roundToElement(competition.getRound(i),teamIds));
        }
        competitionElement.addContent(roundsElement);
        
        //pool
        //TODO: also include results in saved competition

        return competitionElement;
    }

    private static Element teamToElement(Team team, int id){
        Element teamElement = new Element("team");
        teamElement.setAttribute("id", Integer.toString(id));
        teamElement.setAttribute("name", team.getTeamName());
        teamElement.setAttribute("size", Integer.toString(team.getTargetSize()));
        for (int i=0; i<team.getTeamSize();i++) {
            teamElement.addContent(fencerToElement(team.getFencer(i)));
        }
        return teamElement;
    }

    private static Element fencerToElement(Fencer fencer){
        Element fencerElement = new Element("fencer");
        if(fencer.getName()!=null)
            fencerElement.setAttribute("name", fencer.getName());
        if(fencer.getClub()!=null)
            fencerElement.setAttribute("club", fencer.getClub());
        return fencerElement;
    }

    private static Element roundToElement(Round round, Map<Team, Integer> teamIds){
        Element roundElement = new Element("round");
        roundElement.setAttribute("number", Integer.toString(round.getRoundNumber()));
        roundElement.setAttribute("internal", Boolean.toString(round.includeInternalBouts()));
        
        //matches
        Element matchesElement = new Element("matches");
        for (Match match : round.getMatches()) {
            Element matchElement = new Element("match");
            matchElement.addContent(new Element("team1")
                        .setAttribute("id", teamIds.get(match.getFirstTeam()).toString()));
            matchElement.addContent(new Element("team2")
                        .setAttribute("id", teamIds.get(match.getSecondTeam()).toString()));
            matchesElement.addContent(matchElement);
        }
        roundElement.addContent(matchesElement);
        
        //single teams
        Element singleTeamMatchesElement = new Element("singleteam");
        for (Team team : round.getRestingTeams()) {
            singleTeamMatchesElement.addContent(
                    new Element("team")
                        .setAttribute("id", teamIds.get(team).toString()));
        }
        roundElement.addContent(singleTeamMatchesElement);
        
        return roundElement;
    }
}
