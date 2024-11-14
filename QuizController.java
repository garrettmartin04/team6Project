// QuizController.java
package com.example.NoteStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final List<Quiz> quizzes = new ArrayList<>();

    // Create a new quiz
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizRequest request) {
        String title = request.getTitle();
        List<Question> questionList = request.getQuestions();

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Quiz title is required.");
        }

        if (questionList == null || questionList.isEmpty()) {
            return ResponseEntity.badRequest().body("At least one question is required.");
        }

        Quiz newQuiz = new Quiz(title, questionList);
        quizzes.add(newQuiz);
        return ResponseEntity.ok("Quiz created successfully with title: " + title);
    }

    // Get all quizzes
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllQuizzes() {
        List<Map<String, String>> quizList = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            Map<String, String> quizInfo = new HashMap<>();
            quizInfo.put("id", quiz.getId());
            quizInfo.put("title", quiz.getTitle());
            quizList.add(quizInfo);
        }
        return ResponseEntity.ok(quizList);
    }

    // Get a specific quiz
    @GetMapping("/get")
    public ResponseEntity<Quiz> getQuiz(@RequestParam String quizId) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId().equals(quizId)) {
                return ResponseEntity.ok(quiz);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // DTO for Quiz Creation Request
    public static class QuizRequest {
        private String title;
        private List<Question> questions;

        // Getters and Setters

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }
    }
}
