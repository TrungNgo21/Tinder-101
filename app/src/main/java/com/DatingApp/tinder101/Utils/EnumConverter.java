package com.DatingApp.tinder101.Utils;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Enum.LookingForEnum;

public class EnumConverter {
  public static String toString(Object type) {
    if (type instanceof LookingForEnum) {
      if (type.equals(LookingForEnum.LONG_OK)) {
        return Constant.LONG_OK;
      } else if (type.equals(LookingForEnum.LONG_SHORT_OKE)) {
        return Constant.LONG_SHORT_OK;
      } else if (type.equals(LookingForEnum.SHORT_LONG_OK)) {
        return Constant.SHORT_LONG_OK;
      } else if (type.equals(LookingForEnum.FRIEND_OK)) {
        return Constant.FRIEND_OK;
      } else if (type.equals(LookingForEnum.SHORT_OK)) {
        return Constant.SHORT_OK;
      } else {
        return Constant.THINKING;
      }
    }
    return null;
  }
}
