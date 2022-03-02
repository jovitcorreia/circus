package com.castanhocorreia.circus.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignUpData {
  private UUID id;

  @NotBlank(message = "username cannot be blank or null")
  @Pattern(regexp = "^[\\w]*$", message = "username must contain only letters and numbers")
  @Size(max = 16, min = 3, message = "username must have a length between 3 and 16")
  private String username;

  @NotBlank(message = "password cannot be blank or null")
  @Size(min = 6, message = "password must contain the minimum number of 6 characters")
  @ToString.Exclude
  private String password;

  @Email(message = "make sure the email sent in this request is correct")
  private String email;
}
