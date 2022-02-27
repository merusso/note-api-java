package com.example.noteapi.service;

import com.example.noteapi.api.User;
import com.example.noteapi.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private UserConverter converter;

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
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        var dataUser = converter.convert(user);
        dataUser = repository.save(dataUser);
        return converter.convertReverse(dataUser);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
