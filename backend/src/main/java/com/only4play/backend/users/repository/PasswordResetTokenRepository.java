package com.only4play.backend.users.repository;

import com.only4play.backend.users.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  @Query("SELECT prt FROM PasswordResetToken prt WHERE prt.token = ?1")
  Optional<PasswordResetToken> findByToken(String passwordResetToken);

}