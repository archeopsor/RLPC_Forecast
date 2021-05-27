package com.scripts;

import java.io.IOException;
import java.security.GeneralSecurityException;

import java.util.List;

import com.scripts.SheetsHandler;
import com.scripts.Utils;

public class Forecast {

    private static List<List<Object>> getWins(String league) throws IOException, GeneralSecurityException {
        final String powerRankingsSheetId = "1Tlc_TgGMrY5aClFF-Pb5xvtKrJ1Hn2PJOLy2fUDDdFI";
        final String teamWinsRange = Utils.teamWinRanges(league);
        List<List<Object>> wins = SheetsHandler.getValues(powerRankingsSheetId, teamWinsRange);
        return wins;
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        System.out.println(getWins("major"));
    }
}
