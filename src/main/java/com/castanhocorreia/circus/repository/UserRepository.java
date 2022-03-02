package com.castanhocorreia.circus.repository;

import com.castanhocorreia.circus.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  Optional<UserModel> findByUsername(String username);
}
