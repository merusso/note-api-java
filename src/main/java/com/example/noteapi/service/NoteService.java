package com.example.noteapi.service;

import com.example.noteapi.api.Note;

public interface NoteService {
    Note create(Note note);
    Note get(long id);
    Note update(Note note);
    void delete(long id);
}
