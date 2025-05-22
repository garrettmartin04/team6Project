package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private final Set<String> loggedInUsers = new HashSet<>();
    private final Map<String, User> userIdMap = new HashMap<>(); // Map for quick user lookup by ID
    private final Map<String, User> usernameMap = new HashMap<>(); // Map for quick user lookup by username

    // Create user endpoint
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");
        String roleStr = payload.get("role");

        if (username == null || email == null || password == null || roleStr == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        if (usernameMap.containsKey(username)) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        try {
            UserRole userRole = UserRole.valueOf(roleStr.toUpperCase());
            User newUser = new User(username, email, password, userRole);
            users.add(newUser);
            userIdMap.put(newUser.getUserID(), newUser);
            usernameMap.put(newUser.getUsername(), newUser);
            return ResponseEntity.ok("User created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role specified.");
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        System.out.println("Attempting login with username: " + username);

        if (username == null || password == null) {
            System.out.println("Username or password is null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Username and password are required."));
        }

        User user = usernameMap.get(username);

        if (user != null) {
            System.out.println("User found: " + user.getUsername());
            boolean loginSuccess = user.login(password);
            if (loginSuccess) {
                loggedInUsers.add(user.getUserID());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login successful for user: " + username);
                response.put("userId", user.getUserID());
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Password mismatch for user: " + username);
            }
        } else {
            System.out.println("User not found: " + username);
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Login failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
 // Get user by username
    @GetMapping("/getByUsername")
    public ResponseEntity<?> getUserByUsernameEndpoint(@RequestParam String username) {
        User user = usernameMap.get(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userID", user.getUserID());
        userInfo.put("username", user.getUsername());
        return ResponseEntity.ok(userInfo);
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestParam String userId) {
        User user = userIdMap.get(userId);
        if (user != null) {
            if (loggedInUsers.contains(userId)) {
                user.logout();
                loggedInUsers.remove(userId);
                return ResponseEntity.ok("Logout successful for user: " + user.getUsername());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not logged in.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    // Get all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return users;
    }

    // Send friend request
    @PostMapping("/{userId}/friends/request")
    public ResponseEntity<String> sendFriendRequest(@PathVariable String userId,
                                                    @RequestParam String receiverUsername) {
        User sender = userIdMap.get(userId);
        User receiver = usernameMap.get(receiverUsername);

        if (sender == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sender not found.");
        }
        if (receiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not found.");
        }
        if (sender.getFriends().contains(receiver)) {
            return ResponseEntity.badRequest().body("You are already friends with this user.");
        }

        // Check if a friend request has already been sent
        boolean requestExists = sender.getSentRequests().stream()
                .anyMatch(req -> req.getReceiver().equals(receiver) && !req.isAccepted());

        if (requestExists) {
            return ResponseEntity.badRequest().body("Friend request already sent.");
        }

        // Create and send the friend request
        FriendRequest request = new FriendRequest(sender, receiver);
        sender.sendFriendRequest(request);
        receiver.receiveFriendRequest(request);

        return ResponseEntity.ok("Friend request sent to " + receiverUsername + ".");
    }

    // Accept friend request
    @PostMapping("/{userId}/friends/accept")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable String userId,
                                                      @RequestParam String requestId) {
        User receiver = userIdMap.get(userId);

        if (receiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Optional<FriendRequest> optionalRequest = receiver.getReceivedRequests().stream()
                .filter(req -> req.getRequestId().equals(requestId))
                .findFirst();

        if (!optionalRequest.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found.");
        }

        FriendRequest request = optionalRequest.get();
        request.accept();

        User sender = request.getSender();

        // Add each other as friends
        receiver.addFriend(sender);
        sender.addFriend(receiver);

        // Remove the friend request from both users
        receiver.removeReceivedRequest(request);
        sender.removeSentRequest(request);

        return ResponseEntity.ok("Friend request accepted. You are now friends with " + sender.getUsername() + ".");
    }

    // Delete friend
    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable String userId,
                                               @PathVariable String friendId) {
        User user = userIdMap.get(userId);
        User friend = userIdMap.get(friendId);

        if (user == null || friend == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or friend not found.");
        }

        if (!user.getFriends().contains(friend)) {
            return ResponseEntity.badRequest().body("You are not friends with this user.");
        }

        user.removeFriend(friend);
        friend.removeFriend(user);

        return ResponseEntity.ok("You are no longer friends with " + friend.getUsername() + ".");
    }

    // Get friend list
    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable String userId) {
        User user = userIdMap.get(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        List<Map<String, String>> friendList = new ArrayList<>();
        for (User friend : user.getFriends()) {
            Map<String, String> friendInfo = new HashMap<>();
            friendInfo.put("userId", friend.getUserID());
            friendInfo.put("username", friend.getUsername());
            friendList.add(friendInfo);
        }

        return ResponseEntity.ok(friendList);
    }

    // Get received friend requests
    @GetMapping("/{userId}/friends/requests")
    public ResponseEntity<?> getReceivedFriendRequests(@PathVariable String userId) {
        User user = userIdMap.get(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        List<Map<String, String>> requestList = new ArrayList<>();
        for (FriendRequest request : user.getReceivedRequests()) {
            if (!request.isAccepted()) {
                Map<String, String> requestInfo = new HashMap<>();
                requestInfo.put("requestId", request.getRequestId());
                requestInfo.put("senderId", request.getSender().getUserID());
                requestInfo.put("senderUsername", request.getSender().getUsername());
                requestList.add(requestInfo);
            }
        }

        return ResponseEntity.ok(requestList);
    }

    // Helper methods for NoteController
    public User getUserById(String userId) {
        return userIdMap.get(userId);
    }

    public User getUserByUsername(String username) {
        return usernameMap.get(username);
    }

    // Test endpoint
    @GetMapping("/test")
    public String testEndpoint() {
        return "Server is running.";
    }
}


