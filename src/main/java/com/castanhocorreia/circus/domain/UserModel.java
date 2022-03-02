package com.castanhocorreia.circus.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Entity
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Table(name = "users")
@ToString
public class UserModel implements Serializable, UserDetails {
  private static final long serialVersionUID = -9149644997280270862L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  @JsonIgnore
  @ToString.Exclude
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(length = 16, nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @Fetch(FetchMode.SUBSELECT)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "owner")
  @ToString.Exclude
  private Set<RoomModel> rooms = new HashSet<>();

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
    UserModel userModel = (UserModel) object;
    return id.equals(userModel.id)
        && username.equals(userModel.username)
        && email.equals(userModel.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email);
  }

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}
