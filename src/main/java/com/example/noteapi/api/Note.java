package com.example.noteapi.api;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
public class Note {
    private Long id;
    @NotNull
    private Long userId;
    @NotEmpty
    private String title;
    @NotNull
    private byte[] content;
    private Instant createdDate;
    private Instant updatedDate;
    private List<String> labels;
}
