// PageController.java
package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/create-flashcards")
    public String createFlashcardsPage() {
        return "create-flashcards";
    }

    @GetMapping("/study-flashcards")
    public String studyFlashcardsPage() {
        return "study-flashcards";
    }

    @GetMapping("/create-quiz")
    public String createQuizPage() {
        return "create-quiz";
    }

    @GetMapping("/take-quiz")
    public String takeQuizPage() {
        return "take-quiz";
    }
}
