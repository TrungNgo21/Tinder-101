package com.DatingApp.tinder101.Dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

  private int numOfLiked;
  private String gender;
  private int age;
  private Date createdDate;
  private Date updatedDate;
  private ProfileSettingDto profileSetting;
  private HashMap<String, String> imageUrlsMap;
  @Builder.Default private boolean isOnline = true;
  @Builder.Default private int score = 0;
  @Builder.Default private List<String> matchedUsers = new ArrayList<>();
}
