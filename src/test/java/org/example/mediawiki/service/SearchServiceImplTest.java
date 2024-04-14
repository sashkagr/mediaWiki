package org.example.mediawiki.service;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.SearchRepository;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SearchServiceImplTest {

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private PagesServiceImpl pagesService;

    @InjectMocks
    private SearchServiceImpl searchService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Search search = new Search();

        searchService.create(search);

        verify(searchRepository, times(1)).save(search);
    }

    @Test
    void testDelete() {
        Long idToDelete = 1L;

        searchService.delete(idToDelete);

        verify(searchRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void testUpdate() {
        Search search = new Search();

        searchService.update(search);

        verify(searchRepository, times(1)).save(search);
    }

    @Test
    void testRead() {
        List<Search> expectedSearches = new ArrayList<>();
        when(searchRepository.findAll()).thenReturn(expectedSearches);

        List<Search> actualSearches = searchService.read();

        assertEquals(expectedSearches, actualSearches);
    }

    @Test
    void testCreateSearchAndPages() throws InterruptedException {
        String name = "TestName";

        List<Word> words = searchService.createSearchAndPages(name);

        assertNotNull(words);
    }


    @Test
    void testCreateSearchAndPagesCache() throws InterruptedException {
        // Arrange
        String name = "TestName";
        Search search = new Search();
        search.setTitle(name);
        when(searchRepository.save(any(Search.class))).thenReturn(search);

        List<Word> words = new ArrayList<>();
        for (int i = 1; i <= 10; i++) { // Create 10 words
            Word word = new Word();
            word.setId((long) i);
            word.setTitle("Title" + i);
            word.setDescription("Description" + i);
            words.add(word);
        }

       // when(wikiApiService.getDescriptionByTitle(name)).thenReturn(words);

        // Act
        List<Word> result = searchService.createSearchAndPages(name);

        // Assert
        assertNotNull(result);
        verify(searchRepository, times(1)).save(any(Search.class));
        verify(pagesService, times(10)).create(any(Pages.class)); // Verify create() is called 10 times
    }


    @Test
    void testGetSearchExistingById() {
        // Arrange
        Long id = 1L;
        Search search = new Search();
        when(searchRepository.existingById(id)).thenReturn(search);

        // Act
        boolean existing = searchService.getSearchExistingById(id);

        // Assert
        assertTrue(existing);
    }

    @Test
    void testGetSearchByTitle() {
        // Arrange
        String title = "TestTitle";
        Search search = new Search();
        when(searchRepository.existingByTitle(title)).thenReturn(search);

        // Act
        Search result = searchService.getSearchByTitle(title);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetSearchById() {
        // Arrange
        Long id = 1L;
        Search search = new Search();
        when(searchRepository.existingById(id)).thenReturn(search);

        // Act
        Search result = searchService.getSearchById(id);

        // Assert
        assertNotNull(result);
    }
}
