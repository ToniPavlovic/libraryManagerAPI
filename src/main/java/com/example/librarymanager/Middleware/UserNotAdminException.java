package com.example.librarymanager.Middleware;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAdminException extends RuntimeException {
    public UserNotAdminException() {
        super("User is not admin! Please log in as an admin or contact one to perform this action!");
    }
}
