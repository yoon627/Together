package com.zerobase.together.repository;

import com.zerobase.together.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUserId(String userId);

  boolean existsByUserId(String userId);
}
