package com.example.noteapi.service;

import com.example.noteapi.api.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements TwoWayConverter<User, com.example.noteapi.data.User> {
    @Override
    public com.example.noteapi.data.User convert(User source) {
        var result = new com.example.noteapi.data.User();
        result.id = source.getId();
        result.name = source.getName();
        result.joinDate = source.getJoinDate();
        result.noteLabels = source.getNoteLabels();
        return result;
    }

    @Override
    public User convertReverse(com.example.noteapi.data.User source) {
        var result = new User();
        result.setId(source.id);
        result.setName(source.name);
        result.setJoinDate(source.joinDate);
        result.setNoteLabels(source.noteLabels);
        return result;
    }
}
