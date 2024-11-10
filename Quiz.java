package com.example.NoteStream;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {
    private String id;
    private String title;
    private List<Question> questions;

    public Quiz(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.questions = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public List<Question> getQuestions() { return questions; }
    public void addQuestion(Question question) { this.questions.add(question); }
}
