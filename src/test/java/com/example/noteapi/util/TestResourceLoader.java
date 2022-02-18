package com.example.noteapi.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public class TestResourceLoader {
    public static byte[] loadAsBytes(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            File file = resource.getFile();
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String loadAsString(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            File file = resource.getFile();
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
