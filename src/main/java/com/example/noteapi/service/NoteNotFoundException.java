package com.example.noteapi.service;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(long id) {
        super("Note with ID %s not found".formatted(id));
    }
}
