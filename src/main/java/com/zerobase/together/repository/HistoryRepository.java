package com.zerobase.together.repository;

import com.zerobase.together.entity.HistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

  List<HistoryEntity> getHistoryEntitiesByCoupleId(Long coupleId);
}
