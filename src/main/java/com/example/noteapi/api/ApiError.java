package com.example.noteapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationErrorItem> validationErrors;
}
