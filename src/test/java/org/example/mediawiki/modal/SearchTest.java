package org.example.mediawiki.modal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {

    private Search search;

    @BeforeEach
    public void setUp() {
        search = new Search();
    }

    @AfterEach
    public void tearDown() {
        search = null;
    }

    @Test
    public void testIdGetterAndSetter() {
        long id = 123;
        search.setId(id);
        assertEquals(id, search.getId());
    }

    @Test
    public void testTitleGetterAndSetter() {
        String title = "Test Title";
        search.setTitle(title);
        assertEquals(title, search.getTitle());
    }

    @Test
    public void testPagesListNotNull() {
        assertNotNull(search.getPages());
    }

    @Test
    public void testPagesListAddition() {
        Pages page = new Pages();
        search.getPages().add(page);
        assertTrue(search.getPages().contains(page));
    }

    @Test
    public void testWordsListNotNull() {
        assertNotNull(search.getWords());
    }

    @Test
    public void testWordsListAddition() {
        Word word = new Word();
        search.getWords().add(word);
        assertTrue(search.getWords().contains(word));
    }

    // Add more tests as needed for specific functionality
}
