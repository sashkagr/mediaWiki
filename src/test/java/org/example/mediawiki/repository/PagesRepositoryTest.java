package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagesRepositoryTest {

    @Mock
    private PagesRepository pagesRepository;

    @Test
    void testExistingByPageId() {
        Long pageId = 1L;
        Pages page = new Pages();
        when(pagesRepository.existingByPageId(anyLong())).thenReturn(page);

        Pages result = pagesRepository.existingByPageId(pageId);

        assertEquals(page, result);
        verify(pagesRepository, times(1)).existingByPageId(pageId);
    }

    @Test
    void testExistingBySearch() {
        Search search = new Search();
        List<Pages> pages = new ArrayList<>();
        when(pagesRepository.existingBySearch(any(Search.class))).thenReturn(pages);

        List<Pages> result = pagesRepository.existingBySearch(search);

        assertEquals(pages, result);
        verify(pagesRepository, times(1)).existingBySearch(search);
    }
}