package com.example.NoteStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final Map<String, Note> notes = new HashMap<>(); // Map of noteId to Note
    private final UserController userController; // Reference to UserController for user management

    public NoteController(UserController userController) {
        this.userController = userController;
    }

    // Create a new note
    @PostMapping("/create")
    public ResponseEntity<?> createNote(@RequestParam String userId,
                                        @RequestParam String title,
                                        @RequestParam String content) {
        User owner = userController.getUserById(userId);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Note note = new Note(title, content, owner);
        notes.put(note.getNoteId(), note);

        return ResponseEntity.ok("Note created with ID: " + note.getNoteId());
    }

    // Add collaborator to a note
    @PostMapping("/{noteId}/addCollaborator")
    public ResponseEntity<String> addCollaborator(@PathVariable String noteId,
                                                  @RequestParam String ownerId,
                                                  @RequestParam String collaboratorId) {
        Note note = notes.get(noteId);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");
        }
        if (!note.getOwner().getUserID().equals(ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the owner can add collaborators.");
        }
        User collaborator = userController.getUserById(collaboratorId);
        if (collaborator == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collaborator not found.");
        }
        if (note.getCollaborators().contains(collaborator)) {
            return ResponseEntity.badRequest().body("User is already a collaborator.");
        }
        note.addCollaborator(collaborator);
        return ResponseEntity.ok("Collaborator added.");
    }

    // Remove collaborator from a note
    @PostMapping("/{noteId}/removeCollaborator")
    public ResponseEntity<String> removeCollaborator(@PathVariable String noteId,
                                                     @RequestParam String ownerId,
                                                     @RequestParam String collaboratorId) {
        Note note = notes.get(noteId);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");
        }
        if (!note.getOwner().getUserID().equals(ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the owner can remove collaborators.");
        }
        User collaborator = userController.getUserById(collaboratorId);
        if (collaborator == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collaborator not found.");
        }
        if (!note.getCollaborators().contains(collaborator)) {
            return ResponseEntity.badRequest().body("User is not a collaborator.");
        }
        note.removeCollaborator(collaborator);
        return ResponseEntity.ok("Collaborator removed.");
    }

    // Edit a note
    @PostMapping("/{noteId}/edit")
    public ResponseEntity<String> editNote(@PathVariable String noteId,
                                           @RequestParam String userId,
                                           @RequestParam String newContent) {
        Note note = notes.get(noteId);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");
        }
        User user = userController.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        if (!note.getOwner().equals(user) && !note.isCollaborator(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to edit this note.");
        }
        note.setContent(newContent);
        return ResponseEntity.ok("Note updated.");
    }

    // Get notes accessible by a user
 // Get notes accessible by a user
    @GetMapping("/accessible")
    public ResponseEntity<?> getAccessibleNotes(@RequestParam String userId) {
        User user = userController.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        List<Map<String, String>> accessibleNotes = new ArrayList<>();
        for (Note note : notes.values()) {
            if (note.getOwner().equals(user) || note.isCollaborator(user)) {
                Map<String, String> noteInfo = new HashMap<>();
                noteInfo.put("noteId", note.getNoteId());
                noteInfo.put("title", note.getTitle());
                noteInfo.put("ownerId", note.getOwner().getUserID()); // Include ownerId
                accessibleNotes.add(noteInfo);
            }
        }
        return ResponseEntity.ok(accessibleNotes);
    }
 // Get a note by ID
    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteById(@PathVariable String noteId, @RequestParam String userId) {
        Note note = notes.get(noteId);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");
        }
        User user = userController.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        if (!note.getOwner().equals(user) && !note.isCollaborator(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this note.");
        }
        Map<String, String> noteInfo = new HashMap<>();
        noteInfo.put("noteId", note.getNoteId());
        noteInfo.put("title", note.getTitle());
        noteInfo.put("content", note.getContent());
        noteInfo.put("ownerId", note.getOwner().getUserID());
        return ResponseEntity.ok(noteInfo);
    }
}

