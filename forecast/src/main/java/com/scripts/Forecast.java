package com.scripts;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Forecast {

    private static final String p4SheetId = "1gHlqD-xekmpwFDpblAiJfpuCvxRliK6C-AjYoEUU8tc"; // "1AJoBYkYGMIrpe8HkkJcB25DbLP2Z-eV7P6Tk9R6265I";
    private static final String indySheetId = "1bWvgo_YluMbpQPheldQQZdASKGHRPIUVfYL2r2KSdaE";
    private static final String powerRankingsSheetId = "1Tlc_TgGMrY5aClFF-Pb5xvtKrJ1Hn2PJOLy2fUDDdFI";
    private static final String forecastSheetId = "1GEFufHK5xt0WqThYC7xaK2gz3cwjinO43KOsb7HogQQ";

    private static HashMap<String, Integer> getWins(String league) throws IOException, GeneralSecurityException {
        final String teamWinsRange = Utils.teamWinRanges(league);
        List<List<Object>> wins = SheetsHandler.getValues(powerRankingsSheetId, teamWinsRange);
        HashMap<String, Integer> winsMap = new HashMap<String, Integer>();

        for (List<Object> team : wins) {
            winsMap.put(team.get(0).toString(), Integer.parseInt(team.get(1).toString()));
        }

        return winsMap;
    }

    private static List<List<Object>> getSchedule(String league) throws IOException, GeneralSecurityException {
        final String range = league + " Schedule!N4:V";
        List<String> p4Leagues = Arrays.asList(new String[] { "major", "aaa", "aa", "a" });
        List<String> indyLeagues = Arrays.asList(new String[] { "independent", "maverick", "renegade", "paladin" });
        List<List<Object>> schedule;
        if (p4Leagues.contains(league.toLowerCase())) {
            schedule = SheetsHandler.getValues(p4SheetId, range);
        } else if (indyLeagues.contains(league.toLowerCase())) {
            schedule = SheetsHandler.getValues(indySheetId, range);
        } else {
            throw new IOException(league + " isn't a valid league.");
        }

        return schedule;
    }

    private static List<Object> playGame(String team1, String team2, Integer rating1, Integer rating2, boolean playoffs)
            throws IOException {
        Integer Q1 = (int) Math.pow(10.0, ((double) rating1 / 250.0));
        Integer Q2 = (int) Math.pow(10.0, ((double) rating2 / 250.0));
        float team1WinProbability = (float) Q1 / (float) (Q1 + Q2);
        float team2WinProbability = (float) Q2 / (float) (Q1 + Q2);
        int numGames = (playoffs) ? 4 : 3;

        List<Object> teams = new ArrayList<Object>();
        teams.add(team1);
        teams.add(team2);

        List<Float> probabilities = new ArrayList<Float>();
        probabilities.add(team1WinProbability);
        probabilities.add(team2WinProbability);

        // Randomly pick winner based on the probabilities, and pick a random score
        String winner = Utils.getRandomElement(teams, probabilities).toString();
        String loser = (winner == team1) ? team2 : team1;
        int team1Score = (winner == team1) ? numGames : Utils.randBetween(0, numGames - 1);
        int team2Score = (winner == team2) ? numGames : Utils.randBetween(0, numGames - 1);

        // Generate expected score and actual score to update ratings
        float E1 = (float) Q1 / (float) (Q1 + Q2); // Expected Score
        float E2 = (float) Q2 / (float) (Q1 + Q2);
        float S1 = (float) team1Score / (float) (team1Score + team2Score); // Actual score
        float S2 = (float) team2Score / (float) (team1Score + team2Score);
        String score = "3 - " + Math.min(team1Score, team2Score);

        List<Object> result = new ArrayList<Object>();
        result.add(winner);
        result.add(loser);
        result.add(S1 - E1);
        result.add(S2 - E2);
        result.add(score);
        return result;
    }

    private static List<String> getPlayoffs(HashMap<String, Integer> wins, List<List<Object>> schedule) {

    }

    private static void simulateSeason(String league, List<List<Object>> schedule, HashMap<String, Integer> wins,
            HashMap<String, Integer> ratings) throws IOException {

        // Get a copy of the schedule for use in tiebreakers
        // Both played and unplayed games will be added to this list
        List<List<Object>> tiebreakSchedule = new ArrayList<List<Object>>();

        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i).get(5).toString().length() > 0) {
                List<Object> toRemove = schedule.remove(i);
                tiebreakSchedule.add(toRemove);
                i--;
            } else if (schedule.get(i).get(1).toString() == "Y") {
                schedule.remove(i);
                i--;
            }
        }

        for (List<Object> game : schedule) {
            String team1 = game.get(2).toString();
            String team2 = game.get(4).toString();
            Integer rating1 = ratings.get(team1);
            Integer rating2 = ratings.get(team2);

            // Simulate this game being played
            List<Object> result = playGame(team1, team2, rating1, rating2, false);

            // Add result to the tiebreakSchedule
            List<Object> gameResult = new ArrayList<Object>();
            gameResult.add(game.get(0));
            gameResult.add(game.get(1));
            gameResult.add(game.get(2));
            gameResult.add(game.get(3));
            gameResult.add(game.get(4));
            gameResult.add(result.get(0).toString());
            gameResult.add(result.get(4).toString());
            gameResult.add(game.get(7));
            gameResult.add(game.get(8));
            tiebreakSchedule.add(gameResult);

            // Add win to winner
            Integer winnerWins = wins.get(result.get(0).toString());
            wins.put(result.get(0).toString(), winnerWins + 1);

            // Change ratings of both teams
            Integer newRating1 = (int) ( (float) rating1 + (Utils.k * Float.parseFloat(result.get(2).toString())) );
            Integer newRating2 = (int) ( (float) rating2 + (Utils.k * Float.parseFloat(result.get(3).toString())) );
            ratings.put(result.get(0).toString(), newRating1);
            ratings.put(result.get(1).toString(), newRating2);
        }

        // Get playoff teams

        System.out.println(wins);
    }

    public static void runForecast(String[] args) throws IOException, GeneralSecurityException, SQLException {
        // Arguments
        String league = "major";
        Integer num_times = 10000;
        boolean official = false;
        boolean image = false;

        // Ensure valid args
        if (!Utils.isValidLeague(league)) {
            throw new IOException("Invalid League: " + league);
        }

        // Useful data
        List<List<Object>> schedule = getSchedule(league);
        HashMap<String, Integer> wins = getWins(league);
        HashMap<String, Integer> ratings = Database.getElo();

        // List of teams that make it to each stage
        String[] playoffTeams = {};
        String[] semiTeams = {};
        String[] finalTeams = {};
        String[] champTeams = {};
        HashMap<String, Float> playoffProbabilities = new HashMap<String, Float>();
        HashMap<String, Float> semiProbabilities = new HashMap<String, Float>();
        HashMap<String, Float> finalProbabilities = new HashMap<String, Float>();
        HashMap<String, Float> champProbabilities = new HashMap<String, Float>();
        HashMap<String, Float> predictedRecords = new HashMap<String, Float>();

        // Begin simulating seasons
        for (Integer i = 0; i <= num_times; i++) {
            System.out.println("Simulation #" + i + "       " + league);
            // Make copies of useful data
            HashMap<String, Integer> ratingsCopy = new HashMap<String, Integer>(ratings);
            HashMap<String, Integer> winsCopy = new HashMap<String, Integer>(wins);
            simulateSeason(league, schedule, winsCopy, ratingsCopy);
        }
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException, SQLException {
        // System.out.println(getSchedule("major"));
        simulateSeason("major", getSchedule("major"), getWins("major"), Database.getElo());
    }
}
