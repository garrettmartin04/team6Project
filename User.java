package com.example.NoteStream;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

enum UserRole {
    GUEST,
    USER,
    ADMIN
}

public class User {
    private String userID;
    private String username;
    private String email;
    private String password;
    private UserRole role;

    // New fields for friendship management
    private List<User> friends;
    private List<FriendRequest> sentRequests;
    private List<FriendRequest> receivedRequests;

    public User(String username, String email, String password, UserRole role) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;

        // initialize new lists
        this.friends = new ArrayList<>();
        this.sentRequests = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
    }
    // getters/setters
    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public List<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    // Methods to manage friends
    public void addFriend(User friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
        }
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
    }

    // Methods to manage friend requests
    public void sendFriendRequest(FriendRequest request) {
        sentRequests.add(request);
    }

    public void receiveFriendRequest(FriendRequest request) {
        receivedRequests.add(request);
    }

    public void removeSentRequest(FriendRequest request) {
        sentRequests.remove(request);
    }

    public void removeReceivedRequest(FriendRequest request) {
        receivedRequests.remove(request);
    }

    // Login method
    public boolean login(String password) {
        System.out.println("Verifying password for user: " + username);
        if (this.password.equals(password)) {
            System.out.println(username + " has logged in successfully.");
            return true;
        } else {
            System.out.println("Login failed for user " + username);
            return false;
        }
    }

    // Logout method
    public void logout() {
        // Logout logic...
    }
}


