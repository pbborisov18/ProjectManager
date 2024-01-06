package com.company.projectManager.whiteboard.notes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.whiteboard.notes.entity.Note;

import java.util.List;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    List<Note> findAllByColumnId(Long id);

    void deleteNotesByColumnId(Long id);

}
