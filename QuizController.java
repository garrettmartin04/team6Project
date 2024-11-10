package com.example.NoteStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private List<Quiz> quizzes = new ArrayList<>();

    //  new quiz
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        List<Map<String, String>> questionList = (List<Map<String, String>>) request.get("questions");

        Quiz newQuiz = new Quiz(title);

        for (Map<String, String> q : questionList) {
            String prompt = q.get("prompt");
            String answer = q.get("answer");
            newQuiz.addQuestion(new Question(prompt, answer));
        }

        quizzes.add(newQuiz);
        return ResponseEntity.ok("Quiz created successfully with title: " + title);
    }

    //  all quizzes
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

    //  specific quiz
    @GetMapping("/get")
    public ResponseEntity<Quiz> getQuiz(@RequestParam String quizId) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId().equals(quizId)) {
                return ResponseEntity.ok(quiz);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @Controller
    public class QuizPageController {

        @GetMapping("/create-quiz")
        public String createQuizPage() {
            return "create-quiz"; //  static folder
        }

        @GetMapping("/take-quiz")
        public String takeQuizPage() {
            return "take-quiz"; //  static folder
        }
    }
}
