package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.api.NoteSearchRequest;
import com.example.noteapi.api.PageResponse;

public interface NoteService {
    Note create(Note note);
    Note get(String id);
    Note update(Note note);
    void delete(String id);
    PageResponse<Note> search(NoteSearchRequest request);
}
