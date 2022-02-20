package com.example.noteapi.api;

import com.example.noteapi.json.Base64ToStringConverter;
import com.example.noteapi.json.StringToBase64Converter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonDeserialize(converter = Base64ToStringConverter.class)
    @JsonSerialize(converter = StringToBase64Converter.class)
    private String content;
    private Instant createdDate;
    private Instant updatedDate;
    private List<String> labels;
}
