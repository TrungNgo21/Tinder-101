package com.DatingApp.tinder101.Dto;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
  private String id;
  private String name;
  private String email;
  private Date createdDate;
  private Date updatedDate;
  @Builder.Default private boolean isOnline = true;
  @Builder.Default private List<String> matchedUsers = new ArrayList<>();
}
