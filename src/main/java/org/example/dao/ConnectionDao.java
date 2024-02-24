package org.example.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionDao {
    // JDBC variables for opening and managing connection
    private static java.sql.Connection con;
    Properties prop = new Properties();
    private static final Logger logger = Logger.getLogger(ConnectionDao.class.getName());

    public Connection openConnection() {
//        try (InputStream input = new FileInputStream("resources/application.properties")) {
//            prop.load(input);
//            String password = prop.getProperty("db.database");
//
//            // Используйте пароль из конфигурационного файла
//        } catch (SQLException e) {
//            logger.info(e.getMessage());        }
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediawiki", "root", "root");
            // getting Statement object to execute query
        } catch (
                SQLException e) {
            logger.info(e.getMessage());
        }
        return con;
    }
    public void closeConnection(PreparedStatement stmt){

            try {
                con.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }

    }
}
