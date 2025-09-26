package com.example.librarymanager.Services;

import com.example.librarymanager.Models.User;
import com.example.librarymanager.AppDataContext.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo){
        this.repo = repo;
    }

    @Override
    public User registerUser(String name, String password, boolean isAdmin, User loggedInUser) {
        if(!repo.findAll().isEmpty()){
            if (loggedInUser == null || !loggedInUser.isAdmin()){
                throw new RuntimeException("Only admins can register users!");
            }
        } else {
            // first user is admin by default
            isAdmin = true;
        }

        User newUser = new User(0, name, hashPassword(password), isAdmin);
        repo.save(newUser);
        return newUser;
    }

    @Override
    public User login(String name, String password) {
        User user = repo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Invalid credentials!"));

        if (!verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        return user;
    }

    @Override
    public List<User> listUsers() {
        return repo.findAll();
    }

    @Override
    public void removeUser(int userId, User loggedInUser) {
        if (loggedInUser == null || !loggedInUser.isAdmin()){
            throw new RuntimeException("Only admins can remove users!");
        }

        User existing = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        repo.delete(existing);
    }

    @Override
    public Optional<User> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public User updateUser(int id, User updatedUser, User loggedInUser) {
        if (loggedInUser == null || !loggedInUser.isAdmin()){
            throw new RuntimeException("Only admins can update users!");
        }

        User existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        existing.setName(updatedUser.getName());
        existing.setPassword(hashPassword(updatedUser.getPassword()));
        existing.setAdmin(updatedUser.isAdmin());

        return repo.save(existing);
    }

    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }
}