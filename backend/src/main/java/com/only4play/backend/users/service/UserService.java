package com.only4play.backend.users.service;

import com.only4play.backend.users.User;
import com.only4play.backend.users.VerificationCode;
import com.only4play.backend.users.data.CreateUserRequest;
import com.only4play.backend.users.data.UserResponse;
import com.only4play.backend.users.jobs.SendWelcomeEmailJob;
import com.only4play.backend.users.repository.UserRepository;
import com.only4play.backend.users.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    @Transactional
    public UserResponse create(@Valid CreateUserRequest request) {
        User user = new User(request);
        user = userRepository.save(user);
        sendVerificationEmail(user);
        return new UserResponse(user);
    }

    private void sendVerificationEmail(User user) {
        VerificationCode verificationCode = new VerificationCode(user);
        user.setVerificationCode(verificationCode);
        verificationCodeRepository.save(verificationCode);
        SendWelcomeEmailJob sendWelcomeEmailJob = new SendWelcomeEmailJob(user.getId());
        BackgroundJobRequest.enqueue(sendWelcomeEmailJob);
    }
}
