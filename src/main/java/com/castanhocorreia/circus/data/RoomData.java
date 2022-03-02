package com.castanhocorreia.circus.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomData {
  private UUID id;

  @NotBlank(message = "title cannot be blank or null")
  private String title;
}
