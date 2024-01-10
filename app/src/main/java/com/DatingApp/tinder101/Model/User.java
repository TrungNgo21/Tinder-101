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
  private HashMap<String, String> imageUrlsMap;
  private Date createdDate;
  private Date updatedDate;

  @Builder.Default private ProfileSetting profileSetting = ProfileSetting.builder().build();

  public UserDto toDto() {
    return UserDto.builder()
        .name(name)
        .email(email)
        .imageUrlsMap(imageUrlsMap)
        .profileSetting(
            ProfileSettingDto.builder()
                .quotes(profileSetting.getQuotes())
                .basics(profileSetting.getBasics())
                .interests(profileSetting.getInterests())
                .lifestyleList(profileSetting.getLifestyleList())
                .lookingForEnum(LookingForEnum.valueOf(profileSetting.getLookingForEnum()))
                .build())
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
