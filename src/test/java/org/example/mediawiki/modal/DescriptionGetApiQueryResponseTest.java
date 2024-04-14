package org.example.mediawiki.modal;


import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DescriptionGetApiQueryResponseTest {

    @Test
    void testSearchListInitialization() {
        // Создаем объект DescriptionGetApiQueryResponse
        DescriptionGetApiQueryResponse response = new DescriptionGetApiQueryResponse();
        response.setSearch(new ArrayList<>());

        // Проверяем, что список search инициализирован
        assertNotNull(response.getSearch());
    }

    @Test
    void testSearchListModification() {
        // Создаем объект DescriptionGetApiQueryResponse
        DescriptionGetApiQueryResponse response = new DescriptionGetApiQueryResponse();

        // Создаем новый список search
        List<DescriptionGetApiSearchResponse> searchList = new ArrayList<>();
        DescriptionGetApiSearchResponse descriptionGetApiSearchResponse = new DescriptionGetApiSearchResponse();
        descriptionGetApiSearchResponse.setDescription("description");
        descriptionGetApiSearchResponse.setTitle("title");
        DescriptionGetApiSearchResponse descriptionGetApiSearchResponse1 = new DescriptionGetApiSearchResponse();
        descriptionGetApiSearchResponse1.setDescription("description1");
        descriptionGetApiSearchResponse1.setTitle("title1");
        searchList.add(descriptionGetApiSearchResponse);
        searchList.add(descriptionGetApiSearchResponse1);

        // Устанавливаем новый список search в объект response
        response.setSearch(searchList);

        // Проверяем, что список search изменен
        assertEquals(2, response.getSearch().size());
        assertEquals("title", response.getSearch().get(0).getTitle());
        assertEquals("description1", response.getSearch().get(1).getDescription());
    }
}
