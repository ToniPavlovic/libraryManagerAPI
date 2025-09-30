package com.example.librarymanager;

import com.example.librarymanager.Middleware.*;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.AppDataContext.UserRepository;
import com.example.librarymanager.Services.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepo = mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepo);

    @Test
    void login_ShouldThrowInvalidCredentialsException_WhenUserDoesNotExist() {
        when(userRepo.findByName("TestingUser")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> userService.login("TestingUser", "password"));
    }

    @Test
    void login_ShouldThrowInvalidCredentialsException_WhenPasswordWrong() {
        User user = new User(1, "Toni", UserServiceImpl.hashPassword("correctPassword"), false);
        when(userRepo.findByName("Toni")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class,
                () -> userService.login("Toni", "wrongPassword"));
    }

    @Test
    void removeUser_ShouldThrowUserNotAdminException_WhenUserNotAdmin() {
        User normalUser = new User(2, "Bob", "pass", false);
        assertThrows(UserNotAdminException.class,
                () -> userService.removeUser(1, normalUser));
    }

    @Test
    void removeUser_ShouldThrowUserNotFoundException_WhenUserMissing() {
        User admin = new User(1, "Admin", "pass", true);
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.removeUser(99, admin));
    }
}
