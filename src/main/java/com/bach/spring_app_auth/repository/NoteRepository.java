package com.bach.spring_app_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bach.spring_app_auth.entities.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
