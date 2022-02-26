package com.example.noteapi.service;

import com.example.noteapi.api.User;

public interface UserService {
    User create(User user);
    User get(String id);
    User update(User user);
    void delete(String id);
}
