package com.company.projectManager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.models.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

    Iterable<Note> findAllByColumnId(Long id);
}
