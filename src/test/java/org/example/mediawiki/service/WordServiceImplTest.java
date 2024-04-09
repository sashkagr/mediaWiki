package org.example.mediawiki.service;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.WordRepository;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.example.mediawiki.service.impl.WordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordServiceImplTest {
    @Mock
    private WordRepository wordRepository;

    @Mock
    private PagesServiceImpl pagesService;

    @Mock
    private SearchServiceImpl searchService;

    @InjectMocks
    private WordServiceImpl wordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWords() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);

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

        Search search = new Search();
        when(searchService.getSearchById(anyLong())).thenReturn(search);

        ArgumentCaptor<Word> wordCaptor = ArgumentCaptor.forClass(Word.class);
        when(wordRepository.save(wordCaptor.capture())).thenReturn(new Word());

        // Act
        List<Word> createdWords = wordService.createWords(words, params);

        // Assert
        assertEquals(2, createdWords.size());
        verify(wordRepository, times(2)).save(any(Word.class));

        List<Word> capturedWords = wordCaptor.getAllValues();
        assertEquals("Title1", capturedWords.get(0).getTitle());
        assertEquals("Description1", capturedWords.get(0).getDescription());
        assertEquals("Title2", capturedWords.get(1).getTitle());
        assertEquals("Description2", capturedWords.get(1).getDescription());

        assertEquals(search, capturedWords.get(0).getSearch());
        assertEquals(search, capturedWords.get(1).getSearch());
    }

    @Test
    void testGetExistingById() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Long id = 1L;
        when(wordRepository.existingById(id)).thenReturn(new Word());

        // Act
        boolean existing = wordService.getExistingById(id);

        // Assert
        assertTrue(existing);
        verify(wordRepository, times(1)).existingById(id);
    }

    @Test
    void testGetWordBySearch() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Search search = new Search();
        when(wordRepository.existingBySearch(search)).thenReturn(new ArrayList<>());

        // Act
        List<Word> words = wordService.getWordBySearch(search);

        // Assert
        assertNotNull(words);
        verify(wordRepository, times(1)).existingBySearch(search);
    }

    @Test
    void testGetWordById() {
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
    void testCreate() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Word word = new Word();

        // Act
        wordService.create(word);

        // Assert
        verify(wordRepository, times(1)).save(word);
    }

    @Test
    void testDelete() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        long idToDelete = 1L;

        // Act
        wordService.delete(idToDelete);

        // Assert
        verify(wordRepository, times(1)).deleteById(idToDelete);
    }
    @Test
    void testUpdate() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Word word = new Word();

        // Act
        wordService.update(word);

        // Assert
        verify(wordRepository, times(1)).save(word);
    }
    @Test
    void testRead() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        when(wordRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Word> words = wordService.read();

        // Assert
        assertNotNull(words);
        verify(wordRepository, times(1)).findAll();
    }
    @Test
    void testGetWordByTitle() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        String title = "TestTitle";
        when(wordRepository.findWordByTitle(title)).thenReturn(new ArrayList<>());

        // Act
        List<Word> words = wordService.getWordByTitle(title);

        // Assert
        assertNotNull(words);
    }
    @Test
    void testGetWordsFromPages() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        Search search = new Search();
        when(pagesService.getPagesBySearch(search)).thenReturn(new ArrayList<>());

        // Act
        List<Word> words = wordService.getWordsFromPages(search);

        // Assert
        assertNotNull(words);
    }
}
