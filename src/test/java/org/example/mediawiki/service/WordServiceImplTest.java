package org.example.mediawiki.service;

import org.example.mediawiki.cache.Cache;
import org.example.mediawiki.modal.Pages;
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

class WordServiceImplTest {

    @Mock
    private SearchServiceImpl searchService;

    @Mock
    private WordRepository wordRepository;

    @InjectMocks
    private WordServiceImpl wordService;

    @Mock
    private PagesServiceImpl pagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wordService = new WordServiceImpl(pagesService, wordRepository, searchService);

    }

    @Test
    void testGetExistingById() {
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Long id = 1L;
        when(wordRepository.existingById(id)).thenReturn(new Word());

        boolean existing = wordService.getExistingById(id);

        assertTrue(existing);
        verify(wordRepository, times(1)).existingById(id);
    }

    @Test
    void testCreate() {
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Word word = new Word();

        wordService.create(word);

        verify(wordRepository, times(1)).save(word);
    }

    @Test
    void testDelete() {
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        long idToDelete = 1L;

        wordService.delete(idToDelete);

        verify(wordRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void testUpdate() {
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Word word = new Word();

        wordService.update(word);

        verify(wordRepository, times(1)).save(word);
    }

    @Test
    void testRead() {
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        when(wordRepository.findAll()).thenReturn(new ArrayList<>());

        List<Word> words = wordService.read();

        assertNotNull(words);
        verify(wordRepository, times(1)).findAll();
    }

    @Test
    void testCreateWordsWithNonNullTitleAndDescription() {
        List<Word> words = new ArrayList<>();
        Word word1 = new Word();
        word1.setTitle("Title1");
        word1.setDescription("Description1");
        words.add(word1);

        Word word2 = new Word();
        word2.setTitle("Title2");
        word2.setDescription("Description2");
        words.add(word2);

        List<Long> params = List.of(1L, 2L);

        when(searchService.getSearchById(anyLong())).thenReturn(new Search());
        when(wordRepository.save(any(Word.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Word> createdWords = wordService.createWords(words, params);

        assertEquals(2, createdWords.size());
        verify(searchService, times(2)).getSearchById(anyLong());
        verify(wordRepository, times(2)).save(any(Word.class));
        assertTrue(createdWords.stream().allMatch(word -> word.getTitle() != null && word.getDescription() != null));
    }


    @Test
    void testCreateWords() {
        List<Word> words = new ArrayList<>();
        Word word1 = new Word();
        word1.setTitle("Title1");
        word1.setDescription("Description1");
        words.add(word1);

        Word word2 = new Word();
        word2.setTitle("Title2");
        word2.setDescription("Description2");
        words.add(word2);

        List<Long> params = List.of(1L, 2L);

        when(searchService.getSearchById(anyLong())).thenReturn(new Search());

        when(wordRepository.save(any(Word.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Word> createdWords = wordService.createWords(words, params);

        assertEquals(2, createdWords.size());
        verify(searchService, times(2)).getSearchById(anyLong());
        verify(wordRepository, times(2)).save(any(Word.class));

        assertTrue(createdWords.stream().allMatch(word -> word.getTitle() != null && word.getDescription() != null));
    }

    @Test
    void testGetWordBySearch() {
        Search search = new Search();
        List<Word> expectedWords = new ArrayList<>();
        expectedWords.add(new Word());
        when(wordRepository.existingBySearch(search)).thenReturn(expectedWords);
        List<Word> actualWords = wordService.getWordBySearch(search);

        assertEquals(expectedWords, actualWords);
        verify(wordRepository, times(1)).existingBySearch(search);
    }

    @Test
    void testGetWordById() {
        Long id = 1L;
        Word expectedWord = new Word();
        Search search = new Search();
        search.setId(2L);
        expectedWord.setSearch(search);
        when(wordRepository.existingById(id)).thenReturn(expectedWord);

        Word actualWord = wordService.getWordById(id);

        assertEquals(expectedWord, actualWord);
        verify(wordRepository, times(1)).existingById(id);
    }

    @Test
    void testGetWordByTitle() {
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        String title = "TestTitle";
        Search search = new Search();
        search.setId(1L);
        when(wordRepository.findWordByTitle(title)).thenReturn(new ArrayList<>());
        when(searchService.getSearchById(anyLong())).thenReturn(search);
        List<Word> words = wordService.getWordByTitle(title);

        assertNotNull(words);
    }

    @Test
    void testGetExistingByIdWhenExists() {
        Long id = 1L;
        when(wordRepository.existingById(id)).thenReturn(new Word());

        boolean existing = wordService.getExistingById(id);

        assertTrue(existing);
        verify(wordRepository, times(1)).existingById(id);
    }

    @Test
    void testGetExistingByIdWhenNotExists() {
        Long id = 1L;
        when(wordRepository.existingById(id)).thenReturn(null);

        boolean existing = wordService.getExistingById(id);

        assertFalse(existing);
        verify(wordRepository, times(1)).existingById(id);
    }

    @Test
    void testGetWordsFromPages() {
        Search search = new Search();
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages());
        when(pagesService.getPagesBySearch(search)).thenReturn(pages);
        List<Word> expectedWords = new ArrayList<>();
        Word expectedWord = new Word();
        expectedWord.setId(pages.get(0).getPageId());
        expectedWord.setTitle(pages.get(0).getTitle());
        expectedWord.setSearch(search);
        expectedWords.add(expectedWord);

        List<Word> actualWords = wordService.getWordsFromPages(search);

        assertEquals(expectedWords, actualWords);
        verify(pagesService, times(1)).getPagesBySearch(search);
    }
}
