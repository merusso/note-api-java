package com.example.noteapi.api;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
public class Note {
    private String id;
    @NotNull
    private String userId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private Instant createdDate;
    private Instant updatedDate;
    private List<String> labels;
}
