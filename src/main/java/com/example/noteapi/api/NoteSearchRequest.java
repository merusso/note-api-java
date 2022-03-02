package com.example.noteapi.api;

import lombok.Data;

@Data
public class NoteSearchRequest {
    private String userId;
    private String title;
    private String label;
    private int pageSize = 20;
}
