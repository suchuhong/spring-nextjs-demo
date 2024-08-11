package com.only4play.backend.users.service;

import com.only4play.backend.users.User;
import com.only4play.backend.users.data.CreateUserRequest;
import com.only4play.backend.users.data.UserResponse;
import com.only4play.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(@Valid CreateUserRequest request) {
        User user = new User(request);
        user = userRepository.save(user);
        // TODO send email
        return new UserResponse(user);
    }
}
