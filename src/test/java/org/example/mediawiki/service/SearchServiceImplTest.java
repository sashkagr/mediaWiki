package org.example.mediawiki.service;

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

public class SearchServiceImplTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private PagesServiceImpl pagesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSearchExistingById() {
        Long id = 1L;
        when(searchRepository.existingById(id)).thenReturn(new Search());

        boolean existing = searchService.getSearchExistingById(id);

        assertTrue(existing);
    }

    @Test
    public void testGetSearchByTitle() {
        String title = "TestTitle";
        when(searchRepository.existingByTitle(title)).thenReturn(new Search());

        Search search = searchService.getSearchByTitle(title);

        assertNotNull(search);
    }

    @Test
    public void testGetSearchById() {
        Long id = 1L;
        when(searchRepository.existingById(id)).thenReturn(new Search());

        Search search = searchService.getSearchById(id);

        assertNotNull(search);
    }

    @Test
    public void testCreate() {
        Search search = new Search();

        searchService.create(search);

        verify(searchRepository, times(1)).save(search);
    }

    @Test
    public void testDelete() {
        Long idToDelete = 1L;

        searchService.delete(idToDelete);

        verify(searchRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    public void testUpdate() {
        Search search = new Search();

        searchService.update(search);

        verify(searchRepository, times(1)).save(search);
    }

    @Test
    public void testRead() {
        List<Search> expectedSearches = new ArrayList<>();
        when(searchRepository.findAll()).thenReturn(expectedSearches);

        List<Search> actualSearches = searchService.read();

        assertEquals(expectedSearches, actualSearches);
    }

    @Test
    public void testCreateSearchAndPages() {
        String name = "TestName";

        List<Word> words = searchService.createSearchAndPages(name);

        assertNotNull(words);
    }
}
