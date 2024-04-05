package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Search;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchRepositoryTest {

    @Mock
    private SearchRepository searchRepository;

    @Test
    public void testExistingByTitle() {
        String title = "Example Title";
        Search search = new Search();
        when(searchRepository.existingByTitle(any(String.class))).thenReturn(search);

        Search result = searchRepository.existingByTitle(title);

        assertEquals(search, result);
        verify(searchRepository, times(1)).existingByTitle(title);
    }

    @Test
    public void testExistingById() {
        Long id = 1L;
        Search search = new Search();
        when(searchRepository.existingById(anyLong())).thenReturn(search);

        Search result = searchRepository.existingById(id);

        assertEquals(search, result);
        verify(searchRepository, times(1)).existingById(id);
    }
}
