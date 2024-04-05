package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class WordRepositoryTest {

    @Mock
    private WordRepository wordRepository;


    @Test
     void testFindWordByTitle() {
        String title = "Example Title";
        List<Word> wordList = new ArrayList<>();
        when(wordRepository.findWordByTitle(any(String.class))).thenReturn(wordList);

        List<Word> result = wordRepository.findWordByTitle(title);

        assertEquals(wordList, result);
        verify(wordRepository, times(1)).findWordByTitle(title);
    }

    @Test
     void testExistingById() {
        Long id = 1L;
        Word word = new Word();
        when(wordRepository.existingById(anyLong())).thenReturn(word);

        Word result = wordRepository.existingById(id);

        assertEquals(word, result);
        verify(wordRepository, times(1)).existingById(id);
    }

    @Test
     void testExistingBySearch() {
        Search search = new Search();
        List<Word> wordList = new ArrayList<>();
        when(wordRepository.existingBySearch(any(Search.class))).thenReturn(wordList);

        List<Word> result = wordRepository.existingBySearch(search);

        assertEquals(wordList, result);
        verify(wordRepository, times(1)).existingBySearch(search);
    }
}
