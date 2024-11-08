package com.zerobase.together.repository;

import com.zerobase.together.entity.HistoryEntity;
import com.zerobase.together.type.HistoryTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

  void deleteAllByHistoryTargetAndTargetId(HistoryTarget historyTarget, Long targetId);

  Page<HistoryEntity> findAllByCoupleIdOrderByCreatedDateTimeDesc(Long coupleId, Pageable pageable);
}
