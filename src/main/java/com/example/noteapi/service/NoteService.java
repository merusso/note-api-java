package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.api.NoteSearchRequest;
import com.example.noteapi.api.PageResponse;
import com.fasterxml.jackson.databind.JsonNode;

public interface NoteService {
    Note create(Note note);
    Note get(String id);
    Note update(Note note);
    Note patch(String id, JsonNode json);
    void delete(String id);
    PageResponse<Note> search(NoteSearchRequest request);
}
