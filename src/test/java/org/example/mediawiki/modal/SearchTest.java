package org.example.mediawiki.modal;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

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
    public void testToString() {
        // Test toString() method
        long id = 123;
        String title = "Test Title";
        search.setId(id);
        search.setTitle(title);
        assertEquals("Search{id=123, title='Test Title'}", search.toString());
    }

    @Test
    public void testPagesListRemove() {
        // Test removing a page from pages list
        Pages page = new Pages();
        search.getPages().add(page);
        assertTrue(search.getPages().contains(page));
        search.getPages().remove(page);
        assertFalse(search.getPages().contains(page));
    }

    @Test
    public void testWordsListRemove() {
        // Test removing a word from words list
        Word word = new Word();
        search.getWords().add(word);
        assertTrue(search.getWords().contains(word));
        search.getWords().remove(word);
        assertFalse(search.getWords().contains(word));
    }

    @Test
    public void testIdGenerationType() throws NoSuchFieldException {
        var idField = Search.class.getDeclaredField("id");
        var generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValueAnnotation);
    }

    @Test
    public void testWordsOneToManyAnnotation() throws NoSuchFieldException {
        var wordsField = Search.class.getDeclaredField("words");
        var oneToManyAnnotation = wordsField.getAnnotation(OneToMany.class);
        assertNotNull(oneToManyAnnotation);
    }

    @Test
    public void testAddAndRemovePages() {
        Pages page = new Pages();
        search.getPages().add(page);
        assertTrue(search.getPages().contains(page));

        search.getPages().remove(page);
        assertFalse(search.getPages().contains(page));
    }

    @Test
    public void testWordsGetterAndSetter() {
        Word word = new Word();
        search.getWords().add(word);
        assertTrue(search.getWords().contains(word));

        List<Word> newWords = new ArrayList<>();
        search.setWords(newWords);
        assertEquals(newWords, search.getWords());
    }

}
