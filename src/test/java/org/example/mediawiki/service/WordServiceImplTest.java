package org.example.mediawiki.service;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.WordRepository;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.example.mediawiki.service.impl.WordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WordServiceImplTest {
    @Mock
    private WordRepository wordRepository;

    @Mock
    private PagesServiceImpl pagesService;

    @Mock
    private SearchServiceImpl searchService;

    @InjectMocks
    private WordServiceImpl wordService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWords() {
        List<Word> words = new ArrayList<>();

        Word word = new Word();
        word.setTitle("Title1");
        word.setDescription("Description1");
        words.add(word);
        word.setTitle("Title2");
        word.setDescription("Description2");
        words.add(word);


        List<Long> params = new ArrayList<>();
        params.add(1L);
        params.add(2L);

        Search search = new Search();
        when(searchService.getSearchById(anyLong())).thenReturn(search);
        when(wordRepository.save(any(Word.class))).thenReturn(new Word());

        List<Word> createdWords = wordService.createWords(words, params);

        assertEquals(2, createdWords.size());
        verify(wordRepository, times(2)).save(any(Word.class));
    }

    @Test
    public void testGetExistingById() {
        when(wordRepository.existingById(anyLong())).thenReturn(new Word());

        boolean existing = wordService.getExistingById(1L);

        assertTrue(existing);
    }

    @Test
    public void testGetWordBySearch() {
        Search search = new Search();
        when(wordRepository.existingBySearch(any(Search.class))).thenReturn(new ArrayList<>());

        List<Word> words = wordService.getWordBySearch(search);

        assertNotNull(words);
    }

    @Test
    public void testGetWordById() {
        Word word = mock(Word.class);
        when(word.getId()).thenReturn(1L);
        Search search = mock(Search.class);
        when(word.getSearch()).thenReturn(search);
        when(wordRepository.existingById(anyLong())).thenReturn(word);

        Word retrievedWord = wordService.getWordById(1L);

        assertNotNull(retrievedWord);
        assertEquals(1L, retrievedWord.getId());
    }

    @Test
    public void testCreate() {
        Word word = new Word();

        wordService.create(word);

        verify(wordRepository, times(1)).save(word);
    }

    @Test
    public void testDelete() {
        long idToDelete = 1L;

        wordService.delete(idToDelete);

        verify(wordRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    public void testUpdate() {
        Word word = new Word();

        wordService.update(word);

        verify(wordRepository, times(1)).save(word);
    }

    @Test
    public void testRead() {
        when(wordRepository.findAll()).thenReturn(new ArrayList<>());

        List<Word> words = wordService.read();

        assertNotNull(words);
    }

    @Test
    public void testGetWordByTitle() {
        String title = "TestTitle";
        when(wordRepository.findWordByTitle(title)).thenReturn(new ArrayList<>());

        List<Word> words = wordService.getWordByTitle(title);

        assertNotNull(words);
    }

    @Test
    public void testGetWordsFromPages() {
        Search search = new Search();
        when(pagesService.getPagesBySearch(search)).thenReturn(new ArrayList<>());

        List<Word> words = wordService.getWordsFromPages(search);

        assertNotNull(words);
    }
}
