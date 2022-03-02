package com.castanhocorreia.circus.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignInData {
  private UUID id;

  @NotBlank(message = "username cannot be blank or null")
  private String username;

  @NotBlank(message = "password cannot be blank or null")
  @ToString.Exclude
  private String password;
}
