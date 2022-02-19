package com.example.noteapi.api;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidationErrorItemTest {
    @ParameterizedTest
    @CsvSource({
        "a,a, a,a,  0",
        "a,a, b,a, -1",
        "b,a, a,a,  1",
        "a,a, a,b, -1",
        "a,b, a,a,  1",
        " , ,  , ,  0",
        "a,a,  , , -1",
        " , , a,a,  1",
    })
    void compareTo(String field1, String message1, String field2, String message2, int result) {
        ValidationErrorItem error1 = new ValidationErrorItem(field1, message1);
        ValidationErrorItem error2 = new ValidationErrorItem(field2, message2);
        assertEquals(result, error1.compareTo(error2));
    }
}
