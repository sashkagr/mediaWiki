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
    void testCreate() {
        Pages page = new Pages();

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

        pagesService.update(page);

        verify(pagesRepository, times(1)).save(page);
    }

    @Test
    void testRead() {
        when(pagesRepository.findAll()).thenReturn(new ArrayList<>());

        List<Pages> pages = pagesService.read();

        assertNotNull(pages);
    }

    @Test
    void testGetPagesBySearch() {
        Search search = new Search();
        when(pagesRepository.existingBySearch(search)).thenReturn(new ArrayList<>());

        List<Pages> pages = pagesService.getPagesBySearch(search);

        assertNotNull(pages);
    }
}
