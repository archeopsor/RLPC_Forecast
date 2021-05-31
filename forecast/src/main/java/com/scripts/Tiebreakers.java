package com.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Tiebreakers {
    public static String twoTeamsSameDivision(List<String> teams, List<List<Object>> schedule, String league) throws Exception {
        String team1 = teams.get(0);
        String team2 = teams.get(1);

        // Head to head series and games record
        List<String> headToHeadSeriesWinners = new ArrayList<String>(); // Should have size 2 at the end
        HashMap<String, Integer> headToHeadGamesLosses = new HashMap<String, Integer>(); // Should end with size 2
        for (List<Object> game : schedule) {
            if (game.get(2).toString().equals(team1) && game.get(4).toString().equals(team2)) {
                headToHeadSeriesWinners.add(game.get(5).toString());
                Integer losses = Integer.parseInt(game.get(6).toString().split(" - ")[1]);
                headToHeadGamesLosses.put(game.get(5).toString(), losses + 3);
            } else if (game.get(2).toString().equals(team2) && game.get(4).toString().equals(team1)) {
                headToHeadSeriesWinners.add(game.get(5).toString());
                Integer losses = Integer.parseInt(game.get(6).toString().split(" - ")[1]);
                headToHeadGamesLosses.put(game.get(5).toString(), losses + 3);
            }
        }

        // See if one team won both head-to-head series
        if (headToHeadSeriesWinners.get(0).equals(headToHeadSeriesWinners.get(1))) {
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
        conferenceWins.add(0);
        conferenceWins.add(0);
        List<Integer> divisionWins = new ArrayList<Integer>(); // Size 2
        divisionWins.add(0);
        divisionWins.add(0);
        List<String> divisionTeams = Utils.getDivisionTeams(team1, league);
        List<String> conferenceTeams = Utils.getConferenceTeams(team1, league);
        for (List<Object> game : schedule) {
            // Get team 1 wins
            if (game.get(2).toString().equals(team1) && conferenceTeams.contains(game.get(4).toString())
                    && game.get(5).toString().equals(team1)) {
                conferenceWins.set(0, conferenceWins.get(0) + 1);
                if (divisionTeams.contains(game.get(4).toString())) {
                    divisionWins.set(0, divisionWins.get(0) + 1);
                }
            } else if (game.get(4).toString().equals(team1) && conferenceTeams.contains(game.get(2).toString())
                    && game.get(5).toString().equals(team1)) {
                conferenceWins.set(0, conferenceWins.get(0) + 1);
                if (divisionTeams.contains(game.get(2).toString())) {
                    divisionWins.set(0, divisionWins.get(0) + 1);
                }
            }

            // Get team 2 wins
            if (game.get(2).toString().equals(team2) && conferenceTeams.contains(game.get(4).toString())
                    && game.get(5).toString().equals(team2)) {
                conferenceWins.set(1, conferenceWins.get(1) + 1);
                if (divisionTeams.contains(game.get(4).toString())) {
                    divisionWins.set(1, divisionWins.get(1) + 1);
                }
            } else if (game.get(4).toString().equals(team2) && conferenceTeams.contains(game.get(2).toString())
                    && game.get(5).toString().equals(team2)) {
                conferenceWins.set(1, conferenceWins.get(1) + 1);
                if (divisionTeams.contains(game.get(2).toString())) {
                    divisionWins.set(1, divisionWins.get(1) + 1);
                }
            }
        }

        // See if one team has a higher division record
        if (divisionWins.get(0) > divisionWins.get(1)) {
            return team1;
        } else if (divisionWins.get(1) > divisionWins.get(0)) {
            return team2;
        }

        // See if one team has a higher conference record
        if (conferenceWins.get(0) > conferenceWins.get(1)) {
            return team1;
        } else if (conferenceWins.get(1) > conferenceWins.get(0)) {
            return team2;
        }

        // Randomly pick one team
        Random rand = new Random();
        return teams.get(rand.nextInt(2));
    }

    public static String manyTeamsSameDivision(List<String> teams, List<List<Object>> schedule, String league) throws Exception {
        // Head to head series and games record
        HashMap<String, Integer> headToHeadSeriesWins = new HashMap<String, Integer>();
        HashMap<String, Integer> headToHeadGamesWins = new HashMap<String, Integer>();
        HashMap<String, Integer> headToHeadGamesLosses = new HashMap<String, Integer>();
        for (List<Object> game : schedule) {
            if (teams.contains(game.get(2).toString()) && teams.contains(game.get(4).toString())) {
                // Update maps with wins and losses
                Integer existingSeriesWins = headToHeadSeriesWins.getOrDefault(game.get(5).toString(), 0);
                headToHeadSeriesWins.put(game.get(5).toString(), existingSeriesWins + 1);
                Integer existingGameWins = headToHeadGamesWins.getOrDefault(game.get(5).toString(), 0);
                headToHeadGamesWins.put(game.get(5).toString(), existingGameWins + 3);
                String loser = (game.get(2).toString().equals(game.get(5).toString())) ? game.get(2).toString()
                        : game.get(4).toString();
                Integer lostGames = Integer.parseInt(game.get(6).toString().split(" - ")[1]);
                Integer existingGameLosses = headToHeadGamesLosses.getOrDefault(loser, 0);
                headToHeadGamesLosses.put(loser, existingGameLosses + lostGames);
            }
        }

        // See if one team won both head-to-head series
        Integer maxWins = Collections.max(headToHeadSeriesWins.values());
        List<String> toRemove = new ArrayList<String>();
        for (String team : teams) {
            if (!headToHeadSeriesWins.getOrDefault(team, 0).equals(maxWins)) {
                toRemove.add(team);
            }
        }
        teams.removeAll(toRemove);
        if (teams.size() == 1) {
            return teams.get(0);
        } else if (teams.size() == 2) {
            return twoTeamsSameDivision(teams, schedule, league);
        }

        // See if either team lost more head-to-head games
        HashMap<String, Double> winPercentages = new HashMap<String, Double>();
        for (String team : teams) {
            Integer teamWins = headToHeadGamesWins.getOrDefault(team, 0);
            Integer teamGames = teamWins + headToHeadGamesLosses.getOrDefault(team, 0);
            Double winPercentage = (double) (teamWins / teamGames);
            winPercentages.put(team, winPercentage);
        }
        Double bestWinPercentage = Collections.max(winPercentages.values());
        List<String> toRemove2 = new ArrayList<String>();
        for (String team : teams) {
            if (!winPercentages.get(team).equals(bestWinPercentage)) {
                toRemove2.add(team);
            }
        }
        teams.removeAll(toRemove2);
        if (teams.size() == 1) {
            return teams.get(0);
        } else if (teams.size() == 2) {
            return twoTeamsSameDivision(teams, schedule, league);
        }

        // Get conference and division record
        HashMap<String, Integer> conferenceWins = new HashMap<String, Integer>();
        HashMap<String, Integer> divisionWins = new HashMap<String, Integer>();
        List<String> divisionTeams = Utils.getDivisionTeams(teams.get(0), league);
        List<String> conferenceTeams = Utils.getConferenceTeams(teams.get(0), league);
        for (List<Object> game : schedule) {
            if (conferenceTeams.contains(game.get(2).toString()) && conferenceTeams.contains(game.get(4).toString())
                    && teams.contains(game.get(5).toString())) {
                Integer existingConferenceWins = conferenceWins.getOrDefault(game.get(5).toString(), 0);
                conferenceWins.put(game.get(5).toString(), existingConferenceWins + 1);
                if (divisionTeams.contains(game.get(2).toString()) && divisionTeams.contains(game.get(4).toString())) {
                    Integer existingDivisionWins = divisionWins.getOrDefault(game.get(5).toString(), 0);
                    divisionWins.put(game.get(5).toString(), existingDivisionWins + 1);
                }
            }
        }

        // See if one team has a higher division record
        Integer maxDivisionWins = Collections.max(divisionWins.values());
        List<String> toRemove3 = new ArrayList<String>();
        for (String team : teams) {
            if (!divisionWins.get(team).equals(maxDivisionWins)) {
                toRemove3.add(team);
            }
        }
        teams.removeAll(toRemove3);
        if (teams.size() == 1) {
            return teams.get(0);
        } else if (teams.size() == 2) {
            return twoTeamsSameDivision(teams, schedule, league);
        }

        // See if one team has a higher conference record
        Integer maxConferenceWins = Collections.max(conferenceWins.values());
        List<String> toRemove4 = new ArrayList<String>();
        for (String team : teams) {
            if (!conferenceWins.get(team).equals(maxConferenceWins)) {
                toRemove4.add(team);
            }
        }
        teams.removeAll(toRemove4);
        if (teams.size() == 1) {
            return teams.get(0);
        } else if (teams.size() == 2) {
            return twoTeamsSameDivision(teams, schedule, league);
        }

        // Randomly pick one team
        Random rand = new Random();
        return teams.get(rand.nextInt(teams.size()));
    }

    public static String twoTeamsDifferentDivisions(List<String> teams, List<List<Object>> schedule, String league) throws Exception {
        String team1 = teams.get(0);
        String team2 = teams.get(1);

        // Head to head series and games record
        List<String> headToHeadSeriesWinners = new ArrayList<String>(); // Should have size 2 at the end
        HashMap<String, Integer> headToHeadGamesLosses = new HashMap<String, Integer>(); // Should end with size 2
        for (List<Object> game : schedule) {
            if (game.get(2).toString().equals(team1) && game.get(4).toString().equals(team2)) {
                headToHeadSeriesWinners.add(game.get(5).toString());
                Integer losses = Integer.parseInt(game.get(6).toString().split(" - ")[1]);
                headToHeadGamesLosses.put(game.get(5).toString(), losses + 3);
            } else if (game.get(2).toString().equals(team2) && game.get(4).toString().equals(team1)) {
                headToHeadSeriesWinners.add(game.get(5).toString());
                Integer losses = Integer.parseInt(game.get(6).toString().split(" - ")[1]);
                headToHeadGamesLosses.put(game.get(5).toString(), losses + 3);
            }
        }

        // See if one team won both head-to-head series
        if (teams.contains(headToHeadSeriesWinners.get(0))) {
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
        conferenceWins.add(0);
        conferenceWins.add(0);
        List<String> conferenceTeams = Utils.getConferenceTeams(team1, league);
        for (List<Object> game : schedule) {
            // Get team 1 wins
            if (game.get(2).toString().equals(team1) && conferenceTeams.contains(game.get(4).toString())
                    && game.get(5).toString().equals(team1)) {
                conferenceWins.set(0, conferenceWins.get(0) + 1);
            } else if (game.get(4).toString().equals(team1) && conferenceTeams.contains(game.get(2).toString())
                    && game.get(5).toString().equals(team1)) {
                conferenceWins.set(0, conferenceWins.get(0) + 1);
            }

            // Get team 2 wins
            if (game.get(2).toString().equals(team2) && conferenceTeams.contains(game.get(4).toString())
                    && game.get(5).toString().equals(team2)) {
                conferenceWins.set(1, conferenceWins.get(1) + 1);
            } else if (game.get(4).toString().equals(team2) && conferenceTeams.contains(game.get(2).toString())
                    && game.get(5).toString().equals(team2)) {
                conferenceWins.set(1, conferenceWins.get(1) + 1);
            }
        }

        // See if one team has a higher conference record
        if (conferenceWins.get(0) > conferenceWins.get(1)) {
            return team1;
        } else if (conferenceWins.get(1) > conferenceWins.get(0)) {
            return team2;
        }

        // Randomly pick one team
        Random rand = new Random();
        return teams.get(rand.nextInt(2));
    }

    public static String manyTeamsDifferentDivisions(List<String> teams, List<List<Object>> schedule, String league) throws Exception {
        // Get conference record
        HashMap<String, Integer> conferenceWins = new HashMap<String, Integer>();
        List<String> conferenceTeams = Utils.getConferenceTeams(teams.get(0), league);
        for (List<Object> game : schedule) {
            if (conferenceTeams.contains(game.get(2).toString()) && conferenceTeams.contains(game.get(4).toString())
                    && teams.contains(game.get(5).toString())) {
                Integer existingConferenceWins = conferenceWins.getOrDefault(game.get(5).toString(), 0);
                conferenceWins.put(game.get(5).toString(), existingConferenceWins + 1);
            }
        }

        // See if one team has a higher conference record
        Integer maxConferenceWins = Collections.max(conferenceWins.values());
        List<String> toRemove = new ArrayList<String>();
        for (String team : teams) {
            if (!conferenceWins.getOrDefault(team, 0).equals(maxConferenceWins)) {
                toRemove.add(team);
            }
        }
        teams.removeAll(toRemove);
        if (teams.size() == 1) {
            return teams.get(0);
        } else if (teams.size() == 2 && Utils.inSameDivision(teams, league)) {
            return twoTeamsDifferentDivisions(teams, schedule, league);
        }

        // Randomly pick one team
        Random rand = new Random();
        return teams.get(rand.nextInt(teams.size()));
    }

    public static String divisionWinner(HashMap<String, Integer> wins, List<List<Object>> schedule, String league)
            throws Exception {
        List<String> winners = new ArrayList<String>();
        Integer maxWins = Collections.max(wins.values());
        for (String team : wins.keySet()) {
            if (wins.get(team).equals(maxWins)) {
                winners.add(team);
            }
        }

        if (winners.size() == 2) {
            return twoTeamsSameDivision(winners, schedule, league);
        } else if (winners.size() > 2) {
            return manyTeamsSameDivision(winners, schedule, league);
        } else if (winners.size() < 1) {
            throw new Exception("No division winner was found.");
        } else {
            return winners.get(0);
        }
    }

    public static List<String> conferencePlayoffs(HashMap<String, Integer> wins, List<List<Object>> schedule, String league)
            throws Exception {
        List<String> teamsInOrder = new ArrayList<String>();

        // Split the 8 teams into two divisions
        HashMap<String, Integer> division1 = new HashMap<String, Integer>();
        HashMap<String, Integer> division2 = new HashMap<String, Integer>();
        String firstTeam = "";
        for (String team : wins.keySet()) {
            if (firstTeam.isEmpty()) {
                division1.put(team, wins.get(team));
                firstTeam = team;
                continue;
            }
            List<String> arrayWithTeams = new ArrayList<String>();
            arrayWithTeams.add(firstTeam);
            arrayWithTeams.add(team);
            if (Utils.inSameDivision(arrayWithTeams, league)) {
                division1.put(team, wins.get(team));
            } else {
                division2.put(team, wins.get(team));
            }
        }

        // Get the two division winners, one of which should also be conference winner
        // This gets 1st and second seeds
        String winner1 = divisionWinner(division1, schedule, league);
        String winner2 = divisionWinner(division2, schedule, league);
        if (wins.get(winner1).equals(wins.get(winner2))) {
            String firstSeed = twoTeamsDifferentDivisions(Arrays.asList(new String[] {winner1, winner2}), schedule, league);
            String secondSeed = (firstSeed.equals(winner1)) ? winner2 : winner1;
            teamsInOrder.add(firstSeed);
            teamsInOrder.add(secondSeed);
        } else if (wins.get(winner1) > wins.get(winner2)) {
            teamsInOrder.add(winner1);
            teamsInOrder.add(winner2);
        } else {
            teamsInOrder.add(winner2);
            teamsInOrder.add(winner1);
        }

        // Remove division winners from map of teams
        wins.remove(winner1);
        wins.remove(winner2);

        // Get the top remaining teams
        int teamsAdded = 0;
        while(teamsAdded < 2) {
            List<String> topTeams  = new ArrayList<String>();
            Integer maxWins = Collections.max(wins.values());
            for (String team : wins.keySet()) {
                if (wins.get(team).equals(maxWins)) {
                    topTeams.add(team);
                }
            }
            if (topTeams.size() == 1) {
                teamsInOrder.add(topTeams.get(0));
                wins.remove(topTeams.get(0));
                teamsAdded++;
            } else if (Utils.inSameDivision(topTeams, league)){
                String winner = manyTeamsSameDivision(topTeams, schedule, league);
                teamsInOrder.add(winner);
                wins.remove(winner);
                teamsAdded++;
            } else {
                String winner = manyTeamsDifferentDivisions(topTeams, schedule, league);
                teamsInOrder.add(winner);
                wins.remove(winner);
                teamsAdded++;
            }
        }

        return teamsInOrder;
    }

    public static List<List<String>> getPlayoffs(HashMap<String, Integer> wins, List<List<Object>> schedule, String league) throws Exception {
        // Get rid of preseason games in schedule
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i).get(5).toString().equals("PRESEASON")) {
                schedule.remove(i);
                i--;
            }
        }

        List<List<String>> playoffTeams = new ArrayList<List<String>>();

        // Get two separate conferences
        HashMap<String, Integer> conference1 = new HashMap<String, Integer>();
        HashMap<String, Integer> conference2 = new HashMap<String, Integer>();
        String firstTeam = "";
        for (String team : wins.keySet()) {
            if (firstTeam.isEmpty()) {
                conference1.put(team, wins.get(team));
                firstTeam = team;
                continue;
            }
            List<String> arrayWithTeams = new ArrayList<String>();
            arrayWithTeams.add(firstTeam);
            arrayWithTeams.add(team);
            if (Utils.inSameConference(arrayWithTeams, league)) {
                conference1.put(team, wins.get(team));
            } else {
                conference2.put(team, wins.get(team));
            }
        }
        playoffTeams.add(conferencePlayoffs(conference1, schedule, league));
        playoffTeams.add(conferencePlayoffs(conference2, schedule, league));

        return playoffTeams;
    }
}
