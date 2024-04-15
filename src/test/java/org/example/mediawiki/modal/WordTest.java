package org.example.mediawiki.modal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {


    @Test
    void testHandlingOfNullValues() {
        Word word = new Word();
        word.setId(0);
        word.setTitle(null);
        word.setDescription(null);

        assertNull(word.getTitle());
        assertNull(word.getDescription());
    }

    @Test
    void testWordConstructor() {
        Word word = new Word();

        assertNotNull(word);
        assertEquals(0, word.getId());
        assertNull(word.getSearch());
        assertNull(word.getTitle());
        assertNull(word.getDescription());
    }

    @Test
    void testGetterAndSetter() {
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
    void testEqualsAndHashCode() {
        Word word1 = new Word();
        word1.setId(1);
        word1.setTitle("Test Title");
        word1.setDescription("Test Description");

        Word word2 = new Word();
        word2.setId(1);
        word2.setTitle("Test Title");
        word2.setDescription("Test Description");

        assertEquals(word1, word2);
        assertEquals(word1.hashCode(), word2.hashCode());

        Word word3 = new Word();
        word3.setId(2);
        word3.setTitle("Another Title");
        word3.setDescription("Another Description");

        assertEquals(word1, word2);
    }

    @Test
    void testEqualsWithNull() {
        Word word = new Word();
        assertNotEquals(null, word);
    }


    @Test
    void testToString() {
        Word word = new Word();
        word.setId(1);
        word.setTitle("Test Title");
        word.setDescription("Test Description");

        assertEquals("Word(id=1, search=null, title=Test Title, description=Test Description)", word.toString());
    }


}
