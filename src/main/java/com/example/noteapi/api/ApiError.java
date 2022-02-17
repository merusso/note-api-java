package com.example.noteapi.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiError {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
}
