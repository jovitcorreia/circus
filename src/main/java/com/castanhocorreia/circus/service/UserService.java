package com.castanhocorreia.circus.service;

import com.castanhocorreia.circus.data.UserSignUpData;
import com.castanhocorreia.circus.domain.UserModel;
import com.castanhocorreia.circus.domain.UserStatus;
import com.castanhocorreia.circus.repository.UserRepository;
import com.castanhocorreia.circus.util.PropertyRejector;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public boolean checkEmailUnavailability(String email) {
    return userRepository.existsByEmail(email);
  }

  public boolean checkUsernameUnavailability(String username) {
    return userRepository.existsByUsername(username);
  }

  public UserModel createUser(UserSignUpData userData) {
    var userModel = new UserModel();
    BeanUtils.copyProperties(userData, userModel, PropertyRejector.rejectEmptyValues(userData));
    userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
    userModel.setStatus(UserStatus.ACTIVE);
    userModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
    userModel.setLastModifiedDate(userModel.getCreatedDate());
    return userRepository.save(userModel);
  }

  public Optional<UserModel> findUserById(UUID id) {
    return userRepository.findById(id);
  }

  public Optional<UserModel> findUserByName(String username) {
    return userRepository.findByUsername(username);
  }

  public Page<UserModel> listAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public UserModel getUser() {
    return (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
