package com.example.noteapi.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ApiError {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private List<ValidationErrorItem> validationErrors;
}
