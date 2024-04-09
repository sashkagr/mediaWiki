package org.example.mediawiki.service;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagesServiceImplTest {

    @Mock
    private PagesRepository pagesRepository;

    @InjectMocks
    private PagesServiceImpl pagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPageByPageId() {
        // Create a mock page
        Pages page = new Pages();
        page.setId(1L);
        page.setPageId(100L);

        // Set up repository to return the mock page
        when(pagesRepository.existingByPageId(100L)).thenReturn(page);

        // Test the method
        Pages retrievedPage = pagesService.getPageByPageId(100L);
        assertNotNull(retrievedPage);
        assertEquals(page.getId(), retrievedPage.getId());
        assertEquals(page.getPageId(), retrievedPage.getPageId());
    }

    @Test
    void testGetPageByPageId_NullFromRepository() {
        // Set up repository to return null
        when(pagesRepository.existingByPageId(anyLong())).thenReturn(null);

        // Test the method
        Pages retrievedPage = pagesService.getPageByPageId(100L);
        assertNull(retrievedPage);
    }

    @Test
    void testCreate() {
        Pages page = new Pages();

        // Test the method
        pagesService.create(page);

        verify(pagesRepository, times(1)).save(page);
    }

    @Test
    void testDelete() {
        long idToDelete = 1L;

        pagesService.delete(idToDelete);

        verify(pagesRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void testUpdate() {
        Pages page = new Pages();

        // Test the method
        pagesService.update(page);

        verify(pagesRepository, times(1)).save(page);
    }

    @Test
    void testRead() {
        List<Pages> pagesList = new ArrayList<>();
        when(pagesRepository.findAll()).thenReturn(pagesList);

        // Test the method
        List<Pages> retrievedPages = pagesService.read();
        assertNotNull(retrievedPages);
        assertEquals(pagesList, retrievedPages);
    }

    @Test
    void testGetPagesBySearch() {
        Search search = new Search();
        List<Pages> pagesList = new ArrayList<>();
        when(pagesRepository.existingBySearch(search)).thenReturn(pagesList);

        // Test the method
        List<Pages> retrievedPages = pagesService.getPagesBySearch(search);
        assertNotNull(retrievedPages);
        assertEquals(pagesList, retrievedPages);
    }
}
