package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {
    private String id;
    private String title;
    private List<Question> questions;

    public Quiz(String title, List<Question> questions) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.questions = new ArrayList<>(questions);
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    // Add other necessary methods if required
}



