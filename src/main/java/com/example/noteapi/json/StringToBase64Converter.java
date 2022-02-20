package com.example.noteapi.json;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringToBase64Converter extends StdConverter<String, String> {

    @Override
    public String convert(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
