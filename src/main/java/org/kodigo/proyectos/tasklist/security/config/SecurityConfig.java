package org.kodigo.proyectos.tasklist.security.config;

import org.kodigo.proyectos.tasklist.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Autowired private UserDetailsServiceImpl userDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> {
              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);

    return authenticationManagerBuilder.build();
  }
}
