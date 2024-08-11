package com.only4play.backend.util.validators;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface PasswordMatch {

  String message() default "Passwords do not match";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};

  String passwordField();
  String passwordConfirmationField();
}