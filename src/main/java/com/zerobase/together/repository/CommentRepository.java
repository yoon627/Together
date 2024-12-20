package com.zerobase.together.repository;

import com.zerobase.together.entity.CommentEntity;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  Page<CommentEntity> findAllByPostIdAndDeletedDateTimeOrderByCreatedDateTimeDesc(Long postId,
      LocalDateTime deletedDateTime, Pageable pageable);
}
