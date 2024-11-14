package com.example.NoteStream;

import java.util.List;
import java.util.UUID;

public class FlashcardSet {
    private String id;
    private String title;
    private List<Flashcard> flashcards;

    public FlashcardSet(String title, List<Flashcard> flashcards) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.flashcards = flashcards;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    // Inner Class for Flashcard
    public static class Flashcard {
        private String question;
        private String answer;

        public Flashcard(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        // Getters and Setters

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}


