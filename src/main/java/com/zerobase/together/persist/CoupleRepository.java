package com.zerobase.together.persist;

import com.zerobase.together.persist.entity.CoupleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleRepository extends JpaRepository<CoupleEntity, Long> {

}
