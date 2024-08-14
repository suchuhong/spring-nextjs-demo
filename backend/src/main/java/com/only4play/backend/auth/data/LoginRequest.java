package com.only4play.backend.auth.data;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
  @Email
  @NotNull
  private String email;
  private String password;
}