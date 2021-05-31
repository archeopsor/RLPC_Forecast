package com.scripts;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Unit test for Tiebreakers.
 */
public class TiebreakersTest {
    @Test
    public void TwoTeamDivisionWinnerTest() throws IOException, GeneralSecurityException, Exception {
        HashMap<String, Integer> wins = new HashMap<String, Integer>();
        wins.put("Cougars", 6);
        wins.put("Gulls", 12);
        wins.put("Leopards", 12);
        wins.put("Stallions", 11);

        List<List<Object>> schedule = SheetsHandler.getValues("1gHlqD-xekmpwFDpblAiJfpuCvxRliK6C-AjYoEUU8tc", "A Schedule!N4:V");

        // Get rid of preseason games in schedule
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i).get(5).toString().equals("PRESEASON")) {
                schedule.remove(i);
                i--;
            }
        }

        String winner = Tiebreakers.divisionWinner(wins, schedule, "a");
        assertTrue("Division Winner failed", winner.equals("Leopards"));
    }

    @Test
    public void MultiTeamDivisionWinnerTest() throws IOException, GeneralSecurityException, Exception {
        HashMap<String, Integer> wins = new HashMap<String, Integer>();
        wins.put("Hornets", 3);
        wins.put("Pandas", 6);
        wins.put("Samurai", 6);
        wins.put("Solar", 6);

        List<List<Object>> schedule = SheetsHandler.getValues("1gHlqD-xekmpwFDpblAiJfpuCvxRliK6C-AjYoEUU8tc", "Maverick Schedule!N4:V");

        // Get rid of preseason games in schedule
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i).get(5).toString().equals("PRESEASON")) {
                schedule.remove(i);
                i--;
            }
}

        String winner = Tiebreakers.divisionWinner(wins, schedule, "maverick");
        System.out.println(winner);
        assertTrue("Division Winner failed", winner.equals("Pandas"));
    }

    @Test
    public void getPlayoffsTest() throws Exception {
        HashMap<String, Integer> wins = Forecast.getWins("a");
        List<List<Object>> schedule = Forecast.getSchedule("a");
        List<List<String>> teams = Tiebreakers.getPlayoffs(wins, schedule, "a");
        List<String> conf1 = Arrays.asList(new String[] {"Leopards", "Ravens", "Gulls", "Stallions"});
        List<String> conf2 = Arrays.asList(new String[] {"Eskimos", "Miners", "Tempest", "Titans"});
        assertEquals(conf1, teams.get(1));
        assertEquals(conf2, teams.get(0));
    }
}
