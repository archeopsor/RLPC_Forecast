package com.scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;

public class Database {
    private static final String url = "jdbc:postgresql://ec2-3-234-169-147.compute-1.amazonaws.com:5432/ddm7aht2msunmo";
    private static final String user = "mpjpvpnvghfyjz";
    private static final String password = "e2f8b17dfbde75d865d9838a210db952cb19d2c4f1fb6f946ea1e3e8178bbe0a";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            if (conn == null) {
                System.out.println("Failed to connect to the database.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

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

    /**
     * @param args the command line arguments
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        System.out.println(getElo());
    }
}
