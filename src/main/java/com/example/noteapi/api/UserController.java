package com.example.noteapi.api;

import com.example.noteapi.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private UserService userService;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @GetMapping("{id}")
    public User get(@PathVariable String id) {
        return userService.get(id);
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody @Valid User user) {
        user.setId(id);
        return userService.update(user);
    }

    @PatchMapping("{id}")
    public User update(@PathVariable String id, @RequestBody JsonNode json) {
        return userService.patch(id, json);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}
