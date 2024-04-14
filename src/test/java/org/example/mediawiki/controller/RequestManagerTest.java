package org.example.mediawiki.controller;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.service.impl.CounterServiceImpl;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.example.mediawiki.service.impl.WordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestManagerTest {


    @Mock
    private WordServiceImpl wordService;

    @Mock
    private PagesServiceImpl pagesService;

    @Mock
    private SearchServiceImpl searchService;

    @InjectMocks
    private RequestManager requestManager;


    @Test
    void testAddWords_Success() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        PagesServiceImpl pagesService = mock(PagesServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);

        RequestManager requestManager = new RequestManager(wordService, pagesService, searchService);
        Word word = new Word();
        word.setTitle("apple");
        Word word1 = new Word();
        word.setTitle("banana");
        List<Word> inputWords = Arrays.asList(word, word1);
        List<Long> params = Collections.singletonList(123L);

        when(wordService.createWords(inputWords, params)).thenReturn(inputWords);

        // Act
        ResponseEntity<List<Word>> response = requestManager.addWords(params, inputWords);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inputWords, response.getBody());
    }


    @Test
    void testAddWords_BadRequest() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        PagesServiceImpl pagesService = mock(PagesServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);

        RequestManager requestManager = new RequestManager(wordService, pagesService, searchService);
        Word word = new Word();
        word.setTitle("apple");
        Word word1 = new Word();
        word.setTitle("banana");
        List<Word> inputWords = Arrays.asList(word, word1);
        List<Long> params = Collections.singletonList(123L);

        when(wordService.createWords(inputWords, params)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Word>> response = requestManager.addWords(params, inputWords);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testAddWords_PartiallyBadRequests() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        PagesServiceImpl pagesService = mock(PagesServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);

        RequestManager requestManager = new RequestManager(wordService, pagesService, searchService);
        Word word = new Word();
        word.setTitle("apple");
        Word word1 = new Word();
        word.setTitle("banana");
        List<Word> inputWords = Arrays.asList(word, word1);
        List<Long> params = Collections.singletonList(123L);

        List<Word> createdWords = Collections.singletonList(word);
        when(wordService.createWords(inputWords, params)).thenReturn(createdWords);

        // Act
        ResponseEntity<List<Word>> response = requestManager.addWords(params, inputWords);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdWords, response.getBody());
    }


    @Test
    void testGetWordByTitle() {
        String title = "Test Title";
        List<Word> expectedWords = new ArrayList<>();
        when(wordService.getWordByTitle(title)).thenReturn(expectedWords);

        List<Word> actualWords = requestManager.getWordByTitle(title);

        assertEquals(expectedWords, actualWords);
    }

    @Test
    void testGetWordById() {
        Long id = 1L;
        Word word = new Word();
        when(wordService.getWordById(id)).thenReturn(word);
        Model model = mock(Model.class);

        String result = requestManager.getWordById(id, model);

        assertEquals("update", result);
        verify(model, times(1)).addAttribute("word", word);
    }

    @Test
    void testGetDefinition() {
        String name = "Test Name";
        Model model = mock(Model.class);
        Search search = new Search();
        List<Word> expectedWords = new ArrayList<>();
        expectedWords.add(new Word());
        when(searchService.getSearchByTitle(name)).thenReturn(search);
        when(wordService.getWordBySearch(search)).thenReturn(expectedWords);

        String result = requestManager.getDefinition(name, model);

        assertEquals("words", result);
        verify(model, times(1)).addAttribute("words", expectedWords);
    }

    @Test
    void testCreateWordBySearch() {
        Long id = 1L;
        Search currentSearch = new Search();
        Word word = new Word();
        when(wordService.getWordById(id)).thenReturn(word);
        when(pagesService.getPageByPageId(id)).thenReturn(new Pages());

        String result = requestManager.createWordBySearch(id);

        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testUpdateSearch_Success() {
        Long id = 1L;
        String newTitle = "New Search Title";
        Search newSearch = new Search();
        newSearch.setTitle(newTitle);

        // Mocking the search returned by the service
        Search existingSearch = new Search();
        when(searchService.getSearchById(id)).thenReturn(existingSearch);

        // Mocking the service update method
        doNothing().when(searchService).update(existingSearch);

        // Calling the method under test
        ResponseEntity<String> response = requestManager.updateSearch(id, newSearch);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Search was updated", response.getBody());
        assertEquals(newTitle, existingSearch.getTitle());
    }

    @Test
    void testUpdateSearch_Failure_NoSearchFound() {
        Long id = 1L;
        String newTitle = "New Search Title";
        Search newSearch = new Search();
        newSearch.setTitle(newTitle);

        // Mocking the search returned by the service as null (indicating no search found)
        when(searchService.getSearchById(id)).thenReturn(null);

        // Calling the method under test
        ResponseEntity<String> response = requestManager.updateSearch(id, newSearch);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input!", response.getBody());
        verify(searchService, never()).update(any());
    }

    @Test
    void testUpdateSearch_Failure_NoTitle() {
        Long id = 1L;
        Search newSearch = new Search();

        // Mocking the search returned by the service
        Search existingSearch = new Search();
        when(searchService.getSearchById(id)).thenReturn(existingSearch);

        // Calling the method under test
        ResponseEntity<String> response = requestManager.updateSearch(id, newSearch);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input!", response.getBody());
        verify(searchService, never()).update(any());
    }

    @Test
    void testDeleteSearch() {
        Long id = 1L;
        when(searchService.getSearchExistingById(id)).thenReturn(true);

        String result = requestManager.deleteSearch(id);

        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testUpdateWord() {
        Long id = 1L;
        String title = "New Title";
        String description = "New Description";
        Word word = new Word();
        when(wordService.getWordById(id)).thenReturn(word);

        String result = requestManager.updateWord(title, description, id);

        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testShowAllSearches() throws NoSuchMethodException {
        // Arrange
        Constructor<CounterServiceImpl> constructor = CounterServiceImpl.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CounterServiceImpl counterService = null;
        try {
            counterService = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(null, null, searchService);
        Model model = Mockito.mock(Model.class);
        List<Search> searches = new ArrayList<>();
        searches.add(new Search());

        when(searchService.read()).thenReturn(searches);

        // Act
        String result = requestManager.showAllSearches(model);

        // Assert
        assertEquals("searches", result);
    }

    @Test
    void testShowAllPages() throws NoSuchMethodException {
        // Arrange
        Constructor<CounterServiceImpl> constructor = CounterServiceImpl.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CounterServiceImpl counterService = null;
        try {
            counterService = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PagesServiceImpl pagesService = mock(PagesServiceImpl.class);
        RequestManager requestManager = new RequestManager(null, pagesService, null);
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages());

        when(pagesService.read()).thenReturn(pages);

        // Act
        List<Pages> result = requestManager.showAllPages();

        // Assert
        assertEquals(pages, result);
    }

    @Test
    void testShowAllWords() throws Exception {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, null);
        Model model = Mockito.mock(Model.class);
        List<Word> words = new ArrayList<>();
        Word word = new Word();
        word.setTitle("apple");
        words.add(word);

        // Accessing private constructor using reflection
        Constructor<CounterServiceImpl> constructor = CounterServiceImpl.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CounterServiceImpl counterService = constructor.newInstance();

        // Act
        String result = requestManager.showAllWords(model);

        // Assert
        assertEquals("definition", result);
    }


    @Test
    void testCreateWord_Success() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        // Act
        String result = requestManager.createWord(1L, "Apple", "Description of Apple");

        // Assert
        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testCreateWord_NullTitleOrDescription() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        // Act
        String result = requestManager.createWord(1L, null, "Description of Apple");

        // Assert
        assertEquals("error", result);
    }

    @Test
    void testDeleteWord_Success() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);
        when(wordService.getExistingById(1L)).thenReturn(true);

        // Act
        String result = requestManager.deleteWord(1L);

        // Assert
        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testDeleteWord_NonExistingId() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        // Setup mock behavior
        when(wordService.getExistingById(1L)).thenReturn(false);

        // Act
        String result = requestManager.deleteWord(1L);

        // Assert
        assertEquals("error", result);
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private Logger log;

    @Test
    void testDeleteSearch_Success() {
        // Arrange
        // Mocking the behavior to simulate that the search with ID 1 exists
        when(searchService.getSearchExistingById(1L)).thenReturn(true);

        // Act
        String result = requestManager.deleteSearch(1L);

        assertEquals("redirect:/definition/showWords", result);
        verify(searchService).delete(1L);
    }

    @Test
    void testDeleteSearch_NonExistingId() {
        // Arrange
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        // Setup mock behavior
        when(searchService.getSearchExistingById(1L)).thenReturn(false);

        // Act
        String result = requestManager.deleteSearch(1L);

        assertEquals("error", result);
        verify(searchService, never()).delete(1L);
    }
}





