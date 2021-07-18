package com.scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.*;

public class Database {

    private static ArrayList<String> get_creds() throws IOException {
        File tokens = new File("/secrets.txt") ;
        BufferedReader br = new BufferedReader(new FileReader(tokens));
        final String url = br.readLine();
        final String user = br.readLine();
        final String password = br.readLine();
        br.close();
        ArrayList<String> creds = new ArrayList<String>();
        creds.add(url);
        creds.add(user);
        creds.add(password);

        return creds;
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection connect() {
        Connection conn = null;
        ArrayList<String> creds = null;
        try {
            creds = get_creds();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            conn = DriverManager.getConnection(creds.get(0), creds.get(1), creds.get(2));

            if (conn == null) {
                System.out.println("Failed to connect to the database.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Gets map of elo ratings for all RLPC teams
     * 
     * @return HashMap of teams and their respective elo ratings
     * @throws SQLException
     */
    public static HashMap<String, Integer> getElo() throws SQLException {
        Connection conn = connect();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT \"Team\", \"elo\" FROM elo");
        HashMap<String, Integer> ratings = new HashMap<String, Integer>();
        while (rs.next()) {
            ratings.put(rs.getString(1), rs.getInt(2));
        }
        rs.close();
        st.close();
        return ratings;
    }
}
