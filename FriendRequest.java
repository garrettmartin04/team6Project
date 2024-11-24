package com.example.NoteStream;

import java.time.LocalDateTime;
import java.util.UUID;

public class FriendRequest {
    private String requestId;
    private User sender;
    private User receiver;
    private LocalDateTime sentAt;
    private boolean accepted;

    public FriendRequest(User sender, User receiver) {
        this.requestId = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = LocalDateTime.now();
        this.accepted = false;
    }

    // Getters and setters
    public String getRequestId() {
        return requestId;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void accept() {
        this.accepted = true;
    }
}
