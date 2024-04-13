package org.example.mediawiki.modal;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DescriptionGetApiResponseTest {

    @Test
    void testQueryInitialization() {
        // Создаем объект DescriptionGetApiResponse
        DescriptionGetApiResponse response = new DescriptionGetApiResponse();

        // Инициализируем объект query
        response.setQuery(new DescriptionGetApiQueryResponse());

        // Проверяем, что объект query инициализирован
        assertNotNull(response.getQuery());
    }

    @Test
    void testQueryModification() {
        // Создаем объект DescriptionGetApiResponse
        DescriptionGetApiResponse response = new DescriptionGetApiResponse();

        // Инициализируем объект query
        DescriptionGetApiQueryResponse query = new DescriptionGetApiQueryResponse();
        response.setQuery(query);

        // Модифицируем объект query
        // Тут ваш тест на модификацию объекта query

        // Пример теста на проверку изменений в объекте query
        assertNotNull(response.getQuery());
        // Здесь проверка других изменений в объекте query
    }

    @Test
    void testQueryInitializationWithNull() {
        // Создаем объект DescriptionGetApiResponse
        DescriptionGetApiResponse response = new DescriptionGetApiResponse();

        // Проверяем, что объект query инициализирован как null
        assertNull(response.getQuery());
    }
}
