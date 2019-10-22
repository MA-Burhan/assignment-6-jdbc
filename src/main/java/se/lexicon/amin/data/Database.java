package se.lexicon.amin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/world?&autoReconnect=true" +
                                        "&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "devuser";
    private static final String PASSWORD = "1234";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER, PASSWORD);
    }
}
