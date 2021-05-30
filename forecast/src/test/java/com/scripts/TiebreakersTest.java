package com.scripts;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

/**
 * Unit test for Tiebreakers.
 */
public class TiebreakersTest {
    @Test
    public void divisionWinnerTest() throws IOException, GeneralSecurityException, Exception {
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
}
