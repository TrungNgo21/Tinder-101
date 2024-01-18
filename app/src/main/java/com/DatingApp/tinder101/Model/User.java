package com.DatingApp.tinder101.Model;

import com.DatingApp.tinder101.Dto.ProfileSettingDto;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Enum.LookingForEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
  private int age;
  private int numOfLiked;
  private HashMap<String, String> imageUrlsMap;
  private Date createdDate;
  private String gender;
  private Date updatedDate;
  @Builder.Default private List<String> matchedUsers = new ArrayList<>();
  @Builder.Default private ProfileSetting profileSetting = ProfileSetting.builder().build();

  public UserDto toDto() {
    return UserDto.builder()
        .name(name)
        .email(email)
        .gender(gender)
        .age(age)
        .numOfLiked(numOfLiked)
        .imageUrlsMap(imageUrlsMap)
        .profileSetting(profileSetting.toDto())
        .matchedUsers(matchedUsers)
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
