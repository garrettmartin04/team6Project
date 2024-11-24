package com.example.NoteStream;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByUserID(String userID);
    Optional<User> findByUsername(String username);
    List<User> findAll();
}