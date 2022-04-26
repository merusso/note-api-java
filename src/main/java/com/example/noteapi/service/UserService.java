package com.example.noteapi.service;

import com.example.noteapi.api.User;
import com.fasterxml.jackson.databind.JsonNode;

public interface UserService {
    User create(User user);
    User get(String id);
    User update(User user);
    User patch(String id, JsonNode json);
    void delete(String id);
}
