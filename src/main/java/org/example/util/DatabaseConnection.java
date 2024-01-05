package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/project";
    private static final String user = "root";
    private static final String pass = "qwerty";

    private static Connection myConn;

    public static Connection getConnection() throws SQLException {
        if (myConn == null) {
            myConn = DriverManager.getConnection(URL, user, pass);
        }
        return myConn;
    }

}
