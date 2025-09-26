package com.example.librarymanager.Services;

import com.example.librarymanager.Models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(String name, String password, boolean admin, User loggedInUser);

    User login(String name, String password);

    List<User> listUsers();

    void removeUser(int userId, User loggedInUser);

    Optional<User> findById(int id);

    User updateUser(int id, User updatedUser, User loggedInUser);
}
