package org.example.mediawiki.modal;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DescriptionGetApiSearchResponseTest {

    @Test
    void testIdGetterAndSetter() {
        DescriptionGetApiSearchResponse response = new DescriptionGetApiSearchResponse();
        response.setId("12345");
        assertEquals("12345", response.getId());
    }

    @Test
    void testTitleGetterAndSetter() {
        DescriptionGetApiSearchResponse response = new DescriptionGetApiSearchResponse();
        response.setTitle("Test Title");
        assertEquals("Test Title", response.getTitle());
    }

    @Test
    void testDescriptionGetterAndSetter() {
        DescriptionGetApiSearchResponse response = new DescriptionGetApiSearchResponse();
        response.setDescription("Test Description");
        assertEquals("Test Description", response.getDescription());
    }
}
