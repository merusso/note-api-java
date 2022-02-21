package com.example.noteapi.api;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Data
public class User {
    private Long id;
    @NotEmpty
    private String name;
    private Instant joinDate;
    private List<String> noteLabels;
}
