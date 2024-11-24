package com.example.NoteStream;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {
    void save(Note note);
    Optional<Note> findByNoteId(String noteId);
    List<Note> findByOwner(User owner);
    List<Note> findByCollaborator(User collaborator);
}
