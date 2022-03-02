package com.castanhocorreia.circus.controller;

import com.castanhocorreia.circus.data.RoomData;
import com.castanhocorreia.circus.domain.RoomModel;
import com.castanhocorreia.circus.domain.UserModel;
import com.castanhocorreia.circus.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
  private final RoomService roomService;

  @PostMapping
  public ResponseEntity<Object> createRoom(
      Authentication authentication, @RequestBody @Validated RoomData roomData) {
    UserModel userModel = (UserModel) authentication.getPrincipal();
    log.info("new post request for room by {}", userModel.getUsername());
    RoomModel roomModel = roomService.createRoom(roomData, userModel);
    log.info("new room created successfully with id {}", roomModel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(roomModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getRoomById(Authentication authentication, @PathVariable UUID id) {
    UserModel userModel = (UserModel) authentication.getPrincipal();
    log.info("new get request for room {} by {}", id, userModel.getUsername());
    Optional<RoomModel> roomOptional = roomService.getRoomById(id);
    if (roomOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(String.format("room with id %s not found", id));
    }
    if (roomService.findUserIntoRoom(roomOptional.get(), userModel).isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(roomOptional.get());
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(String.format("%s has no authority to get room %s", userModel.getUsername(), id));
  }

  @GetMapping
  public ResponseEntity<List<RoomModel>> getRoomsByUser(Authentication authentication) {
    UserModel userModel = (UserModel) authentication.getPrincipal();
    log.info("new get request for rooms by {}", userModel.getUsername());
    List<RoomModel> userRooms = roomService.listRoomsByOwner(userModel);
    return ResponseEntity.status(HttpStatus.OK).body(userRooms);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateRoom(
      Authentication authentication,
      @PathVariable UUID id,
      @RequestBody @Validated RoomData roomData) {
    UserModel userModel = (UserModel) authentication.getPrincipal();
    log.info("new update request for room {} by {}", id, userModel.getUsername());
    Optional<RoomModel> roomOptional = roomService.getRoomById(id);
    if (roomOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(String.format("room with id %s not found", id));
    }
    RoomModel roomModel = roomOptional.get();
    if (userModel.getUsername().equals(roomModel.getOwner().getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoom(roomModel, roomData));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(
            String.format(
                "%s has no authority to update room %s",
                userModel.getUsername(), roomModel.getId()));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Object> deleteRoom(Authentication authentication, @PathVariable UUID id) {
    UserModel userModel = (UserModel) authentication.getPrincipal();
    log.info("new delete request for room {} by {}", id, userModel.getUsername());
    Optional<RoomModel> roomOptional = roomService.getRoomById(id);
    if (roomOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(String.format("room with id %s not found", id));
    }
    RoomModel roomModel = roomOptional.get();
    if (userModel.getUsername().equals(roomModel.getOwner().getUsername())) {
      roomService.deleteRoom(roomModel);
      return ResponseEntity.status(HttpStatus.OK)
          .body(String.format("room with id %s deleted successfully", id));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(
            String.format(
                "%s has no authority to delete room %s",
                userModel.getUsername(), roomModel.getId()));
  }
}
