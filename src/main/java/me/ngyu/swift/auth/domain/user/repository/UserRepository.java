package me.ngyu.swift.auth.domain.user.repository;

import me.ngyu.swift.auth.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
