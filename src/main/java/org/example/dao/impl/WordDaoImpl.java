package org.example.dao.impl;

import org.example.controller.RequestManager;
import org.example.dao.WordDao;
import org.example.modal.Word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public enum WordDaoImpl implements WordDao {
    INSTANCE;

    // JDBC variables for opening and managing connection
    private static PreparedStatement pstmt;
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private static final String ADD_WORD_QUERY = "INSERT INTO descriptions (word, description) VALUES (?, ?)";

    private static final String REMOVE_WORD_QUERY = "DELETE FROM descriptions WHERE id=?";

    private static final String GET_ALL_USER_WORDS = "SELECT id, word, description FROM descriptions";

    private static final String EDIT_WORD = "UPDATE descriptions SET description=? WHERE id=?";

    private static final Logger logger = Logger.getLogger(WordDaoImpl.class.getName());

    @Override
    public void addWord(Word word) {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediawiki", "root", "root");
            // getting Statement object to execute query
            stmt = con.createStatement();
            // executing SELECT query

            pstmt = con.prepareStatement(ADD_WORD_QUERY);
            pstmt.setString(1, word.getTitle());
            pstmt.setString(2, word.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException e) { logger.info(e.getMessage());}
            try { stmt.close(); } catch(SQLException e) {logger.info(e.getMessage());}
            try { rs.close(); } catch(SQLException e) { logger.info(e.getMessage());}
        }
    }


    @Override
    public void removeWord(Word word) {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediawiki", "root", "root");
            // getting Statement object to execute query
            stmt = con.createStatement();
            // executing SELECT query

            pstmt = con.prepareStatement(REMOVE_WORD_QUERY);
            pstmt.setInt(1, word.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
            try { stmt.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
            try { rs.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
        }
    }

    @Override
    public void editWord(Word word) {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediawiki", "root", "root");
            // getting Statement object to execute query
            stmt = con.createStatement();
            // executing SELECT query

            pstmt = con.prepareStatement(EDIT_WORD);
            pstmt.setString(1, word.getDescription());
            pstmt.setInt(2,word.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
            try { stmt.close(); } catch(SQLException e) { logger.info(e.getMessage());}
            try { rs.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
        }
    }

    @Override
    public List<Word> getUserWords() {
        List<Word> words = new ArrayList<>();
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediawiki", "root", "root");
            // getting Statement object to execute query
            stmt = con.createStatement();
            // executing SELECT query
            rs = stmt.executeQuery(GET_ALL_USER_WORDS);

        while (rs.next()) {
            Word word= new Word();
            word.setId(rs.getInt(1));
            word.setTitle(rs.getString(2));
            word.setDescription(rs.getString(3));
            words.add(word);
        } } catch (SQLException e) {
            logger.info(e.getMessage());
    } finally {
        //close connection ,stmt and resultset here
        try { con.close(); } catch(SQLException e) { logger.info(e.getMessage());}
        try { stmt.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
        try { rs.close(); } catch(SQLException e) { logger.info(e.getMessage()); }
    }
        return words;
    }
}
