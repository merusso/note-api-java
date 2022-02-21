package com.example.noteapi.service;

import com.example.noteapi.api.User;

public interface UserService {
    User create(User user);
    User get(long id);
    User update(User user);
    void delete(long id);
}
