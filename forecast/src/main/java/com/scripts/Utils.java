package com.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final int k = 32;

    /**
     * Get range for Team wins for any given league
     * 
     * @param league
     * @return range to get team wins from power rankings sheet
     */
    public static String teamWinRanges(String league) {
        HashMap<String, String> teamWinRanges = new HashMap<String, String>();
        teamWinRanges.put("major", "Team Wins!A2:C17");
        teamWinRanges.put("aaa", "Team Wins!E2:G17");
        teamWinRanges.put("aa", "Team Wins!I2:K17");
        teamWinRanges.put("a", "Team Wins!M2:O17");
        teamWinRanges.put("independent", "Team Wins!Q2:S17");
        teamWinRanges.put("maverick", "Team Wins!U2:W17");
        teamWinRanges.put("renegade", "Team Wins!Y2:AA17");
        teamWinRanges.put("paladin", "Team Wins!AC2:AE17");

        return teamWinRanges.get(league);
    }

    public static String[] teams(String league) {
        HashMap<String, String[]> teams = new HashMap<String, String[]>();
        teams.put("major", new String[] {"Bulls", "Lions", "Panthers", "Sharks", "Cobras", "Ducks", "Eagles", "Hawks", "Kings", "Lumberjacks", "Pirates", "Spartans", "Ascension", "Flames", "Storm", "Whitecaps"});
        teams.put("aaa", new String[] {"Bobcats", "Bulldogs", "Dolphins", "Tigers", "Geese", "Osprey", "Owls", "Vipers", "Knights", "Pioneers", "Raiders", "Trojans", "Entropy", "Heat", "Thunder", "Tundra"});
        teams.put("aa", new String[] {"Barracuda", "Jaguars", "Lynx", "Mustangs", "Falcons", "Herons", "Pythons", "Vultures", "Bandits", "Dukes", "Vultures", "Warriors", "Avalanche", "Inferno", "Lightning", "Pulsars"});
        teams.put("a", new String[] {"Cougars", "Gulls", "Leopards", "Stallions", "Cardinals", "Rattlers", "Ravens", "Pelicans", "Jesters", "Miners", "Wranglers", "Titans", "Embers", "Eskimos", "Genesis", "Tempest"});
        teams.put("independent", new String[] {"Admirals", "Beavers", "Cyclones", "Dragons", "Centurions", "Galaxy", "Grizzlies", "Yellow Jackets", "Scorpions", "Thrashers", "Toucans", "Wizards", "Rhinos", "Sockeyes", "Wildcats", "Wolves"});
        teams.put("maverick", new String[] {"Camels", "Macaws", "Mages", "Raptors", "Jackrabbits", "Piranhas", "Terriers", "Zebras", "Captains", "Otters", "Tides", "Yetis", "Hornets", "Pandas", "Samurai", "Solar"});
        teams.put("renegade", new String[] {});
        teams.put("paladin", new String[] {});
        
        return teams.get(league);
    }

    public static boolean isValidLeague(String league) {
        league = league.toLowerCase();
        switch(league) {
            case "major": return true;
            case "aaa": return true;
            case "aa": return true;
            case "a": return true;
            case "independent": return true;
            case "maverick": return true;
            default: return false;
            }
        }

    public static Object getRandomElement(List<Object> list, List<Float> probabilities) {
        Random random = new Random();
        final float r = (float) random.nextDouble();
        float sum = (float) 0.0;
        int index = 0;
        for (float i : probabilities) {
            if (r > sum && r < (sum+i)) {
                return list.get(index);
            } else {
                index++;
            }
        }
        return getRandomElement(list, probabilities);
    }

    public static int randBetween(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static boolean inSameDivision(List<String> teams, String league) {
        HashSet<Integer> divisions = new HashSet<Integer>();
        String[] teamsList = teams(league);
        for (String team : teams) {
            int index = Arrays.asList(teamsList).indexOf(team);
            divisions.add(Math.floorDiv(index, 4));
        }
        if (divisions.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean inSameConference(List<String> teams, String league) {
        HashSet<Integer> divisions = new HashSet<Integer>();
        String[] teamsList = teams(league);
        for (String team : teams) {
            int index = Arrays.asList(teamsList).indexOf(team);
            divisions.add(Math.floorDiv(index, 8));
        }
        if (divisions.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> getDivisionTeams(String mainTeam, String league) throws Exception {
        String[] teamsList = teams(league);
        List<String> teamsInDivision = new ArrayList<String>();
        teamsInDivision.add(mainTeam);
        for (String team: teamsList) {
            List<String> arrayWithTeams = new ArrayList<String>();
            arrayWithTeams.add(mainTeam);
            arrayWithTeams.add(team);
            if (team.equals(mainTeam)) {
                continue;
            } else if (inSameDivision(arrayWithTeams, league)) {
                teamsInDivision.add(team);
            }
        }
        if (teamsInDivision.size() != 4) {
            System.out.println(teamsInDivision);
            throw new Exception("Got an invalid number of teams in division");
        } else {
            return teamsInDivision;
        }
    }

    public static List<String> getConferenceTeams(String mainTeam, String league) throws Exception {
        String[] teamsList = teams(league);
        List<String> teamsInConference = new ArrayList<String>();
        teamsInConference.add(mainTeam);
        for (String team: teamsList) {
            List<String> arrayWithTeams = new ArrayList<String>();
            arrayWithTeams.add(mainTeam);
            arrayWithTeams.add(team);
            if (team.equals(mainTeam)) {
                continue;
            } else if (inSameConference(arrayWithTeams, league)) {
                teamsInConference.add(team);
            }
        }
        if (teamsInConference.size() != 8) {
            System.out.println(teamsInConference);
            throw new Exception("Got an invalid number of teams in conference");
        } else {
            return teamsInConference;
        }
    }

    public static List<Integer> twoMaxNumbers(Collection<Integer> nums){
        int maxOne = 0;
        int maxTwo = 0;
        for(Integer n:nums){
            if(maxOne < n){
                maxTwo = maxOne;
                maxOne =n;
            } else if(maxTwo < n){
                maxTwo = n;
            }
        }

        List<Integer> result = new ArrayList<Integer>();
        result.add(maxOne);
        result.add(maxTwo);
        return result;
    }
}
