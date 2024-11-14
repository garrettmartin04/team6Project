// FlashcardController.java
package com.example.NoteStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

// Import the FlashcardSetRequest class
import com.example.NoteStream.FlashcardSetRequest;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    private final List<FlashcardSet> flashcardSets = new ArrayList<>();

    // Create a new flashcard set
    @PostMapping("/create")
    public ResponseEntity<String> createFlashcardSet(@RequestBody FlashcardSetRequest request) {
        String title = request.getTitle();
        List<FlashcardSet.Flashcard> flashcards;

        // Validate title
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Flashcard set title is required.");
        }

        // Validate flashcards
        if (request.getFlashcards() == null || request.getFlashcards().isEmpty()) {
            return ResponseEntity.badRequest().body("At least one flashcard is required.");
        }

        // Map FlashcardSetRequest.FlashcardRequest to FlashcardSet.Flashcard
        flashcards = request.getFlashcards().stream()
                .map(flashcardReq -> new FlashcardSet.Flashcard(flashcardReq.getQuestion(), flashcardReq.getAnswer()))
                .collect(Collectors.toList());

        // Create and add the new flashcard set
        FlashcardSet newSet = new FlashcardSet(title, flashcards);
        flashcardSets.add(newSet);

        return ResponseEntity.ok("Flashcard set created successfully with ID: " + newSet.getId());
    }

    // Get all flashcard sets
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllFlashcardSets() {
        List<Map<String, String>> setList = flashcardSets.stream()
                .map(set -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", set.getId());
                    map.put("title", set.getTitle());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(setList);
    }

    // Get a specific flashcard set
    @GetMapping("/get")
    public ResponseEntity<FlashcardSet> getFlashcardSet(@RequestParam String setId) {
        for (FlashcardSet set : flashcardSets) {
            if (set.getId().equals(setId)) {
                return ResponseEntity.ok(set);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // No nested classes or misplaced code here
}
