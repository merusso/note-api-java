package com.example.noteapi.json;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64ToStringConverter extends StdConverter<String, String> {

    @Override
    public String convert(String value) {
        byte[] decoded = Base64.getDecoder().decode(value);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
