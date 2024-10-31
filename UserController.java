package com.example.NoteStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    private List<User> users = new ArrayList<>();
   


    // Example endpoint to create a new user
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String email,
                                             @RequestParam String password, @RequestParam String role) {
        try {
            // Convert the role string to a UserRole enum
            UserRole userRole = UserRole.valueOf(role.toUpperCase());

            // Create a new User object
            User newUser = new User(username, email, password, userRole);

            // Add the new User to the users list
            users.add(newUser);

            return ResponseEntity.ok("User created successfully!");
        } catch (IllegalArgumentException e) {
            // Handle the case where the role is invalid
            return ResponseEntity.badRequest().body("Invalid role specified.");
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during user creation.");
        }
    }




    // Example endpoint for login
    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.login(password)) {
                return "Login successful for user: " + username;
            }
        }
        return "Login failed";
    }

    // Example endpoint to get all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return users;
    }
    
    @GetMapping("/user/test")
    public String testEndpoint() {
        return "This is a test endpoint to confirm the application is running.";
    }

}
