package com.smatech.smatrentalpro.backend.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smatech.smatrentalpro.backend.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("username")
  private String username;

  @JsonProperty("user_id")
  private Integer userId;

  @JsonProperty("role")
  private Role role;

  @JsonProperty("image")
  private Byte[] image;
}
