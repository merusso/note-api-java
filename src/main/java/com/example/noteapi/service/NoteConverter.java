package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteConverter implements TwoWayConverter<Note, com.example.noteapi.data.Note> {
    @Override
    public com.example.noteapi.data.Note convert(Note source) {
        var result = new com.example.noteapi.data.Note();
        result.id = source.getId();
        result.userId = source.getUserId();
        result.title = source.getTitle();
        result.content = source.getContent();
        result.createdDate = source.getCreatedDate();
        result.updatedDate = source.getUpdatedDate();
        result.labels = source.getLabels();
        return result;
    }

    @Override
    public Note convertReverse(com.example.noteapi.data.Note source) {
        var result = new Note();
        result.setId(source.id);
        result.setUserId(source.userId);
        result.setTitle(source.title);
        result.setContent(source.content);
        result.setCreatedDate(source.createdDate);
        result.setUpdatedDate(source.updatedDate);
        result.setLabels(source.labels);
        return result;
    }
}
