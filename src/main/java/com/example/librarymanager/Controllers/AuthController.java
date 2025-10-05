package com.example.librarymanager.Controllers;

import com.example.librarymanager.Models.User;
import com.example.librarymanager.Security.JWTUtil;
import com.example.librarymanager.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String name = user.getName();
        String password = user.getPassword();

        User foundUser = userService.findByUsername(name)
                .orElse(null);

        if (foundUser == null || !BCrypt.checkpw(password, foundUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(foundUser.getName(), foundUser.isAdmin());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("name", foundUser.getName());
        response.put("isAdmin", foundUser.isAdmin());

        return ResponseEntity.ok(response);
    }
}
