package com.api.backend.comment.data.repository;

import com.api.backend.comment.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}