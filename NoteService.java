package com.example.NoteStream;

public class NoteService {
    private NoteRepository noteRepository;
    private UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public void addCollaborator(String noteId, String collaboratorUsername) throws Exception {
        Note note = noteRepository.findByNoteId(noteId)
                .orElseThrow(() -> new Exception("Note not found"));
        User collaborator = userRepository.findByUsername(collaboratorUsername)
                .orElseThrow(() -> new Exception("User not found"));

        note.addCollaborator(collaborator);
        noteRepository.save(note);
    }

    public void removeCollaborator(String noteId, String collaboratorUsername) throws Exception {
        Note note = noteRepository.findByNoteId(noteId)
                .orElseThrow(() -> new Exception("Note not found"));
        User collaborator = userRepository.findByUsername(collaboratorUsername)
                .orElseThrow(() -> new Exception("User not found"));

        note.removeCollaborator(collaborator);
        noteRepository.save(note);
    }

    public boolean hasAccess(String userId, String noteId) throws Exception {
        Note note = noteRepository.findByNoteId(noteId)
                .orElseThrow(() -> new Exception("Note not found"));
        User user = userRepository.findByUserID(userId)
                .orElseThrow(() -> new Exception("User not found"));

        return note.getOwner().equals(user) || note.isCollaborator(user);
    }

    // Additional methods for note management...
}

