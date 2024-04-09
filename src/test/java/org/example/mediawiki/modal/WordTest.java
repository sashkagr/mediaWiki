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
}
