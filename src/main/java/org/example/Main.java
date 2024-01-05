package org.example;


import org.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("first_name") + "\t" +
                        rs.getString("pa_surname") + "\t" +
                        rs.getString("ma_surname") + "\t" +
                        rs.getString("email") + "\t" +
                        rs.getFloat("salary"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }

}