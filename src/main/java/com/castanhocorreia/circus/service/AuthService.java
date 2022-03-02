package com.castanhocorreia.circus.service;

import com.castanhocorreia.circus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetails loadUserById(UUID id) throws AuthenticationCredentialsNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format("user with id %s not found", id)));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    String.format("user with username %s not found", username)));
  }
}
