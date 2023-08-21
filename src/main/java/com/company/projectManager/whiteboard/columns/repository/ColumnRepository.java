package com.company.projectManager.whiteboard.columns.repository;

import com.company.projectManager.whiteboard.columns.entity.Column;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends CrudRepository<Column, Long> {

   List<Column> findAllByWhiteboardId(Long id);
}
