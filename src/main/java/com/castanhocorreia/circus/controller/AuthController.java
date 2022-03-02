package com.castanhocorreia.circus.controller;

import com.castanhocorreia.circus.component.TokenProvider;
import com.castanhocorreia.circus.data.TokenData;
import com.castanhocorreia.circus.data.UserSignInData;
import com.castanhocorreia.circus.data.UserSignUpData;
import com.castanhocorreia.circus.domain.UserModel;
import com.castanhocorreia.circus.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final TokenProvider tokenProvider;

  @PostMapping(path = "/signup")
  public ResponseEntity<Object> registerUser(@RequestBody @Validated UserSignUpData userData) {
    log.info("new request to register user with the following data: {}", userData.toString());
    if (userService.checkUsernameUnavailability(userData.getUsername())) {
      log.error("username {} is already taken", userData.getUsername());
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(String.format("username %s is already taken", userData.getUsername()));
    } else if (userService.checkEmailUnavailability(userData.getEmail())) {
      log.error("email address {} is already taken", userData.getEmail());
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(String.format("email address %s is already taken", userData.getEmail()));
    }
    UserModel userModel = userService.createUser(userData);
    log.info("new user created successfully with id {}", userModel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenData> authenticateUser(
      @RequestBody @Validated UserSignInData userData) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userData.getUsername(), userData.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = tokenProvider.generate(authentication);
    log.info("new token generated for user with name {}", userData.getUsername());
    return ResponseEntity.ok(
        new TokenData(token, ((UserModel) authentication.getPrincipal()).getId()));
  }
}
