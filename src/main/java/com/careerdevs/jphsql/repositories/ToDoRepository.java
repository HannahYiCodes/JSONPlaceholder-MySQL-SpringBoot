package com.careerdevs.jphsql.repositories;

import com.careerdevs.jphsql.models.ToDoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDoModel, Integer> {
}
