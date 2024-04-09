package org.example.mediawiki.modal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WordTest {

    @Test
    public void testWordConstructor() {
        // Create a Word instance using the constructor
        Word word = new Word();

        // Verify that the Word instance is not null
        assertNotNull(word);

        // Verify that the ID is initialized to 0
        assertEquals(0, word.getId());

        // Verify that search is null
        assertNull(word.getSearch());

        // Verify that title and description are initialized to null
        assertNull(word.getTitle());
        assertNull(word.getDescription());
    }

    @Test
    public void testGetterAndSetter() {
        // Create a Word instance
        Word word = new Word();

        // Set values using setters
        word.setId(1);
        word.setTitle("Test Title");
        word.setDescription("Test Description");

        // Verify that getters return the correct values
        assertEquals(1, word.getId());
        assertEquals("Test Title", word.getTitle());
        assertEquals("Test Description", word.getDescription());

        // Test setter and getter for Search
        Search search = new Search();
        word.setSearch(search);
        assertEquals(search, word.getSearch());
    }
    @Test
    public void testEqualsAndHashCode() {
        // Create two Word instances with the same attributes
        Word word1 = new Word();
        word1.setId(1);
        word1.setTitle("Test Title");
        word1.setDescription("Test Description");

        Word word2 = new Word();
        word2.setId(1);
        word2.setTitle("Test Title");
        word2.setDescription("Test Description");

        // Verify that equals() method returns true for equal objects
        assertTrue(word1.equals(word2));

        // Verify that hashCode() returns the same value for equal objects
        assertEquals(word1.hashCode(), word2.hashCode());
    }

    @Test
    public void testHandlingOfNullValues() {
        // Create a Word instance with null values
        Word word = new Word();
        word.setId(0);
        word.setTitle(null);
        word.setDescription(null);

        // Verify that null values are handled properly
        assertNull(word.getTitle());
        assertNull(word.getDescription());
    }

}
