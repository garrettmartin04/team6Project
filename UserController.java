package com.example.NoteStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private final Set<String> loggedInUsers = new HashSet<>();

    // Create user endpoint
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username,
                                             @RequestParam String email,
                                             @RequestParam String password,
                                             @RequestParam String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            User newUser = new User(username, email, password, userRole);
            users.add(newUser);
            return ResponseEntity.ok("User created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role specified.");
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestParam String username,
                                                         @RequestParam String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.login(password)) {
                loggedInUsers.add(user.getUserID());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login successful for user: " + username);
                response.put("userId", user.getUserID());
                return ResponseEntity.ok(response);
            }
        }
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Login failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestParam String userId) {
        for (User user : users) {
            if (user.getUserID().equals(userId)) {
                if (loggedInUsers.contains(userId)) {
                    user.logout();
                    loggedInUsers.remove(userId);
                    return ResponseEntity.ok("Logout successful for user: " + user.getUsername());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not logged in.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    // Get all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return users;
    }

    // Test endpoint
    @GetMapping("/test")
    public String testEndpoint() {
        return "Server is running.";
    }
}

