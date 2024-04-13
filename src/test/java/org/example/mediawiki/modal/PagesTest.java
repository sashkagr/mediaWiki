package org.example.mediawiki.modal;
import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;



class PagesTest {
    private Pages pages;

    @BeforeEach
    void setUp() {
        pages = new Pages();
    }
    @Test
    void testIdGenerationType() throws NoSuchFieldException {
        // Assuming the field name is 'id' in the Search class
        var idField = Search.class.getDeclaredField("id");
        var generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValueAnnotation);
        assertEquals(GenerationType.IDENTITY, generatedValueAnnotation.strategy());
    }

    @Test
    void testTitleFieldType() {
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
    void testPageIdFieldType() throws NoSuchFieldException {
        // Get the field 'pageId' from the Pages class
        java.lang.reflect.Field pageIdField = Pages.class.getDeclaredField("pageId");

        // Assert that the type of 'pageId' field is long
        assertEquals(long.class, pageIdField.getType());
    }
    @Test
    void testSearchesFieldType() {
        assertNotNull(pages.getSearches());
    }

    @Test
    void testSearchesFetchType() throws NoSuchFieldException {
        // Assuming the first element of pages list is used for testing
        Search search = new Search();
        // Get the fetch type from the @ManyToMany annotation
        FetchType fetchType = Search.class.getDeclaredField("pages")
                .getAnnotation(ManyToMany.class)
                .fetch();

        assertEquals(FetchType.EAGER, fetchType);
    }


    @Test
    void testIdGetterAndSetter() {
        long id = 1L;
        pages.setId(id);
        assertEquals(id, pages.getId());
    }

    @Test
    void testTitleGetterAndSetter() {
        String title = "Test Title";
        pages.setTitle(title);
        assertEquals(title, pages.getTitle());
    }

    @Test
    void testPageIdGetterAndSetter() {
        long pageId = 100L;
        pages.setPageId(pageId);
        assertEquals(pageId, pages.getPageId());
    }

    @Test
    void testSearchesGetterAndSetter() {
        Search search = new Search();
        pages.getSearches().add(search);
        assertEquals(1, pages.getSearches().size());
        assertEquals(search, pages.getSearches().get(0));
    }
    @Test
    void testToString() {
        pages.setId(1);
        pages.setTitle("Test Title");
        assertEquals("Pages(id=1, title=Test Title, pageId=0, searches=[])", pages.toString());
    }

    @Test
    void testJoinTableAnnotation() throws NoSuchFieldException {
        Field searchesField = Pages.class.getDeclaredField("searches");
        JoinTable joinTableAnnotation = searchesField.getAnnotation(JoinTable.class);
        assertNotNull(joinTableAnnotation);
        assertEquals("pages_search", joinTableAnnotation.name());
        assertEquals("page_id", joinTableAnnotation.joinColumns()[0].name());
        assertEquals("search_id", joinTableAnnotation.inverseJoinColumns()[0].name());
    }

    @Test
    void testManyToManyAnnotation() throws NoSuchFieldException {
        Field searchesField = Pages.class.getDeclaredField("searches");
        ManyToMany manyToManyAnnotation = searchesField.getAnnotation(ManyToMany.class);
        assertNotNull(manyToManyAnnotation);
    }

    @Test
    void testJoinColumnAnnotation() throws NoSuchFieldException {
        Field searchesField = Pages.class.getDeclaredField("searches");
        JoinColumn joinColumnAnnotation = searchesField.getAnnotation(JoinColumn.class);
        assertNull(joinColumnAnnotation);
    }

    @Test
    void testSearchesListNotNull() {
        assertNotNull(pages.getSearches());
    }

    @Test
    void testSearchesListAddition() {
        Search search = new Search();
        pages.getSearches().add(search);
        assertTrue(pages.getSearches().contains(search));
    }
}
