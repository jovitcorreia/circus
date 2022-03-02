package com.castanhocorreia.circus.controller;

import com.castanhocorreia.circus.domain.UserModel;
import com.castanhocorreia.circus.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping()
  public ResponseEntity<Page<UserModel>> indexAllUsers(
      @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable,
      Authentication authentication) {
    UserModel userModel = (UserModel) authentication.getPrincipal();
    log.info("authentication: {}", userModel.getUsername());
    Page<UserModel> userPage = userService.listAllUsers(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(userPage);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
    Optional<UserModel> userOptional = userService.findUserById(id);
    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(String.format("user with id %s not found", id));
    }
    UserModel user = userOptional.get();
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }
}
