package com.example.NoteStream;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private List<User> users = new ArrayList<>();
    private Set<String> loggedInUsers = new HashSet<>();

    // Create user endpoint
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String email,
                                             @RequestParam String password, @RequestParam String role) {
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
    public ResponseEntity<Map<String, String>> loginUser(@RequestParam String username, @RequestParam String password) {
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
    @RestController
    @RequestMapping("/flashcards")
    public class FlashcardController {

        private List<FlashcardSet> flashcardSets = new ArrayList<>();

        // Create Flashcard Set
        @PostMapping("/create")
        public ResponseEntity<Map<String, String>> createFlashcardSet(@RequestBody Map<String, Object> request) {
            String title = (String) request.get("title");
            List<Map<String, String>> flashcardList = (List<Map<String, String>>) request.get("flashcards");
            List<FlashcardSet.Flashcard> flashcards = new ArrayList<>();

            for (Map<String, String> flashcardData : flashcardList) {
                String question = flashcardData.get("question");
                String answer = flashcardData.get("answer");
                flashcards.add(new FlashcardSet.Flashcard(question, answer));
            }

            FlashcardSet newSet = new FlashcardSet(title, flashcards);
            flashcardSets.add(newSet);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Flashcard set created successfully with title: " + title);
            return ResponseEntity.ok(response);
        }

        // Study Flashcards
        @GetMapping("/study")
        public ResponseEntity<Map<String, Object>> studyFlashcards(@RequestParam String setId) {
            FlashcardSet setToStudy = flashcardSets.stream()
                .filter(set -> set.getId().equals(setId))
                .findFirst()
                .orElse(null);

            if (setToStudy == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Flashcard set not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Studying flashcard set: " + setToStudy.getTitle());
            response.put("flashcards", setToStudy.getFlashcards());
            return ResponseEntity.ok(response);
        }

    public class FlashcardSet {
        private String id;
        private String title;
        private List<Flashcard> flashcards;

        public FlashcardSet(String title, List<Flashcard> flashcards) {
            this.id = UUID.randomUUID().toString();
            this.title = title;
            this.flashcards = flashcards;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public List<Flashcard> getFlashcards() {
            return flashcards;
        }

        public static class Flashcard {
            private String question;
            private String answer;

            public Flashcard(String question, String answer) {
                this.question = question;
                this.answer = answer;
            }

            public String getQuestion() {
                return question;
            }

            public String getAnswer() {
                return answer;
            }
        }
    }
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/").setViewName("forward:/index.html");
        }
    }
    @Controller
    public class FlashcardPageController {

        @GetMapping("/create-flashcards")
        public String createFlashcardsPage() {
            return "create-flashcards"; // This will load create-flashcards.html from the static folder
        }

        @GetMapping("/study-flashcards")
        public String studyFlashcardsPage() {
            return "study-flashcards"; // This will load study-flashcards.html from the static folder
        }
    }
    @GetMapping("/all-sets")
    public ResponseEntity<List<Map<String, String>>> getAllFlashcardSets() {
        List<Map<String, String>> sets = new ArrayList<>();
        for (FlashcardSet set : flashcardSets) {
            Map<String, String> setInfo = new HashMap<>();
            setInfo.put("id", set.getId());
            setInfo.put("title", set.getTitle());
            sets.add(setInfo);
        }
        return ResponseEntity.ok(sets);
    }
  }
}
