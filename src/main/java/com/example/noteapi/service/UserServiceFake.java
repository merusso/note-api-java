package com.example.noteapi.service;

import com.example.noteapi.api.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserServiceFake implements UserService {

    private AtomicLong idGenerator = new AtomicLong();
    private HashMap<Long, User> users = new HashMap<>();

    {
        Instant date = ZonedDateTime.of(
                LocalDate.of(2022, 2, 18),
                LocalTime.NOON,
                ZoneId.of("US/Central"))
            .toInstant();
        User user = new User();
        user.setId(1L);
        user.setName("ndrake");
        user.setJoinDate(date);
        user.setNoteLabels(List.of("favorites", "vacations"));
        users.put(user.getId(), user);
    }

    @Override
    public User create(User user) {
        user.setId(idGenerator.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(long id) {
        if (!users.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        return users.get(id);
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (!users.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        users.put(id, user);
        return user;
    }

    @Override
    public void delete(long id) {
        if (!users.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        users.remove(id);
    }
}
