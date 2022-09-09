package com.careerdevs.jphsql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.careerdevs.jphsql.models.CommentModel;

public interface CommentRepository extends JpaRepository<CommentModel, Integer> {
}
