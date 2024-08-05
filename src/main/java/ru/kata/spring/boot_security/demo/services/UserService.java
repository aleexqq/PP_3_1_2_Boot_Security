package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    void deleteUser(User user);

    User getUserById(Long id);

    List<User> allUsers();
}
