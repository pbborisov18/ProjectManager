package com.company.projectManager.whiteboard.notes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.whiteboard.notes.entity.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    Iterable<Note> findAllByColumnId(Long id);
}
