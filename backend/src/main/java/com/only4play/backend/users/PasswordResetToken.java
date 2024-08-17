package com.only4play.backend.users;

import com.only4play.backend.entity.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Getter
@NoArgsConstructor
public class PasswordResetToken extends AbstractEntity {

  private String token;
  private boolean emailSent = false;
  private LocalDateTime expiresAt;

  @ManyToOne
  private User user;

  public PasswordResetToken(User user) {
    this.user = user;
    this.token = RandomStringUtils.random(6, false, true);
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }

  public void onEmailSent() {
    this.emailSent = true;
    this.expiresAt = LocalDateTime.now().plusMinutes(10);
  }
}