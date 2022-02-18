package com.example.noteapi.api;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Note {
    private Long id;
    private Long userId;
    private String title;
    private byte[] content;
    private Instant createdDate;
    private Instant updatedDate;
    private List<String> labels;
}
