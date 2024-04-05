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
public class PagesServiceImplTest {
    @Mock
    private PagesRepository pagesRepository;

    @InjectMocks
    private PagesServiceImpl pagesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        Pages page = new Pages();

        pagesService.create(page);

        verify(pagesRepository, times(1)).save(page);
    }

    @Test
    public void testDelete() {
        long idToDelete = 1L;

        pagesService.delete(idToDelete);

        verify(pagesRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    public void testUpdate() {
        Pages page = new Pages();

        pagesService.update(page);

        verify(pagesRepository, times(1)).save(page);
    }

    @Test
    public void testRead() {
        when(pagesRepository.findAll()).thenReturn(new ArrayList<>());

        List<Pages> pages = pagesService.read();

        assertNotNull(pages);
    }

    @Test
    public void testGetPagesBySearch() {
        Search search = new Search();
        when(pagesRepository.existingBySearch(search)).thenReturn(new ArrayList<>());

        List<Pages> pages = pagesService.getPagesBySearch(search);

        assertNotNull(pages);
    }
}
