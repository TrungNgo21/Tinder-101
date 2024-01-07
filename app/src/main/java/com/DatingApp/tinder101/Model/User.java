package com.DatingApp.tinder101.Model;

import com.DatingApp.tinder101.Dto.UserDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
  private String name;
  private String email;
  private Date createdDate;
  private Date updatedDate;

  public UserDto toDto() {
    return UserDto.builder()
        .name(name)
        .email(email)
        .createdDate(createdDate)
        .updatedDate(updatedDate)
        .build();
  }

  public HashMap<String, Object> toMap() {
    return new HashMap<String, Object>() {
      {
        put("isOnline", true);
      }
    };
  }
}
