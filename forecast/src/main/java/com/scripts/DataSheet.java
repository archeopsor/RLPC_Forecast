package com.scripts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class DataSheet {
    String path;
    String league;
    Workbook wb;
    Sheet sheet;
    CSVPrinter printer;
    HashMap<String, String> abbreviations;

    private HashMap<String, String> createAbbreviations() {
        // For shortening team names
        HashMap<String, String> map = new HashMap<String, String>();

        // Major
        map.put("Ascension", "ASC");
        map.put("Bulls", "BUL");
        map.put("Cobras", "COB");
        map.put("Ducks", "DUC");
        map.put("Eagles", "EAG");
        map.put("Flames", "FLA");
        map.put("Hawks", "HAW");
        map.put("Kings", "KIN");
        map.put("Lions", "LIO");
        map.put("Lumberjacks", "LUM");
        map.put("Panthers", "PAN");
        map.put("Pirates", "PIR");
        map.put("Sharks", "SHA");
        map.put("Spartans", "SPA");
        map.put("Storm", "STO");
        map.put("Whitecaps", "WHI");

        // AAA
        map.put("Bobcats", "BOB");
        map.put("Bulldogs", "BDG");
        map.put("Dolphins", "DOL");
        map.put("Entropy", "ENT");
        map.put("Geese", "GSE");
        map.put("Heat", "HEA");
        map.put("Knights", "KNI");
        map.put("Osprey", "OSP");
        map.put("Owls", "OWL");
        map.put("Pioneers", "PIO");
        map.put("Raiders", "RAI");
        map.put("Thunder", "THU");
        map.put("Tigers", "TIG");
        map.put("Trojans", "TRO");
        map.put("Tundra", "TUN");
        map.put("Vipers", "VIP");

        // AA
        map.put("Avalanche", "AVA");
        map.put("Bandits", "BAN");
        map.put("Barracuda", "BAR");
        map.put("Dukes", "DUK");
        map.put("Falcons", "FAL");
        map.put("Herons", "HER");
        map.put("Inferno", "INF");
        map.put("Jaguars", "JAG");
        map.put("Lightning", "LGH");
        map.put("Lynx", "LNX");
        map.put("Mustangs", "MUS");
        map.put("Pulsars", "PUL");
        map.put("Pythons", "PYT");
        map.put("Voyagers", "VOY");
        map.put("Vultures", "VUL");
        map.put("Warriors", "WAR");

        // A
        map.put("Cardinals", "CAR");
        map.put("Cougars", "COU");
        map.put("Embers", "EMB");
        map.put("Eskimos", "ESK");
        map.put("Genesis", "GEN");
        map.put("Gulls", "GUL");
        map.put("Jesters", "JES");
        map.put("Leopards", "LEO");
        map.put("Miners", "MIN");
        map.put("Pelicans", "PEL");
        map.put("Rattlers", "RAT");
        map.put("Ravens", "RAV");
        map.put("Stallions", "STA");
        map.put("Tempest", "TEM");
        map.put("Titans", "TTN");
        map.put("Wranglers", "WRA");

        // Independent
        map.put("Admirals", "ADM");
        map.put("Beavers", "BEA");
        map.put("Centurions", "CEN");
        map.put("Cyclones", "CYC");
        map.put("Dragons", "DRA");
        map.put("Galaxy", "GAL");
        map.put("Grizzlies", "GRI");
        map.put("Rhinos", "RHI");
        map.put("Scorpions", "SCO");
        map.put("Sockeyes", "SCK");
        map.put("Thrashers", "THR");
        map.put("Toucans", "TOU");
        map.put("Wildcats", "WIL");
        map.put("Wizards", "WIZ");
        map.put("Wolves", "WOL");
        map.put("Yellow Jackets", "YLJ");
        
        // Maverick
        map.put("Camels", "CAM");
        map.put("Captains", "CAP");
        map.put("Hornets", "HOR");
        map.put("Jackrabbits", "JAC");
        map.put("Macaws", "MAC");
        map.put("Mages", "MAG");
        map.put("Otters", "OTT");
        map.put("Pandas", "PND");
        map.put("Piranhas", "PRN");
        map.put("Raptors", "RAP");
        map.put("Samurai", "SAM");
        map.put("Solar", "SOL");
        map.put("Terriers", "TER");
        map.put("Tides", "TID");
        map.put("Yetis", "YET");
        map.put("Zebras", "ZEB");

        // Renegade
        map.put("Comets", "COM");
        map.put("Coyotes", "COY");
        map.put("Fireflies", "FIR");
        map.put("Gorillas", "GOR");
        map.put("Harriers", "HAR");
        map.put("Hounds", "HOU");
        map.put("Hurricanes", "HUR");
        map.put("Koalas", "KOA");
        map.put("Pilots", "PIL");
        map.put("Puffins", "PUF");
        map.put("Stingrays", "STI");
        map.put("Vikings", "VIK");
        map.put("Warthogs", "WRT");
        map.put("Werewolves", "WWL");
        map.put("Witches", "WIT");
        map.put("Wolverines", "WVN");

        // Paladin
        map.put("Badgers", "BAD");
        map.put("Buzzards", "BUZ");
        map.put("Cosmos", "COS");
        map.put("Cubs", "CUB");
        map.put("Dragonflies", "DGF");
        map.put("Foxes", "FOX");
        map.put("Griffins", "GRF");
        map.put("Hammerheads", "HAM");
        map.put("Jackals", "JKL");
        map.put("Ninjas", "NIN");
        map.put("Penguins", "PEN");
        map.put("Quakes", "QUA");
        map.put("Roadrunners", "RDR");
        map.put("Sailors", "SAI");
        map.put("Sorcerers", "SOR");
        map.put("Wildebeests", "WDB");

        return map;
    }

    private String abbreviate(String team) {
        String name = abbreviations.get(team);
        if (name == null) {
            return team;
        } else {
            return name;
        }
    }

    private ArrayList<String> abbreviate(List<String> teams) {
        ArrayList<String> newList = new ArrayList<String>();
        for (String team : teams) {
            newList.add(abbreviate(team));
        }
        return newList;
    }

    public DataSheet(String league) throws IOException {
        this.path = "data.xlsx";
        this.league = league;
        this.wb = WorkbookFactory.create(new FileInputStream(path));
        this.sheet = wb.getSheet(league + " Forecast");
        this.abbreviations = createAbbreviations();
    }

    public void save() throws IOException {
        try (OutputStream fileOut = new FileOutputStream(path)) {
            wb.write(fileOut);
            fileOut.close();
        }
    }

    public void clear() {
        int rowEnd = sheet.getLastRowNum();

        // Loop over each row from row 1 to 1,000,000 (or however many there are)
        for (int i = 1; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            // Loop over each column
            short columnEnd = row.getLastCellNum();
            for (short j = 0; j <= columnEnd; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                cell.setBlank();
            }
        }
    }

    private void createCSV() throws IOException {
        this.printer = new CSVPrinter(new FileWriter(league + " Forecast.csv"), CSVFormat.EXCEL);

        // Add headers to file
        String[] headers = new String[148];
        for (int i = 0; i < 144; i++) {
            headers[i] = "Game " + (i+1);
        }
        headers[144] = "Quarterfinals";
        headers[145] = "Semifinals";
        headers[146] = "Finals";
        headers[147] = "Champion";

        printer.printRecord(headers);
    }

    @SuppressWarnings("unchecked")
    public void addSimToCSV(List<Object> sim) throws IOException {
        if (this.printer == null) {
            this.createCSV();
        }

        String[] data = new String[148];
        List<String> playoffs = abbreviate((List<String>) sim.get(3));
        List<String> semis = abbreviate((List<String>) sim.get(2));
        List<String> finals = abbreviate((List<String>) sim.get(1));
        String champs = abbreviate(sim.get(0).toString());
        List<List<String>> schedule = (List<List<String>>) sim.get(5);

        // Add games to list
        for (int i = 0; i < schedule.size(); i++) {
            List<String> game = schedule.get(i);
            String winner = game.get(6);
            String loser = game.get(6).equals(game.get(3)) ? game.get(5) : game.get(3);
            String score = game.get(7);
            // String result = abbreviate(winner) + " " + score + " " + abbreviate(loser);
            String result = matchEncoder.encode(this.league, score, winner, loser);

            data[i] = result;
        }

        // Add playoffs, semifinals, finals, champ teams to list
        data[144] = String.join(" ", playoffs);
        data[145] = String.join(" ", semis);
        data[146] = String.join(" ", finals);
        data[147] = String.join(" ", champs);

        printer.printRecord(data);
    }

    public void closeCSV() throws IOException {
        printer.flush();
        printer.close();
    }

    @SuppressWarnings("unchecked")
    public void simsToCSV(ArrayList<List<Object>> sims) throws IOException {
        int counter = 0;
        List<String[]> datalines = new ArrayList<>();
        String[] headers = new String[148];

        // Add headers
        for (int i = 0; i < 144; i++) {
            headers[i] = "Game " + (i+1);
        }
        headers[144] = "Quarterfinals";
        headers[145] = "Semifinals";
        headers[146] = "Finals";
        headers[147] = "Champion";

        for (List<Object> sim : sims) {
            String[] row = new String[148];
            System.out.println(counter);
            counter++;

            List<String> playoffs = abbreviate((List<String>) sim.get(3));
            List<String> semis = abbreviate((List<String>) sim.get(2));
            List<String> finals = abbreviate((List<String>) sim.get(1));
            String champs = abbreviate(sim.get(0).toString());
            List<List<String>> schedule = (List<List<String>>) sim.get(5);

            // Add games to list
            for (int i = 0; i < schedule.size(); i++) {
                List<String> game = schedule.get(i);
                String winner = game.get(6);
                String loser = game.get(6).equals(game.get(3)) ? game.get(5) : game.get(3);
                String score = game.get(7);
                String result = abbreviate(winner) + " " + score + " " + abbreviate(loser);

                row[i] = result;
            }

            // Add playoffs, semifinals, finals, champ teams to list
            row[144] = String.join(" ", playoffs);
            row[145] = String.join(" ", semis);
            row[146] = String.join(" ", finals);
            row[147] = String.join(" ", champs);

            datalines.add(row);
        }

        
        printer.printRecord(headers);
        for (String[] row : datalines) {
            printer.printRecord(row);
        }
    }

    @SuppressWarnings("unchecked")
    public void addSimsXL(ArrayList<List<Object>> sims) {
        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);

        int counter = 0;

        for (List<Object> sim : sims) {
            System.out.println(counter);
            counter++;
            List<String> playoffs = (List<String>) sim.get(3);
            List<String> semis = (List<String>) sim.get(2);
            List<String> finals = (List<String>) sim.get(1);
            String champs = sim.get(0).toString();
            List<List<String>> schedule = (List<List<String>>) sim.get(5);

            // Add each game in schedule to columns 0-143 of the row
            for (int i = 0; i < schedule.size(); i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }

                List<String> game = schedule.get(i);
                String winner = game.get(6);
                String loser = game.get(6).equals(game.get(3)) ? game.get(5) : game.get(3);
                String score = game.get(7);
                String result = winner + score + loser;

                cell.setCellValue(result);
            }

            // Add quarterfinals teams to columns 144-151
            for (int i = 144; i <= 151; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                String team = playoffs.get(i - 144);
                cell.setCellValue(team);
            }

            // Add semifinals teams to columns 152-155
            for (int i = 152; i <= 155; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                String team = semis.get(i - 152);
                cell.setCellValue(team);
            }

            // Add finals teams to columns 156-157
            for (int i = 156; i <= 157; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                String team = finals.get(i - 156);
                cell.setCellValue(team);
            }

            // Add champions to column 158
            Cell cell = row.getCell(158);
            if (cell == null) {
                cell = row.createCell(158);
            }
            cell.setCellValue(champs);
        }
    }

    public static void main(String args[]) throws IOException {
    }
}
