package org.example.mediawiki.service;

import org.example.mediawiki.cache.Cache;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PagesServiceImplTest {

    @Mock
    private PagesRepository pagesRepository;

    @Mock
    private Cache cache;

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
    void testDelete_PageInCache() {
        long idToDelete = 1L;
        List<Pages> pagesInCache = new ArrayList<>();
        Pages page = new Pages();
        page.setId(idToDelete);
        pagesInCache.add(page);

        when(cache.getCache()).thenReturn(Collections.singletonMap(Long.toString(idToDelete), pagesInCache));

        pagesService.delete(idToDelete);

        verify(cache, times(1)).remove(Long.toString(idToDelete)); // Verify cache.remove() is called
        verify(cache, times(1)).put(anyString(), anyList()); // Ensure cache.put() is called
        verify(pagesRepository, times(1)).deleteById(idToDelete); // Verify repository method call
    }

    @Test
    void testDelete_PageNotInCache() {
        long idToDelete = 1L;
        when(cache.getCache()).thenReturn(Collections.emptyMap());

        pagesService.delete(idToDelete);

        verify(pagesRepository, times(1)).deleteById(idToDelete);
        verify(cache, never()).remove(any());
    }

    @Test
    void testUpdate_PageInCache() {
        Pages page = new Pages();
        long idToUpdate = 1L;
        page.setId(idToUpdate);

        List<Pages> pagesInCache = new ArrayList<>();
        pagesInCache.add(page);

        when(cache.getCache()).thenReturn(Collections.singletonMap(Long.toString(idToUpdate), pagesInCache));

        pagesService.update(page);

        verify(pagesRepository, times(1)).save(page);
        verify(cache, times(1)).remove(Long.toString(idToUpdate));
    }

    @Test
    void testUpdate_PageNotInCache() {
        Pages page = new Pages();
        long idToUpdate = 1L;
        page.setId(idToUpdate);

        when(cache.getCache()).thenReturn(Collections.emptyMap());

        pagesService.update(page);

        verify(pagesRepository, times(1)).save(page);
        verify(cache, never()).remove(any());
    }

    @Test
    void testRead() {
        // Setup data
        List<Pages> pagesList = new ArrayList<>();
        Pages page1 = new Pages();
        page1.setId(1L);
        pagesList.add(page1);

        Pages page2 = new Pages();
        page2.setId(2L);
        pagesList.add(page2);

        when(pagesRepository.findAll()).thenReturn(pagesList);
        when(cache.get(anyString())).thenReturn(null);

        // Test the method
        List<Pages> retrievedPages = pagesService.read();
        assertNotNull(retrievedPages);
        assertEquals(pagesList, retrievedPages);

        // Verify cache interactions
        verify(cache, times(2)).get(anyString());
        verify(cache, times(2)).remove(anyString());
        verify(cache, times(2)).put(anyString(), any(List.class));
    }

    @Test
    void testGetPagesBySearch() {
        Search search = new Search();
        List<Pages> pagesList = new ArrayList<>();
        when(pagesRepository.existingBySearch(search)).thenReturn(pagesList);
        when(cache.get(anyString())).thenReturn(null);

        // Test the method
        List<Pages> retrievedPages = pagesService.getPagesBySearch(search);
        assertNotNull(retrievedPages);
        assertEquals(pagesList, retrievedPages);

        // Verify cache interactions
        verify(cache, times(1)).get(anyString());
        verify(cache, times(1)).remove(anyString());
        verify(cache, times(pagesList.size())).put(anyString(), any(List.class));
    }
}
