package org.example.mediawiki.modal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WordTest {


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

    @Test
    public void testWordConstructor() {
        Word word = new Word();

        assertNotNull(word);
        assertEquals(0, word.getId());
        assertNull(word.getSearch());
        assertNull(word.getTitle());
        assertNull(word.getDescription());
    }

    @Test
    public void testGetterAndSetter() {
        Word word = new Word();
        word.setId(1);
        word.setTitle("Test Title");
        word.setDescription("Test Description");

        assertEquals(1, word.getId());
        assertEquals("Test Title", word.getTitle());
        assertEquals("Test Description", word.getDescription());

        Search search = new Search();
        word.setSearch(search);
        assertEquals(search, word.getSearch());
    }

    @Test
    public void testEqualsAndHashCode() {
        Word word1 = new Word();
        word1.setId(1);
        word1.setTitle("Test Title");
        word1.setDescription("Test Description");

        Word word2 = new Word();
        word2.setId(1);
        word2.setTitle("Test Title");
        word2.setDescription("Test Description");

        assertTrue(word1.equals(word2));
        assertEquals(word1.hashCode(), word2.hashCode());

        // Test inequality
        Word word3 = new Word();
        word3.setId(2);
        word3.setTitle("Another Title");
        word3.setDescription("Another Description");

        assertFalse(word1.equals(word3));
    }

    @Test
    public void testEqualsWithNull() {
        Word word = new Word();
        assertFalse(word.equals(null));
    }


    @Test
    public void testToString() {
        Word word = new Word();
        word.setId(1);
        word.setTitle("Test Title");
        word.setDescription("Test Description");

        assertEquals("Word(id=1, search=null, title=Test Title, description=Test Description)", word.toString());
    }


}
