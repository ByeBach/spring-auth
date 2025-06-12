package com.bach.spring_app_auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bach.spring_app_auth.entities.Note;
import com.bach.spring_app_auth.entities.User;
import com.bach.spring_app_auth.repository.UserRepository;
import com.bach.spring_app_auth.security.AuthenticationFacade;
import com.bach.spring_app_auth.service.NoteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    public NoteController(NoteService noteService, AuthenticationFacade authenticationFacade, UserRepository userRepository){
        this.noteService = noteService;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody @Valid Note note) {
        User currentUser = authenticationFacade.getAuthenticatedUser();
        note.setAuthor(currentUser);
        Note savedNote = noteService.saveNote(note);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }
    /**
     * En este fragmento, obtenemos el nombre de usuario del usuario autenticado y lo utilizamos para recuperar la entidad User de la base de datos.
     * Luego, asignamos este usuario como autor del producto antes de guardarlo.
     */

    

}
