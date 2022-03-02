package com.castanhocorreia.circus.service;

import com.castanhocorreia.circus.data.RoomData;
import com.castanhocorreia.circus.domain.RoomModel;
import com.castanhocorreia.circus.domain.UserModel;
import com.castanhocorreia.circus.repository.RoomRepository;
import com.castanhocorreia.circus.repository.UserRepository;
import com.castanhocorreia.circus.util.PropertyRejector;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomService {
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  public RoomModel createRoom(RoomData roomData, UserModel userModel) {
    var roomModel = new RoomModel();
    roomModel.setName(roomData.getTitle());
    roomModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
    roomModel.setLastModifiedDate(roomModel.getCreatedDate());
    roomModel.setOwner(userModel);
    roomModel.getUsers().add(roomModel.getOwner());
    userModel.getRooms().add(roomModel);
    userRepository.save(userModel);
    return roomModel;
  }

  public List<RoomModel> listRoomsByOwner(UserModel userModel) {
    return roomRepository.findAllByOwner(userModel.getId());
  }

  public Optional<RoomModel> getRoomById(UUID id) {
    return roomRepository.findById(id);
  }

  public Optional<UserModel> findUserIntoRoom(RoomModel roomModel, UserModel userModel) {
    return roomRepository.findUserIntoRoom(roomModel.getId(), userModel.getId());
  }

  public RoomModel updateRoom(RoomModel roomModel, RoomData roomData) {
    BeanUtils.copyProperties(roomModel, roomData, PropertyRejector.rejectEmptyValues(roomData));
    return roomRepository.save(roomModel);
  }

  public void deleteRoom(RoomModel roomModel) {}
}
