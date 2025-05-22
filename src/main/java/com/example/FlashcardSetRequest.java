// FlashcardSetRequest.java
package com.example.NoteStream;

import java.util.List;

public class FlashcardSetRequest {
    private String title;
    private List<FlashcardRequest> flashcards;

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FlashcardRequest> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<FlashcardRequest> flashcards) {
        this.flashcards = flashcards;
    }

    // Inner Class for Flashcard Request
    public static class FlashcardRequest {
        private String question;
        private String answer;

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
