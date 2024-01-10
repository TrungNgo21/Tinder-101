package com.DatingApp.tinder101.Model;

import java.util.ArrayList;
import java.util.List;

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
  @Builder.Default private List<String> lifestyleList = new ArrayList<>();
  @Builder.Default private List<String> basics = new ArrayList<>();
  @Builder.Default private String lookingForEnum = null;
}
