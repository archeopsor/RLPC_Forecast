package com.scripts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
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

    public DataSheet(String league) throws IOException {
        this.path = "data.xlsx";
        this.league = league;
        this.wb = WorkbookFactory.create(new FileInputStream(path));
        this.sheet = wb.getSheet(league + " Forecast");
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
        List<String> playoffs = (List<String>) sim.get(3);
        List<String> semis = (List<String>) sim.get(2);
        List<String> finals = (List<String>) sim.get(1);
        String champs = sim.get(0).toString();
        List<List<String>> schedule = (List<List<String>>) sim.get(5);

        // Add games to list
        for (int i = 0; i < schedule.size(); i++) {
            List<String> game = schedule.get(i);
            String winner = game.get(6);
            String loser = game.get(6).equals(game.get(3)) ? game.get(5) : game.get(3);
            String score = game.get(7);
            String result = winner + " " + score + " " + loser;

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

            List<String> playoffs = (List<String>) sim.get(3);
            List<String> semis = (List<String>) sim.get(2);
            List<String> finals = (List<String>) sim.get(1);
            String champs = sim.get(0).toString();
            List<List<String>> schedule = (List<List<String>>) sim.get(5);

            // Add games to list
            for (int i = 0; i < schedule.size(); i++) {
                List<String> game = schedule.get(i);
                String winner = game.get(6);
                String loser = game.get(6).equals(game.get(3)) ? game.get(5) : game.get(3);
                String score = game.get(7);
                String result = winner + " " + score + " " + loser;

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
