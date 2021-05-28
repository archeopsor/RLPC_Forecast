package com.scripts;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        System.out.println(getSchedule("major"));
    }
}
