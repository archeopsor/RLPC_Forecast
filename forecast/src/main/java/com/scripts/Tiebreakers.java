package com.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Tiebreakers {
    public static String twoTeamsSameDivision(List<String> teams, HashMap<String, Integer> wins, List<List<Object>> schedule, String league) throws Exception {
        String team1 = teams.get(0);
        String team2 = teams.get(1);

        // Head to head series and games record
        List<String> headToHeadSeriesWinners = new ArrayList<String>(); // Should have size 2 at the end
        HashMap<String, Integer> headToHeadGamesLosses = new HashMap<String, Integer>(); // Should have size 2 at the end
        for (List<Object> game : schedule) {
            if (game.get(2).toString() == team1 && game.get(4).toString() == team2) {
                headToHeadSeriesWinners.add(game.get(5).toString());
                Integer losses = Integer.parseInt(game.get(6).toString().split(" - ")[1]); 
                headToHeadGamesLosses.put(game.get(5).toString(), losses + 3);
            } else if (game.get(2).toString() == team2 && game.get(4).toString() == team1) {
                headToHeadSeriesWinners.add(game.get(5).toString());
                Integer losses = Integer.parseInt(game.get(6).toString().split(" - ")[1]); 
                headToHeadGamesLosses.put(game.get(5).toString(), losses + 3);
            }
        }

        // See if one team won both head-to-head series
        if (headToHeadSeriesWinners.get(0) == headToHeadSeriesWinners.get(1)) {
            return headToHeadSeriesWinners.get(0);
        }

        // See if either team lost more head-to-head games
        if (headToHeadGamesLosses.get(team2) > headToHeadGamesLosses.get(team1)) {
            return team1;
        } else if (headToHeadGamesLosses.get(team1) > headToHeadGamesLosses.get(team2)) {
            return team2;
        }

        // Get conference and division record
        List<Integer> conferenceWins = new ArrayList<Integer>(); // Size 2
        List<Integer> divisionWins = new ArrayList<Integer>(); // Size 2
        List<String> divisionTeams = Utils.getDivisionTeams(team1, league);
        List<String> conferenceTeams = Utils.getConferenceTeams(team1, league);
        for (List<Object> game : schedule) {
            // Get team 1 wins
            if (game.get(2).toString() == team1 && conferenceTeams.contains(game.get(4).toString()) && game.get(5).toString() == team1) {
                conferenceWins.set(0, conferenceWins.get(0) + 1);
                if (divisionTeams.contains(game.get(4).toString())) {
                    divisionWins.set(0, divisionWins.get(0) + 1);
                }
            } else if (game.get(4).toString() == team1 && conferenceTeams.contains(game.get(2).toString()) && game.get(5).toString() == team1) {
                conferenceWins.set(0, conferenceWins.get(0) + 1);
                if (divisionTeams.contains(game.get(2).toString())) {
                    divisionWins.set(0, divisionWins.get(0) + 1);
                }
            }

            // Get team 2 wins
            if (game.get(2).toString() == team2 && conferenceTeams.contains(game.get(4).toString()) && game.get(5).toString() == team2) {
                conferenceWins.set(1, conferenceWins.get(1) + 1);
                if (divisionTeams.contains(game.get(4).toString())) {
                    divisionWins.set(1, divisionWins.get(1) + 1);
                }
            } else if (game.get(4).toString() == team2 && conferenceTeams.contains(game.get(2).toString()) && game.get(5).toString() == team2) {
                conferenceWins.set(1, conferenceWins.get(1) + 1);
                if (divisionTeams.contains(game.get(2).toString())) {
                    divisionWins.set(1, divisionWins.get(1) + 1);
                }
            }
        }

        // See if one team has a higher division record
        

        // See if one team has a higher conference record


        // Randomly pick one team
        Random rand = new Random();
        return teams.get(rand.nextInt(2));
    }

    // public static String twoTeamsDifferentDivisions(List<String> teams, HashMap<String, Integer> wins, List<List<Object>> schedule, String league) {

    // }

    // public static String manyTeamsSameDivision(List<String> teams, HashMap<String, Integer> wins, List<List<Object>> schedule, String league) {

    // }

    // public static String manyTeamsDifferentDivisions(List<String> teams, HashMap<String, Integer> wins, List<List<Object>> schedule, String league) {

    // }

    public static String divisionWinner(HashMap<String, Integer> wins, List<List<Object>> schedule, String league) throws Exception {
        List<String> winners = new ArrayList<String>();
        Integer maxWins = Collections.max(wins.values());
        for (String team : wins.keySet()) {
            if (wins.get(team) == maxWins) {
                winners.add(team);
            }
        }

        if (winners.size() > 1) {
            return twoTeamsSameDivision(winners, wins, schedule, league);
        } else if (winners.size() < 1) {
            throw new NullPointerException("No division winner was found.");
        } else {
            return winners.get(0);
        }
    }
    
    public static void conferencePlayoffs() {
        
    }

    public static void getPlayoffs(HashMap<String, Integer> wins, List<List<Object>> schedule, String league) {
        // Get rid of preseason games in schedule
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i).get(5).toString().length() > 0) {
                schedule.remove(i);
                i--;
            }
        }
    }
}
