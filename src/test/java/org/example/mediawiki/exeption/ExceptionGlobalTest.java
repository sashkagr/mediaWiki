package org.example.mediawiki.exeption;

import org.apache.coyote.BadRequestException;
import org.example.mediawiki.exception.ExceptionGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class ExceptionGlobalTest {


    @Mock
    private ChangeSetPersister.NotFoundException notFoundException;
    @InjectMocks
    private ExceptionGlobal exceptionGlobal;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testHandleBadRequestException() {
        String errorMessage = "Bad request error message";

        ResponseEntity<String> responseEntity = exceptionGlobal.handleBadRequestException(new BadRequestException(errorMessage));

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }


    @Test
     void testHandleNotFoundException() {
        String errorMessage = "Not found message";
        when(notFoundException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<String> responseEntity = exceptionGlobal.handleNotFoundException(notFoundException);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }


    @Test
     void testHandleGlobalException() {
        String errorMessage = "Internal server error message";

        ResponseEntity<String> responseEntity = exceptionGlobal.handleGlobalException(new Exception(errorMessage));

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }
}
