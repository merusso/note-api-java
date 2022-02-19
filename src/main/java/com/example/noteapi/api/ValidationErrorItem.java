package com.example.noteapi.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.Objects;

@AllArgsConstructor
@Getter
@ToString
public class ValidationErrorItem implements Comparable<ValidationErrorItem> {
    private String field;
    private String message;

    /**
     * Comparison using null-safe case-insensitive string comparison.
     * Sort first by field, then message.
     */
    @Override
    public int compareTo(ValidationErrorItem o) {
        Comparator<String> nullSafeComparator = Comparator.nullsLast(String::compareToIgnoreCase);
        return Comparator.comparing(ValidationErrorItem::getField, nullSafeComparator)
            .thenComparing(ValidationErrorItem::getMessage, nullSafeComparator)
            .compare(this, o);
    }
}
