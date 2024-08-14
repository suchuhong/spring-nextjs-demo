package com.only4play.backend.auth;

import com.only4play.backend.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final ApplicationProperties applicationProperties;
  private final UserDetailsService userDetailsService;
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/users").permitAll()
            .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .antMatchers(HttpMethod.GET, "/api/users/verify-email").permitAll()
            .anyRequest().authenticated();

    http.addFilterBefore(new UsernamePasswordAuthenticationFilter(), LogoutFilter.class);
    http.userDetailsService(userDetailsService);

    http.csrf().disable();

    http.cors(customizer -> customizer.configurationSource(corsConfigurationSource()));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedOrigins(applicationProperties.getAllowedOrigins());
      config.setAllowedMethods(Stream.of("GET", "POST", "PUT", "DELETE", "OPTIONS").collect(Collectors.toList()));
      config.setAllowedHeaders(Stream.of("*").collect(Collectors.toList()));
      config.setAllowCredentials(true);
      return config;
    };
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(daoAuthenticationProvider);
  }


}