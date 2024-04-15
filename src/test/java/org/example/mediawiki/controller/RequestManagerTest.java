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

        ResponseEntity<List<Word>> response = requestManager.addWords(params, inputWords);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inputWords, response.getBody());
    }


    @Test
    void testAddWords_BadRequest() {
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

        ResponseEntity<List<Word>> response = requestManager.addWords(params, inputWords);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testAddWords_PartiallyBadRequests() {
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

        ResponseEntity<List<Word>> response = requestManager.addWords(params, inputWords);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdWords, response.getBody());
    }


    @Test
    void testGetWordByTitle() {
        String title = "Test Title";
        Model model = mock(Model.class);
        List<Word> expectedWords = new ArrayList<>();
        when(wordService.getWordByTitle(title)).thenReturn(expectedWords);

        String result = requestManager.getWordByTitle(title, model);

        assertEquals("error", result);
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

        Search existingSearch = new Search();
        when(searchService.getSearchById(id)).thenReturn(existingSearch);

        doNothing().when(searchService).update(existingSearch);

        ResponseEntity<String> response = requestManager.updateSearch(id, newSearch);

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

        when(searchService.getSearchById(id)).thenReturn(null);

        ResponseEntity<String> response = requestManager.updateSearch(id, newSearch);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input!", response.getBody());
        verify(searchService, never()).update(any());
    }

    @Test
    void testUpdateSearch_Failure_NoTitle() {
        Long id = 1L;
        Search newSearch = new Search();

        Search existingSearch = new Search();
        when(searchService.getSearchById(id)).thenReturn(existingSearch);

        ResponseEntity<String> response = requestManager.updateSearch(id, newSearch);

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

        String result = requestManager.showAllSearches(model);

        assertEquals("searches", result);
    }

    @Test
    void testShowAllPages() throws NoSuchMethodException {
        Model model = mock(Model.class);

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

        String result = requestManager.showAllPages(model);

        assertEquals("error", result);
    }

    @Test
    void testShowAllWords() throws Exception {
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, null);
        Model model = Mockito.mock(Model.class);
        List<Word> words = new ArrayList<>();
        Word word = new Word();
        word.setTitle("apple");
        words.add(word);

        Constructor<CounterServiceImpl> constructor = CounterServiceImpl.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CounterServiceImpl counterService = constructor.newInstance();

        String result = requestManager.showAllWords(model);

        assertEquals("definition", result);
    }


    @Test
    void testCreateWord_Success() {
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        String result = requestManager.createWord(1L, "Apple", "Description of Apple");

        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testCreateWord_NullTitleOrDescription() {
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        String result = requestManager.createWord(1L, null, "Description of Apple");

        assertEquals("error", result);
    }

    @Test
    void testDeleteWord_Success() {
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);
        when(wordService.getExistingById(1L)).thenReturn(true);

        String result = requestManager.deleteWord(1L);

        assertEquals("redirect:/definition/showWords", result);
    }

    @Test
    void testDeleteWord_NonExistingId() {
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        when(wordService.getExistingById(1L)).thenReturn(false);

        String result = requestManager.deleteWord(1L);

        assertEquals("error", result);
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testDeleteSearch_Success() {
        when(searchService.getSearchExistingById(1L)).thenReturn(true);

        String result = requestManager.deleteSearch(1L);

        assertEquals("redirect:/definition/showWords", result);
        verify(searchService).delete(1L);
    }

    @Test
    void testDeleteSearch_NonExistingId() {
        WordServiceImpl wordService = mock(WordServiceImpl.class);
        SearchServiceImpl searchService = mock(SearchServiceImpl.class);
        RequestManager requestManager = new RequestManager(wordService, null, searchService);

        when(searchService.getSearchExistingById(1L)).thenReturn(false);

        String result = requestManager.deleteSearch(1L);

        assertEquals("error", result);
        verify(searchService, never()).delete(1L);
    }
}