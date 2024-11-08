package com.zerobase.together.repository;

import com.zerobase.together.entity.ChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

  Page<ChatEntity> findAllByCoupleIdOrderByCreatedDateTimeDesc(Long coupleId, Pageable pageable);
}
