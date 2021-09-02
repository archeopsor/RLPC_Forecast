package com.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MatchEncoder {
    static HashMap<String, Integer> leagues;
    static HashMap<String, Integer> teams;
    static HashMap<String, Integer> results;
    static HashMap<List<String>, String> cache = new HashMap<List<String>, String>();

    static {
        leagues = new HashMap<String, Integer>();
        leagues.put("major", 0);
        leagues.put("aaa", 1);
        leagues.put("aa", 2);
        leagues.put("a", 3);
        leagues.put("independent", 4);
        leagues.put("maverick", 5);
        leagues.put("renegade", 6);
        leagues.put("paladin", 7);

        teams = new HashMap<String, Integer>();

        // Major
        teams.put("Ascension", 0);
        teams.put("Bulls", 1);
        teams.put("Cobras", 2);
        teams.put("Ducks", 3);
        teams.put("Eagles", 4);
        teams.put("Flames", 5);
        teams.put("Hawks", 6);
        teams.put("Kings", 7);
        teams.put("Lions", 8);
        teams.put("Lumberjacks", 9);
        teams.put("Panthers", 10);
        teams.put("Pirates", 11);
        teams.put("Sharks", 12);
        teams.put("Spartans", 13);
        teams.put("Storm", 14);
        teams.put("Whitecaps", 15);

        // AAA
        teams.put("Bobcats", 0);
        teams.put("Bulldogs", 1);
        teams.put("Dolphins", 2);
        teams.put("Entropy", 3);
        teams.put("Geese", 4);
        teams.put("Heat", 5);
        teams.put("Knights", 6);
        teams.put("Osprey", 7);
        teams.put("Owls", 8);
        teams.put("Pioneers", 9);
        teams.put("Raiders", 10);
        teams.put("Thunder", 11);
        teams.put("Tigers", 12);
        teams.put("Trojans", 13);
        teams.put("Tundra", 14);
        teams.put("Vipers", 15);

        // AA
        teams.put("Avalanche", 0);
        teams.put("Bandits", 1);
        teams.put("Barracuda", 2);
        teams.put("Dukes", 3);
        teams.put("Falcons", 4);
        teams.put("Herons", 5);
        teams.put("Inferno", 6);
        teams.put("Jaguars", 7);
        teams.put("Lightning", 8);
        teams.put("Lynx", 9);
        teams.put("Mustangs", 10);
        teams.put("Pulsars", 11);
        teams.put("Pythons", 12);
        teams.put("Voyagers", 13);
        teams.put("Vultures", 14);
        teams.put("Warriors", 15);

        // A
        teams.put("Cardinals", 0);
        teams.put("Cougars", 1);
        teams.put("Embers", 2);
        teams.put("Eskimos", 3);
        teams.put("Genesis", 4);
        teams.put("Gulls", 5);
        teams.put("Jesters", 6);
        teams.put("Leopards", 7);
        teams.put("Miners", 8);
        teams.put("Pelicans", 9);
        teams.put("Rattlers", 10);
        teams.put("Ravens", 11);
        teams.put("Stallions", 12);
        teams.put("Tempest", 13);
        teams.put("Titans", 14);
        teams.put("Wranglers", 15);

        // Independent
        teams.put("Admirals", 0);
        teams.put("Beavers", 1);
        teams.put("Centurions", 2);
        teams.put("Cyclones", 3);
        teams.put("Dragons", 4);
        teams.put("Galaxy", 5);
        teams.put("Grizzlies", 6);
        teams.put("Rhinos", 7);
        teams.put("Scorpions", 8);
        teams.put("Sockeyes", 9);
        teams.put("Thrashers", 10);
        teams.put("Toucans", 11);
        teams.put("Wildcats", 12);
        teams.put("Wizards", 13);
        teams.put("Wolves", 14);
        teams.put("Yellow Jackets", 15);
        
        // Maverick
        teams.put("Camels", 0);
        teams.put("Captains", 1);
        teams.put("Hornets", 2);
        teams.put("Jackrabbits", 3);
        teams.put("Macaws", 4);
        teams.put("Mages", 5);
        teams.put("Otters", 6);
        teams.put("Pandas", 7);
        teams.put("Piranhas", 8);
        teams.put("Raptors", 9);
        teams.put("Samurai", 10);
        teams.put("Solar", 11);
        teams.put("Terriers", 12);
        teams.put("Tides", 13);
        teams.put("Yetis", 14);
        teams.put("Zebras", 15);

        // Renegade
        teams.put("Comets", 0);
        teams.put("Coyotes", 1);
        teams.put("Fireflies", 2);
        teams.put("Gorillas", 3);
        teams.put("Harriers", 4);
        teams.put("Hounds", 5);
        teams.put("Hurricanes", 6);
        teams.put("Koalas", 7);
        teams.put("Pilots", 8);
        teams.put("Puffins", 9);
        teams.put("Stingrays", 10);
        teams.put("Vikings", 11);
        teams.put("Warthogs", 12);
        teams.put("Werewolves", 13);
        teams.put("Witches", 14);
        teams.put("Wolverines", 15);

        // Paladin
        teams.put("Badgers", 0);
        teams.put("Buzzards", 1);
        teams.put("Cosmos", 2);
        teams.put("Cubs", 3);
        teams.put("Dragonflies", 4);
        teams.put("Foxes", 5);
        teams.put("Griffins", 6);
        teams.put("Hammerheads", 7);
        teams.put("Jackals", 8);
        teams.put("Ninjas", 9);
        teams.put("Penguins", 10);
        teams.put("Quakes", 11);
        teams.put("Roadrunners", 12);
        teams.put("Sailors", 13);
        teams.put("Sorcerers", 14);
        teams.put("Wildebeests", 15);

        results = new HashMap<String, Integer>();

        results.put("3-0", 0);
        results.put("3 - 0", 0);
        results.put("3-1", 1);
        results.put("3 - 1", 1);
        results.put("3-2", 2);
        results.put("3 - 2", 2);
        results.put("ff", 3);
        results.put("FF", 3);
    }
    
    private int encodeLeague(String league) {
        return leagues.get(league);
    }

    private int encodeTeam(String team) {
        return teams.get(team);
    }

    private int encodeResult(String result) {
        return results.get(result);
    }

    public String encode(String league, String result, String winner, String loser) {
        ArrayList<String> cache_list = new ArrayList<String>();
        String cache_value = cache.get(cache_list);
        if (cache_value != null) {
            return cache_value;
        }

        int match1 = 0b0;
        int match2 = 0b0;

        // First four bits correspond to league (8 choices)
        match1 = match1 << 4;
        match1 = match1 | encodeLeague(league);

        // Next four bits correspond to result (4 choices currently, likely need to add more for double ffs)
        match1 = match1 << 4;
        match1 = match1 | encodeResult(result);

        // Next four bits correspond to match winner (16 choices)
        match2 = match2 << 4;
        match2 = match2 | encodeTeam(winner);

        // Final four bits correspond to match loser (16 choices)
        match2 = match2 << 4;
        match2 = match2 | encodeTeam(loser);

        String encoded = String.format("%1$02x", match1) + String.format("%1$02x", match2);

        cache.put(cache_list, encoded);

        return encoded;
    }
}
