package com.example.librarymanager.Services;

import com.example.librarymanager.Middleware.InvalidCredentialsException;
import com.example.librarymanager.Middleware.UserNotAdminException;
import com.example.librarymanager.Middleware.UserNotFoundException;
import com.example.librarymanager.Models.User;
import com.example.librarymanager.AppDataContext.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(String name, String password, boolean isAdmin, User loggedInUser) {
        if(!userRepository.findAll().isEmpty()){
            if (loggedInUser == null || !loggedInUser.isAdmin()){
                throw new UserNotAdminException();
            }
        } else {
            isAdmin = true; // first user is admin by default
        }

        User newUser = new User(0, name, hashPassword(password), isAdmin);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public void login(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(InvalidCredentialsException::new);

        if (!verifyPassword(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public void removeUser(int userId, User loggedInUser) {
        if (loggedInUser == null || !loggedInUser.isAdmin()){
            throw new UserNotAdminException();
        }

        User existing = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(existing);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(int id, User updatedUser, User loggedInUser) {
        if (loggedInUser == null || !loggedInUser.isAdmin()){
            throw new UserNotAdminException();
        }

        User existing = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        existing.setName(updatedUser.getName());
        existing.setPassword(hashPassword(updatedUser.getPassword()));
        existing.setAdmin(updatedUser.isAdmin());

        return userRepository.save(existing);
    }

    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }
}
