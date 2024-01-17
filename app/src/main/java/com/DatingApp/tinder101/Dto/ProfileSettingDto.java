package com.DatingApp.tinder101.Dto;

import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.Enum.LookingForEnum;

import java.util.ArrayList;
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
public class ProfileSettingDto {
  @Builder.Default private String quotes = "";
  @Builder.Default private List<String> interests = new ArrayList<>();
  @Builder.Default private HashMap<LifestyleEnum, String> lifestyleList = new HashMap<>();
  @Builder.Default private HashMap<BasicEnum, String> basics = new HashMap<>();
  @Builder.Default private LookingForEnum lookingForEnum = null;
}
