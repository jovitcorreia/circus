package com.castanhocorreia.circus.repository;

import com.castanhocorreia.circus.domain.RoomModel;
import com.castanhocorreia.circus.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomModel, UUID> {
  @Query(value = "SELECT * FROM rooms WHERE owner_id = :userId", nativeQuery = true)
  List<RoomModel> findAllByOwner(@Param("userId") UUID userId);

  @Query(
      value =
          "SELECT u.* FROM users u JOIN rooms_users ru ON ru.room_id = :roomId AND ru.user_id = :userId",
      nativeQuery = true)
  Optional<UserModel> findUserIntoRoom(@Param("roomId") UUID roomId, @Param("userId") UUID userId);
}
