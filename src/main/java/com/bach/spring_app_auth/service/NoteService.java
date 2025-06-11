package com.bach.spring_app_auth.service;

import org.springframework.stereotype.Service;

import com.bach.spring_app_auth.entities.Note;
import com.bach.spring_app_auth.repository.NoteRepository;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public Note saveNote(Note note){
        return noteRepository.save(note);
    }
}
