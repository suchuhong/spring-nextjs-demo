package com.only4play.backend.auth.service;

import com.only4play.backend.auth.SecurityUtil;
import com.only4play.backend.auth.data.LoginRequest;
import com.only4play.backend.users.User;
import com.only4play.backend.users.data.UserResponse;
import com.only4play.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
  SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

  /**
   * Sets the cookie for the user if the username and password are correct
   */
  public void login(HttpServletRequest request,
                    HttpServletResponse response,
                    LoginRequest body
  ) throws AuthenticationException {
    UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(body.getEmail(), body.getPassword());
    Authentication authentication = authenticationManager.authenticate(token);
    SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    SecurityContext context = securityContextHolderStrategy.createEmptyContext();
    context.setAuthentication(authentication);
    securityContextHolderStrategy.setContext(context);
    securityContextRepository.saveContext(context, request, response);
  }

  @Transactional
  public UserResponse getSession(HttpServletRequest request) {
    User user = SecurityUtil.getAuthenticatedUser();
    user = userRepository.getReferenceById(user.getId());
    return new UserResponse(user);
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    this.logoutHandler.logout(request, response, authentication);
  }
}