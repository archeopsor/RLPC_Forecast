package com.scripts;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UtilsTest {
    @Test
    public void inSameDivisionTest() {
        List<String> teams = new ArrayList<String>();
        teams.add("Camels");
        teams.add("Macaws");
        assertTrue("inSameDivision() failed", Utils.inSameDivision(teams, "maverick"));
    }
}
