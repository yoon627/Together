package com.zerobase.together.repository;

import com.zerobase.together.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

  Page<PostEntity> findAllByCoupleIdOrderByCreatedDateTimeDesc(Long coupleId, Pageable pageable);
}
