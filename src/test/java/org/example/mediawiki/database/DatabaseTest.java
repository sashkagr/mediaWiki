package org.example.mediawiki.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertNotNull;

 class DatabaseTest {
    private Connection connection;

    @Before
     void setUp() throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/mediawiki";
        String user = "root";
        String password = "root";
        connection = DriverManager.getConnection(jdbcUrl, user, password);
    }

    @After
     void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
     void testConnection() {
        assertNotNull("Connection should not be null", connection);
    }

}
