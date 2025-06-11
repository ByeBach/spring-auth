package com.bach.spring_app_auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bach.spring_app_auth.entities.Note;
import com.bach.spring_app_auth.entities.User;
import com.bach.spring_app_auth.security.AuthenticationFacade;
import com.bach.spring_app_auth.service.NoteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final AuthenticationFacade authenticationFacade;

    public NoteController(NoteService noteService, AuthenticationFacade authenticationFacade){
        this.noteService = noteService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody @Valid Note note) {
        User currentUser = authenticationFacade.getAuthenticatedUser();
        note.setAuthor(currentUser);
        Note saveNote = noteService.saveNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveNote);
    }
    

    

}
