package com.castanhocorreia.circus.data;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class TokenData {
  @NonNull private String token;

  private String type = "Bearer";

  @NonNull private UUID user;
}
