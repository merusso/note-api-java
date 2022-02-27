package com.example.noteapi.data;

import java.time.Instant;
import java.util.List;

public class Note {
    public String id;
    public String userId;
    public String title;
    public String content;
    public Instant createdDate;
    public Instant updatedDate;
    public List<String> labels;
}
