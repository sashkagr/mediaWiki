package org.example.mediawiki.modal;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DescriptionGetApiSearchResponseTest {

    @Test
    public void testIdGetterAndSetter() {
        DescriptionGetApiSearchResponse response = new DescriptionGetApiSearchResponse();
        response.setId("12345");
        assertEquals("12345", response.getId());
    }

    @Test
    public void testTitleGetterAndSetter() {
        DescriptionGetApiSearchResponse response = new DescriptionGetApiSearchResponse();
        response.setTitle("Test Title");
        assertEquals("Test Title", response.getTitle());
    }

    @Test
    public void testDescriptionGetterAndSetter() {
        DescriptionGetApiSearchResponse response = new DescriptionGetApiSearchResponse();
        response.setDescription("Test Description");
        assertEquals("Test Description", response.getDescription());
    }
}
