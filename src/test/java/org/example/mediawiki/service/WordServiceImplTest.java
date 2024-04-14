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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private Cache cache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wordService = new WordServiceImpl(pagesService, wordRepository, searchService);

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
//    @Test
//    void testCreateWords() {
//        // Arrange
//        List<Word> words = new ArrayList<>();
//        Word word1 = new Word();
//        word1.setTitle("Title1");
//        word1.setDescription("Description1");
//        words.add(word1);
//
//        Word word2 = new Word();
//        word2.setTitle("Title2");
//        word2.setDescription("Description2");
//        words.add(word2);
//
//        List<Long> params = List.of(1L, 2L);
//
//        // Mock the behavior of searchService
//        when(searchService.getSearchById(anyLong())).thenReturn(new Search());
//
//        // Mock the behavior of wordRepository
//        when(wordRepository.save(any(Word.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        List<Word> createdWords = wordService.createWords(words, params);
//
//        // Assert
//        assertEquals(2, createdWords.size());
//        verify(searchService, times(2)).getSearchById(anyLong());
//        verify(wordRepository, times(2)).save(any(Word.class));
//    }
    @Test
    void testCreateWords() {
        // Arrange
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

        // Mock the behavior of searchService
        when(searchService.getSearchById(anyLong())).thenReturn(new Search());

        // Mock the behavior of wordRepository
        when(wordRepository.save(any(Word.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        List<Word> createdWords = wordService.createWords(words, params);

        // Assert
        assertEquals(2, createdWords.size());
        verify(searchService, times(2)).getSearchById(anyLong());
        verify(wordRepository, times(2)).save(any(Word.class));

        // Additional assertion for filtering
        assertTrue(createdWords.stream().allMatch(word -> word.getTitle() != null && word.getDescription() != null));
    }
    @Test
    void testGetWordBySearch() {
        // Arrange
        Search search = new Search();
        List<Word> expectedWords = new ArrayList<>();
        expectedWords.add(new Word());
        when(wordRepository.existingBySearch(search)).thenReturn(expectedWords);

        // Act
        List<Word> actualWords = wordService.getWordBySearch(search);

        // Assert
        assertEquals(expectedWords, actualWords);
        verify(wordRepository, times(1)).existingBySearch(search);
    }
    @Test
    void testGetWordById() {
        // Arrange
        Long id = 1L;
        Word expectedWord = new Word();
        Search search = new Search();
        search.setId(2L); // Устанавливаем идентификатор для объекта Search
        expectedWord.setSearch(search); // Устанавливаем Search для expectedWord
        when(wordRepository.existingById(id)).thenReturn(expectedWord);

        // Act
        Word actualWord = wordService.getWordById(id);

        // Assert
        assertEquals(expectedWord, actualWord);
        verify(wordRepository, times(1)).existingById(id);
    }
    @Test
    void testGetWordByTitle() {
        // Arrange
        WordServiceImpl wordService = new WordServiceImpl(pagesService, wordRepository, searchService);
        String title = "TestTitle";
        Search search = new Search();
        search.setId(1L); // Установка идентификатора для объекта Search
        when(wordRepository.findWordByTitle(title)).thenReturn(new ArrayList<>());
        when(searchService.getSearchById(anyLong())).thenReturn(search); // Мокирование сервиса для получения объекта Search

        // Act
        List<Word> words = wordService.getWordByTitle(title);

        // Assert
        assertNotNull(words);
    }
    @Test
    void testGetExistingByIdWhenExists() {
        // Arrange
        Long id = 1L;
        when(wordRepository.existingById(id)).thenReturn(new Word());

        // Act
        boolean existing = wordService.getExistingById(id);

        // Assert
        assertTrue(existing);
        verify(wordRepository, times(1)).existingById(id);
    }
    @Test
    void testGetExistingByIdWhenNotExists() {
        // Arrange
        Long id = 1L;
        when(wordRepository.existingById(id)).thenReturn(null);

        // Act
        boolean existing = wordService.getExistingById(id);

        // Assert
        assertFalse(existing);
        verify(wordRepository, times(1)).existingById(id);
    }
    @Test
    void testGetWordsFromPages() {
        // Arrange
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

        // Act
        List<Word> actualWords = wordService.getWordsFromPages(search);

        // Assert
        assertEquals(expectedWords, actualWords);
        verify(pagesService, times(1)).getPagesBySearch(search);
    }
}
