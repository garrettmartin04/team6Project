package com.example.NoteStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


 // enum to represent the role of a user.
 
enum UserRole {
    GUEST,
    USER,
    ADMIN
}


 // user 
 
public class User {

    // Attributes
    public String userID;
    public String username;
    public String email;
    private String password;
    public UserRole role;
    private List<Task> assignedTasks;

    
     //constructor to initialize a new user.

    public User(String username, String email, String password, UserRole role) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password; 
        this.role = role;
        this.assignedTasks = new ArrayList<>();
    }

    
    class Task {
        private String taskID;
        private String description;

        public Task(String description) {
            this.taskID = UUID.randomUUID().toString();
            this.description = description;
        }

        // getters and setters for taskID and description
        public String getTaskID() {
            return taskID;
        }

        public String getDescription() {
            return description;
        }
    }
    
     // simulates user login by verifying the password.
    public boolean login(String password) {
        if (this.password.equals(password)) {
            System.out.println(username + " has logged in successfully.");
            return true;
        } else {
            System.out.println("Login failed for user " + username);
            return false;
        }
    }

    
     // simulates user logout.
     
    public void logout() {
        System.out.println(username + " has logged out.");
    }

    
     // resets the user's password to a new value.

    public void resetPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password has been reset for user " + username);
    }

    
     // updates the user's profile with a new username and email.

    public void updateProfile(String username, String email) {
        this.username = username;
        this.email = email;
        System.out.println("Profile updated for user " + userID);
    }

    
     // retrieves the list of tasks assigned to the user.

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    
     // assigns a new task to the user.

    public void assignTask(Task task) {
        assignedTasks.add(task);
    }

    
     // removes a task from the user's assigned tasks.

    public void removeTask(Task task) {
        assignedTasks.remove(task);
    }

    // Getters and setters for userID, username, email, and role

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}
