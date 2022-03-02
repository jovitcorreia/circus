package com.castanhocorreia.circus.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Builder
@Entity
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Table(name = "rooms")
@ToString
public class RoomModel implements Serializable {
  private static final long serialVersionUID = 2665154970559241943L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @JoinColumn(name = "owner_id", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @ManyToOne(optional = false)
  private UserModel owner;

  @JoinTable(
      inverseJoinColumns = @JoinColumn(name = "room_id"),
      joinColumns = @JoinColumn(name = "user_id"),
      name = "rooms_users")
  @ManyToMany(fetch = FetchType.EAGER)
  @ToString.Exclude
  private Set<UserModel> users = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private PlayerState playerState;

  private String lastVideoUrl;

  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
  private LocalDateTime createdDate;

  @Column(nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
  private LocalDateTime lastModifiedDate;

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    RoomModel roomModel = (RoomModel) object;
    return id.equals(roomModel.id) && name.equals(roomModel.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
