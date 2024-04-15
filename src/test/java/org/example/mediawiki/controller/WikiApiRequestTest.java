package org.example.mediawiki.controller;

import org.example.mediawiki.modal.Word;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WikiApiRequestTest {

    @Test
    void testGetDescriptionByTitle() throws InterruptedException {
        String title = "Computer";

        List<Word> words = WikiApiRequest.getDescriptionByTitle(title);

        assertNotNull(words);
        assertFalse(words.isEmpty());
    }

    @Test
    void testGetDescriptionByPageId() throws IOException, InterruptedException {
        long id = 12345;

        Word word = WikiApiRequest.getDescriptionByPageId(id);

        assertNotNull(word);
        assertNotNull(word.getTitle());
        assertNotNull(word.getDescription());
    }
}
