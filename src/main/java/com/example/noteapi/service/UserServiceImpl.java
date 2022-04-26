package com.example.noteapi.service;

import com.example.noteapi.api.User;
import com.example.noteapi.data.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Optional;

@Component
@Profile("live")
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private UserConverter converter;
    private ObjectMapper objectMapper;

    @Override
    public User create(User user) {
        var dataUser = converter.convert(user);
        dataUser.id = null;
        dataUser.joinDate = Optional.ofNullable(dataUser.joinDate)
            .orElseGet(Instant::now);
        dataUser = repository.save(dataUser);
        return converter.convertReverse(dataUser);
    }

    @Override
    public User get(String id) {
        return repository.findById(id)
            .map(converter::convertReverse)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User update(User user) {
        String id = user.getId();
        assertUserExists(id);
        var dataUser = converter.convert(user);
        dataUser = repository.save(dataUser);
        return converter.convertReverse(dataUser);
    }

    @Override
    public User patch(String id, JsonNode json) {
        assertUserExists(id);

        User user = get(id);
        try {
            User merged = objectMapper.readerForUpdating(user).readValue(json, User.class);
            return update(merged);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(String id) {
        assertUserExists(id);
        repository.deleteById(id);
    }

    private void assertUserExists(String id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
    }
}
