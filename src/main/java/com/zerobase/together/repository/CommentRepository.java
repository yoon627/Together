package com.zerobase.together.repository;

import com.zerobase.together.entity.CommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  List<CommentEntity> getCommentEntitiesByPostId(Long postId);
}
