package com.example.NoteStream;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Note {
    private String noteId;
    private String title;
    private String content;
    private User owner;
    private List<User> collaborators; // List of users who can edit the note

    public Note(String title, String content, User owner) {
        this.noteId = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.collaborators = new ArrayList<>();
    }

    // Getters and setters
    public String getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    } 

    public void setContent(String content) {
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    // Methods to manage collaborators
    public void addCollaborator(User user) {
        if (!collaborators.contains(user)) {
            collaborators.add(user);
        }
    }

    public void removeCollaborator(User user) {
        collaborators.remove(user);
    }

    public boolean isCollaborator(User user) {
        return collaborators.contains(user);
    }
}
