package com.scripts;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Forecast {

    private static final String p4SheetId = "1umoAxAcVLkE_XKlpTNNdc42rECU7-GtoDvUhEXja7XA";
    private static final String indySheetId = "10cLowvKoG5tAtLNS9NEk808vROULHl5KMU0LduBNqbI";
    private static final String powerRankingsSheetId = "1Tlc_TgGMrY5aClFF-Pb5xvtKrJ1Hn2PJOLy2fUDDdFI";
    private static final String forecastSheetId = "1GEFufHK5xt0WqThYC7xaK2gz3cwjinO43KOsb7HogQQ";

    private static List<List<Object>> getWins(String league) throws IOException, GeneralSecurityException {
        final String teamWinsRange = Utils.teamWinRanges(league);
        List<List<Object>> wins = SheetsHandler.getValues(powerRankingsSheetId, teamWinsRange);
        return wins;
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

    private static List<Object> playGame(String team1, String team2, Integer rating1, Integer rating2,
            boolean playoffs) {
        Integer Q1 = (int) Math.pow(10, (rating1 / 250));
        Integer Q2 = (int) Math.pow(10, (rating2 / 250));
        float team1WinProbability = Q1 / (Q1 + Q2);
        float team2WinProbability = Q2 / (Q1 + Q2);
        int numGames = (playoffs) ? 4 : 3;

        List<Object> teams = new ArrayList<Object>();
        teams.add(team1);
        teams.add(team2);

        List<Float> probabilities = new ArrayList<Float>();
        probabilities.add(team1WinProbability);
        probabilities.add(team2WinProbability);

        // Randomly pick winner based on the probabilities, and pick a random score
        String winner = Utils.getRandomElement(teams, probabilities).toString();
        int team1Score = (winner == team1) ? numGames : Utils.randBetween(0, numGames - 1);
        int team2Score = (winner == team2) ? numGames : Utils.randBetween(0, numGames - 1);

        List<Object> result = new ArrayList<Object>();
        result.add(winner);
        result.add(team1Score);
        result.add(team2Score);
        return result;
    }

    private static void simulateSeason(String league, List<List<Object>> schedule, List<List<Object>> wins,
            HashMap<String, Integer> ratings) {
        for (List<Object> game : schedule) {
            String team1 = game.get(2).toString();
            String team2 = game.get(4).toString();
            Integer rating1 = ratings.get(team1);
            Integer rating2 = ratings.get(team2);

            // Simulate this game being played
            List<Object> result = playGame(team1, team2, rating1, rating2, false);
        }
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
        List<List<Object>> wins = getWins(league);
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

        // Make copies of useful data
        HashMap<String, Integer> ratingsCopy = new HashMap<String, Integer>(ratings);

        // Begin simulating seasons
        for (Integer i = 0; i <= num_times; i++) {
            System.out.println("Simulation #" + i + "       " + league);
            simulateSeason(league, schedule, wins, ratingsCopy);
        }
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        List<Object> teams = new ArrayList<Object>();
        teams.add("team 1");
        teams.add("team 2");

        List<Float> probabilities = new ArrayList<Float>();
        probabilities.add((float) 0.2);
        probabilities.add((float) 0.8);

        for (int i = 0; i <= 100; i++) {
            System.out.println(Utils.getRandomElement(teams, probabilities));
        }
    }
}
