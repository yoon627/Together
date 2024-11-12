package com.zerobase.together.repository;

import com.zerobase.together.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);

  Optional<UserEntity> findByCoupleIdAndUsernameNot(Long coupleId, String username);
}
