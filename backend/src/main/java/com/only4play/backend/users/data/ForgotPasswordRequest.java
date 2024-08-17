package com.only4play.backend.users.data;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ForgotPasswordRequest {
  @Email
  private String email;
}