package com.DatingApp.tinder101.Utils;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.Enum.LookingForEnum;
import com.DatingApp.tinder101.R;

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
    if (type instanceof LifestyleEnum) {
      switch ((LifestyleEnum) type) {
        case PET:
          return Constant.PET;
        case DRINKING:
          return Constant.DRINKING;
        case SMOKE:
          return Constant.SMOKE;
        case WORKOUT:
          return Constant.WORKOUT;
      }
    }

    if (type instanceof BasicEnum) {
      switch ((BasicEnum) type) {
        case ZODIAC:
          return Constant.ZODIAC;
        case EDUCATION:
          return Constant.EDUCATION;
        case COMMUNICATION:
          return Constant.COMMUNICATION;
        case LOVE:
          return Constant.LOVE;
      }
    }

    return null;
  }

  public static int getIconResource(Object type) {
    if (type instanceof LifestyleEnum) {
      switch ((LifestyleEnum) type) {
        case PET:
          return R.drawable.pet_icon;
        case DRINKING:
          return R.drawable.drink_icon;
        case SMOKE:
          return R.drawable.smoke_icon;
        case WORKOUT:
          return R.drawable.workout_icon;
      }
    }

    if (type instanceof BasicEnum) {
      switch ((BasicEnum) type) {
        case ZODIAC:
          return R.drawable.zodiac_icon;
        case EDUCATION:
          return R.drawable.education_icon;
        case COMMUNICATION:
          return R.drawable.communication_icon;
        case LOVE:
          return R.drawable.love_icon;
      }
    }

    if (type instanceof LookingForEnum) {
      switch ((LookingForEnum) type) {
        case LONG_OK:
          return R.drawable.ic_long_ok;
        case SHORT_OK:
          return R.drawable.ic_party;
        case LONG_SHORT_OKE:
          return R.drawable.ic_long_short;
        case SHORT_LONG_OK:
          return R.drawable.ic_short_long_ok;
        case FRIEND_OK:
          return R.drawable.ic_friend;
        case THINKING:
          return R.drawable.ic_thinking;
      }
    }

    return 0;
  }
}
