package com.DatingApp.tinder101.Model;

import com.DatingApp.tinder101.Dto.ProfileSettingDto;
import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.Enum.LookingForEnum;

import java.util.ArrayList;
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
public class ProfileSetting {
  @Builder.Default private String quotes = "";
  @Builder.Default private List<String> interests = new ArrayList<>();
  @Builder.Default private HashMap<String, String> lifestyleList = new HashMap<>();
  @Builder.Default private HashMap<String, String> basics = new HashMap<>();
  @Builder.Default private String lookingForEnum = null;

  private HashMap<LifestyleEnum, String> toEnumLifestyle() {
    HashMap<LifestyleEnum, String> enumLifestyle = new HashMap<>();
    for (Map.Entry<String, String> entry : lifestyleList.entrySet()) {
      enumLifestyle.put(LifestyleEnum.valueOf(entry.getKey()), entry.getValue());
    }
    return enumLifestyle;
  }

  private HashMap<BasicEnum, String> toEnumBasics() {
    HashMap<BasicEnum, String> enumBasics = new HashMap<>();
    for (Map.Entry<String, String> entry : basics.entrySet()) {
      enumBasics.put(BasicEnum.valueOf(entry.getKey()), entry.getValue());
    }
    return enumBasics;
  }

  public ProfileSettingDto toDto() {
    return ProfileSettingDto.builder()
        .quotes(quotes)
        .interests(interests)
        .lifestyleList(toEnumLifestyle())
        .basics(toEnumBasics())
        .lookingForEnum(LookingForEnum.valueOf(lookingForEnum))
        .build();
  }
}
