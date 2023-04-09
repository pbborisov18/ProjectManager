package com.company.projectManager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.models.Note;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    Optional<Note> findNoteByName(String name);

    List<Note> findNotesByWhiteboardId(Long id);
}
