package com.only4play.backend.users.jobs.handler;

import com.only4play.backend.config.ApplicationProperties;
import com.only4play.backend.email.EmailService;
import com.only4play.backend.users.PasswordResetToken;
import com.only4play.backend.users.User;
import com.only4play.backend.users.jobs.SendResetPasswordEmailJob;
import com.only4play.backend.users.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.transaction.Transactional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SendResetPasswordEmailJobHandler implements JobRequestHandler<SendResetPasswordEmailJob> {

  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final EmailService emailService;
  private final ApplicationProperties applicationProperties;
  private final SpringTemplateEngine templateEngine;

  @Override
  @Transactional
  public void run(SendResetPasswordEmailJob sendResetPasswordEmailJob) throws Exception {
    PasswordResetToken resetToken = passwordResetTokenRepository.findById(sendResetPasswordEmailJob.getTokenId())
        .orElseThrow(() -> new IllegalArgumentException("Token not found"));
    if (!resetToken.isEmailSent()) {
      sendResetPasswordEmail(resetToken.getUser(), resetToken);
    }
  }

  private void sendResetPasswordEmail(User user, PasswordResetToken token) {
    String link = applicationProperties.getBaseUrl() + "/auth/reset-password?token=" + token.getToken();
    Context thymeleafContext = new Context();
    thymeleafContext.setVariable("user", user);
    thymeleafContext.setVariable("link", link);
    String htmlBody = templateEngine.process("password-reset", thymeleafContext);
    emailService.sendHtmlMessage(Stream.of(user.getEmail()).collect(Collectors.toList()), "Password reset requested", htmlBody);
    token.onEmailSent();
    passwordResetTokenRepository.save(token);
  }
}