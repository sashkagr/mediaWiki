package org.example.mediawiki.modal;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DescriptionGetApiResponseTest {

    @Test
    void testQueryInitialization() {
        DescriptionGetApiResponse response = new DescriptionGetApiResponse();

        response.setQuery(new DescriptionGetApiQueryResponse());

        assertNotNull(response.getQuery());
    }

    @Test
    void testQueryModification() {
        DescriptionGetApiResponse response = new DescriptionGetApiResponse();

        DescriptionGetApiQueryResponse query = new DescriptionGetApiQueryResponse();
        response.setQuery(query);

        assertNotNull(response.getQuery());
    }

    @Test
    void testQueryInitializationWithNull() {
        DescriptionGetApiResponse response = new DescriptionGetApiResponse();

        assertNull(response.getQuery());
    }
}
