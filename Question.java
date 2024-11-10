package com.example.NoteStream;

import java.util.UUID;

public class Question {
    private String id;
    private String prompt;
    private String answer;

    public Question(String prompt, String answer) {
        this.id = UUID.randomUUID().toString();
        this.prompt = prompt;
        this.answer = answer;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getPrompt() { return prompt; }
    public String getAnswer() { return answer; }
}
