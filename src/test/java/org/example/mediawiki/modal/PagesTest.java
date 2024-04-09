package org.example.mediawiki.modal;
import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;



public class PagesTest {
    private Pages pages;

    @BeforeEach
    public void setUp() {
        pages = new Pages();
    }
    @Test
    public void testIdGenerationType() throws NoSuchFieldException {
        // Assuming the field name is 'id' in the Search class
        var idField = Search.class.getDeclaredField("id");
        var generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValueAnnotation);
        assertEquals(GenerationType.IDENTITY, generatedValueAnnotation.strategy());
    }

    @Test
    public void testTitleFieldType() {
        // Assuming pages is an instance of Pages class
        String title = "Sample Title"; // Assigning a sample title for testing

        // Set the title using reflection
        try {
            Field titleField = Pages.class.getDeclaredField("title");
            titleField.setAccessible(true); // Enable access to private field
            titleField.set(pages, title); // Set the title field with the sample title
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail("Failed to access or set title field via reflection");
        }

        // Now perform the assertion on the title field type
        assertEquals(String.class, pages.getTitle().getClass());
    }


    @Test
    public void testPageIdFieldType() throws NoSuchFieldException {
        // Get the field 'pageId' from the Pages class
        java.lang.reflect.Field pageIdField = Pages.class.getDeclaredField("pageId");

        // Assert that the type of 'pageId' field is long
        assertEquals(long.class, pageIdField.getType());
    }
    @Test
    public void testSearchesFieldType() {
        assertNotNull(pages.getSearches());
    }

    @Test
    public void testSearchesFetchType() throws NoSuchFieldException {
        // Assuming the first element of pages list is used for testing
        Search search = new Search();
        // Get the fetch type from the @ManyToMany annotation
        FetchType fetchType = Search.class.getDeclaredField("pages")
                .getAnnotation(ManyToMany.class)
                .fetch();

        assertEquals(FetchType.EAGER, fetchType);
    }


    @Test
    public void testIdGetterAndSetter() {
        long id = 1L;
        pages.setId(id);
        assertEquals(id, pages.getId());
    }

    @Test
    public void testTitleGetterAndSetter() {
        String title = "Test Title";
        pages.setTitle(title);
        assertEquals(title, pages.getTitle());
    }

    @Test
    public void testPageIdGetterAndSetter() {
        long pageId = 100L;
        pages.setPageId(pageId);
        assertEquals(pageId, pages.getPageId());
    }

    @Test
    public void testSearchesGetterAndSetter() {
        Search search = new Search();
        pages.getSearches().add(search);
        assertEquals(1, pages.getSearches().size());
        assertEquals(search, pages.getSearches().get(0));
    }
}
