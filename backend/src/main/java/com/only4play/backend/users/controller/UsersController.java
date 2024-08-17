package com.only4play.backend.users.controller;

import com.only4play.backend.config.ApplicationProperties;
import com.only4play.backend.users.data.CreateUserRequest;
import com.only4play.backend.users.data.UserResponse;
import com.only4play.backend.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final ApplicationProperties applicationProperties;

    /**
     * Register a new user. The user will be created with the default role USER. Verification email will
     * be sent to the user.
     */
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userService.create(request);
        return ResponseEntity.ok(user);
    }

    /**
     * Verify the email of the user, redirect to the login page.
     */
    @GetMapping("/verify-email")
    public RedirectView verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return new RedirectView(applicationProperties.getLoginPageUrl());
    }

}
