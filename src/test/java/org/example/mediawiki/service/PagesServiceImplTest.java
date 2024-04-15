package org.example.mediawiki.service;

import org.example.mediawiki.cache.Cache;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagesServiceImplTest {

    @Mock
    private PagesRepository pagesRepository;

    @InjectMocks
    private PagesServiceImpl pagesService;

    @Mock
    private Cache cache = new Cache();

    @Test
    void testGetPageByPageId() {
        Long pageId = 1L;

        Pages testPage = new Pages();
        testPage.setPageId(pageId);
        testPage.setTitle("Test Page");

        when(pagesRepository.existingByPageId(pageId)).thenReturn(testPage);

        Pages result = pagesService.getPageByPageId(pageId);

        assertEquals(testPage, result);
    }

    @Test
    void testCreate() {
        Pages testPage = new Pages();
        testPage.setTitle("Test Page");

        pagesService.create(testPage);

        verify(pagesRepository).save(testPage);
    }

    @Test
    void testUpdate() {
        Pages testPage = new Pages();
        testPage.setId(1L);
        testPage.setTitle("Updated Title");

        when(pagesRepository.save(testPage)).thenReturn(testPage);

        pagesService.update(testPage);

        verify(pagesRepository).save(testPage);
    }

    @Test
    void testDelete() {
        Pages testPage = new Pages();
        testPage.setId(1L);

        doNothing().when(pagesRepository).deleteById(testPage.getId());

        pagesService.delete(testPage.getId());

        verify(pagesRepository).deleteById(testPage.getId());
    }

    @Test
    void read_ReturnsListOfPages() {
        Pages page1 = new Pages();
        page1.setId(1L);
        page1.setTitle("Page 1");
        Pages page2 = new Pages();
        page2.setId(2L);
        page2.setTitle("Page 2");
        List<Pages> mockedPages = List.of(page1, page2);

        when(pagesRepository.findAll()).thenReturn(mockedPages);

        List<Pages> result = pagesService.read();

        verify(pagesRepository).findAll();

        assertEquals(mockedPages.size(), result.size());
        assertEquals(mockedPages.get(0).getId(), result.get(0).getId());
        assertEquals(mockedPages.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(mockedPages.get(1).getId(), result.get(1).getId());
        assertEquals(mockedPages.get(1).getTitle(), result.get(1).getTitle());
    }


    @Test
    void getPagesBySearch_ReturnsPagesMatchingSearch() {
        Search search = new Search();
        search.setTitle("query");
        List<Pages> mockPages = new ArrayList<>();
        Pages page1 = new Pages();
        page1.setTitle("Page 1");
        page1.setId(1L);
        page1.getSearches().add(search);
        Pages page2 = new Pages();
        page2.setTitle("Page 2");
        page2.setId(2L);
        mockPages.add(page1);
        mockPages.add(page2);
        when(pagesRepository.existingBySearch(search)).thenReturn(mockPages);

        List<Pages> result = pagesService.getPagesBySearch(search);

        verify(pagesRepository).existingBySearch(search);

        assertEquals(2, result.size());
        assertEquals("Page 1", result.get(0).getTitle());
        assertEquals("Page 2", result.get(1).getTitle());
    }

    @Test
    void getPagesBySearch_ReturnsEmptyListWhenNoMatches() {
        Search search = new Search();
        search.setTitle("query");
        when(pagesRepository.existingBySearch(search)).thenReturn(new ArrayList<>());

        List<Pages> result = pagesService.getPagesBySearch(search);

        verify(pagesRepository).existingBySearch(search);

        assertEquals(0, result.size());
    }

}
