package com.example.librarymanager.Controllers;

import com.example.librarymanager.Middleware.UserNotAdminException;
import com.example.librarymanager.Middleware.UserNotFoundException;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private User getCurrentUser(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new UserNotFoundException();
        }
        return userService.findByUsername(auth.getName())
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(Authentication auth) {
        User user = getCurrentUser(auth);
        if (!user.isAdmin()) throw new UserNotAdminException();
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Integer id, Authentication auth) {
        User user = getCurrentUser(auth);
        if (!user.isAdmin()) {
            throw new UserNotAdminException();
        }
        return userService.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User newUser, Authentication auth, @RequestParam(required = false) Integer adminId) {
        User admin = getCurrentUser(auth);
        if (!admin.isAdmin()) throw new UserNotAdminException();
        return userService.registerUser(newUser.getName(), newUser.getPassword(), newUser.isAdmin(), admin);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User user,
                           Authentication auth, @RequestParam(required = false) Integer adminId) {
        User admin = getCurrentUser(auth);
        if (!admin.isAdmin()) throw new UserNotAdminException();
        return userService.updateUser(id, user, admin);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id, Authentication auth, @RequestParam(required = false) Integer adminId) {
        User admin = getCurrentUser(auth);
        if (!admin.isAdmin()) throw new UserNotAdminException();
        userService.removeUser(id, admin);
    }
}
