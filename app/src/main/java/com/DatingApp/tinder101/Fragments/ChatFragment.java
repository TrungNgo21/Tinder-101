package com.DatingApp.tinder101.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DatingApp.tinder101.Activity.ConversationActivity;
import com.DatingApp.tinder101.Adapter.MessageItemAdapter;
import com.DatingApp.tinder101.Adapter.OnChatItemTap;
import com.DatingApp.tinder101.Adapter.RoundChatItemAdapter;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Model.Conversation;
import com.DatingApp.tinder101.Model.User;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.MessageService;
import com.DatingApp.tinder101.Service.SystemService;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements OnChatItemTap {
  private FragmentChatBinding fragmentChatBinding;
  private List<Conversation> conversations = new ArrayList<>();
  private List<UserDto> users;

  private MessageService messageService;

  private SystemService systemService;

  private UserService userService;

  public ChatFragment(List<UserDto> users) {
    this.users = users;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userService = new UserService(requireContext());
    messageService = new MessageService(requireContext(), userService);
    systemService = new SystemService(userService);
    fragmentChatBinding = FragmentChatBinding.inflate(getLayoutInflater());
    setUpMessages();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    setUpNewMatches();
    fragmentChatBinding.lastOnline.setText(
        "+ " + String.valueOf(userService.getCurrentUser().getNumOfLiked()));
    fragmentChatBinding.profileName.setText(
        userService.getCurrentUser().getNumOfLiked() + " likes");

    systemService.setNotiListener(fragmentChatBinding.lastOnline, fragmentChatBinding.profileName);
    // Inflate the layout for this fragment
    return fragmentChatBinding.getRoot();
  }

  private void setUpNewMatches() {
    RoundChatItemAdapter roundChatItemAdapter = new RoundChatItemAdapter(users, this);
    fragmentChatBinding.roundChatItemsDisplay.setAdapter(roundChatItemAdapter);
    fragmentChatBinding.roundChatItemsDisplay.setLayoutManager(
        new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
  }

  private void setUpMessages() {
    MessageItemAdapter messageItemAdapter =
        new MessageItemAdapter(conversations, requireContext(), this);
    messageService.setConversationListener(
        conversations, messageItemAdapter, fragmentChatBinding.messageItemsDisplay);
    fragmentChatBinding.messageItemsDisplay.setAdapter(messageItemAdapter);
    fragmentChatBinding.messageItemsDisplay.setLayoutManager(
        new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
  }

  @Override
  public void openChat(int userDtoPosition) {
    Intent intent = new Intent(requireContext(), ConversationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(Constant.KEY_RECEIVED_USED_ID, users.get(userDtoPosition).getId());
    startActivity(intent);
  }

  @Override
  public void openChatId(String userId) {
    Intent intent = new Intent(requireContext(), ConversationActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(Constant.KEY_RECEIVED_USED_ID, userId);
    startActivity(intent);
  }
}
