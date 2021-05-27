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
}
