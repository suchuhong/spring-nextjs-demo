package com.only4play.backend.users.data;

import com.only4play.backend.util.validators.PasswordMatch;
import com.only4play.backend.util.validators.Unique;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@PasswordMatch(passwordField = "password", passwordConfirmationField = "passwordConfirmation")
@Builder
public class CreateUserRequest {
    @Email
    @Unique(columnName = "email", tableName = "user", message = "User with this email already exists")
    private String email;
    @NotNull
    @Length(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "must contain at least one uppercase letter, one lowercase letter, and one digit.")
    private String password;
    private String passwordConfirmation;
    private String firstName;
    private String lastName;
}