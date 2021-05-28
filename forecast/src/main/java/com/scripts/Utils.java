package com.scripts;

import java.util.HashMap;

public class Utils {
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
}
