package com.only4play.backend.auth;

import com.only4play.backend.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final ApplicationProperties applicationProperties;
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/users").permitAll()
            .anyRequest().authenticated();

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


}