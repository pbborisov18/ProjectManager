package com.company.projectManager.whiteboard.whiteboards.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.company.projectManager.whiteboard.whiteboards.entity.Whiteboard;

@Repository
public interface WhiteboardRepository extends CrudRepository<Whiteboard, Long> {

}

