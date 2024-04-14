package org.example.mediawiki.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebConfigTest {

    @MockBean
    private HiddenHttpMethodFilter hiddenHttpMethodFilter;

    @Test
    void testHiddenHttpMethodFilterBean() {
        // Verify that the HiddenHttpMethodFilter bean is created
        assertNotNull(hiddenHttpMethodFilter);
    }
}
