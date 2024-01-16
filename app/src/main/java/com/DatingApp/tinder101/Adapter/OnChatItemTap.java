package com.DatingApp.tinder101.Adapter;

import com.DatingApp.tinder101.Dto.UserDto;

public interface OnChatItemTap {
  void openChat(int userDtoPosition);

  void openChatId(String userId);
}
