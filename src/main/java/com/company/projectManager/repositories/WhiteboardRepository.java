package com.company.projectManager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.models.Whiteboard;

//I don't think I'll be using this very often
@Repository
public interface WhiteboardRepository extends CrudRepository<Whiteboard, Long> {

}

