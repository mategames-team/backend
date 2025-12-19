package com.videogamescatalogue.backend.repository;

import com.videogamescatalogue.backend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByGameApiId(Long gameApiId, Pageable pageable);

    boolean existsByIdAndUserId(Long id, Long userId);
}
