package com.zerobase.together.repository;

import com.zerobase.together.entity.PostEntity;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {


  Page<PostEntity> findAllByCoupleIdAndDeletedDateTimeOrderByCreatedDateTimeDesc(Long coupleId,
      LocalDateTime deletedDateTime, Pageable pageable);
}
