package com.example.librarymanager.Controllers;

import com.example.librarymanager.Middleware.UserNotAdminException;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.Middleware.UserNotFoundException;
import com.example.librarymanager.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Integer id) {
        return userService.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user, @RequestParam Integer adminId) {
        User admin = userService.findById(adminId)
                .orElseThrow(UserNotAdminException::new);
        return userService.registerUser(user.getName(), user.getPassword(), user.isAdmin(), admin);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User user,
                           @RequestParam Integer adminId) {
        User admin = userService.findById(adminId)
                .orElseThrow(UserNotAdminException::new);
        return userService.updateUser(id, user, admin);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id, @RequestParam Integer adminId) {
        User admin = userService.findById(adminId)
                .orElseThrow(UserNotAdminException::new);
        userService.removeUser(id, admin);
    }
}
