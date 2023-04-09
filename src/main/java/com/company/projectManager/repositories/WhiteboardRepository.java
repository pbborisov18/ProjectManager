package com.company.projectManager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.models.Whiteboard;

import java.util.Optional;


//I don't I'll be using this very often
@Repository
public interface WhiteboardRepository extends CrudRepository<Whiteboard, Long> {

    Optional<Whiteboard> findByName(String name);

}

