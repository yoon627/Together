package com.zerobase.together.repository;

import com.zerobase.together.entity.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

  Page<PhotoEntity> findAllByCoupleIdOrderByCoupleIdDesc(Long coupleId, Pageable pageable);

  void deleteByImgUrl(String substring);
}
