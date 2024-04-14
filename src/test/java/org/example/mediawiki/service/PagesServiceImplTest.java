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
        // Создаем тестовый идентификатор страницы
        Long pageId = 1L;

        // Создаем фиктивную страницу
        Pages testPage = new Pages();
        testPage.setPageId(pageId);
        testPage.setTitle("Test Page");

        // Устанавливаем ожидаемый результат для mock pagesRepository
        when(pagesRepository.existingByPageId(pageId)).thenReturn(testPage);

        // Вызываем тестируемый метод
        Pages result = pagesService.getPageByPageId(pageId);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(testPage, result);
    }

    @Test
    void testCreate() {
        // Создаем тестовую страницу для создания
        Pages testPage = new Pages();
        testPage.setTitle("Test Page");

        // Вызываем метод create
        pagesService.create(testPage);

        // Проверяем, что метод pagesRepository.save был вызван с тестовой страницей в качестве аргумента
        verify(pagesRepository).save(testPage);
    }

    @Test
    void testUpdate() {
        // Создаем тестовую страницу для обновления
        Pages testPage = new Pages();
        testPage.setId(1L);
        testPage.setTitle("Updated Title");

        // Устанавливаем ожидаемый результат для метода pagesRepository.save
        when(pagesRepository.save(testPage)).thenReturn(testPage);

        // Вызываем метод update
        pagesService.update(testPage);

        // Проверяем, что метод pagesRepository.save был вызван с тестовой страницей в качестве аргумента
        verify(pagesRepository).save(testPage);
    }

    @Test
    void testDelete() {
        // Создаем тестовую страницу
        Pages testPage = new Pages();
        testPage.setId(1L);

        // Устанавливаем ожидаемый результат для метода pagesRepository.deleteById
        doNothing().when(pagesRepository).deleteById(testPage.getId());

        // Вызываем метод delete
        pagesService.delete(testPage.getId());

        // Проверяем, что метод pagesRepository.deleteById был вызван с идентификатором тестовой страницы в качестве аргумента
        verify(pagesRepository).deleteById(testPage.getId());
    }




    @Test
    void read_ReturnsListOfPages() {
        // Mock data setup
        Pages page1 = new Pages();
        page1.setId(1L);
        page1.setTitle("Page 1");
        Pages page2 = new Pages();
        page2.setId(2L);
        page2.setTitle("Page 2");
        List<Pages> mockedPages = List.of(page1, page2);

        // Configure mock repository to return the mocked data
        when(pagesRepository.findAll()).thenReturn(mockedPages);

        // Invoke the method
        List<Pages> result = pagesService.read();

        // Verify that the repository method was called
        verify(pagesRepository).findAll();

        // Assert that the result is not null and matches the mocked data
        assertEquals(mockedPages.size(), result.size());
        assertEquals(mockedPages.get(0).getId(), result.get(0).getId());
        assertEquals(mockedPages.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(mockedPages.get(1).getId(), result.get(1).getId());
        assertEquals(mockedPages.get(1).getTitle(), result.get(1).getTitle());
    }


    @Test
    void getPagesBySearch_ReturnsPagesMatchingSearch() {
        // Mock search object

        Search search = new Search();
        search.setTitle("query");
        // Mock data setup
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

        // Invoke the method
        List<Pages> result = pagesService.getPagesBySearch(search);

        // Verify that the repository method was called
        verify(pagesRepository).existingBySearch(search);

        // Assert that the result matches the mock data
        assertEquals(2, result.size());
        assertEquals("Page 1", result.get(0).getTitle());
        assertEquals("Page 2", result.get(1).getTitle());
    }

    @Test
    void getPagesBySearch_ReturnsEmptyListWhenNoMatches() {
        // Mock search object
        Search search = new Search();
        search.setTitle("query");
        // Mock data setup
        when(pagesRepository.existingBySearch(search)).thenReturn(new ArrayList<>());

        // Invoke the method
        List<Pages> result = pagesService.getPagesBySearch(search);

        // Verify that the repository method was called
        verify(pagesRepository).existingBySearch(search);

        // Assert that the result is an empty list
        assertEquals(0, result.size());
    }

}
