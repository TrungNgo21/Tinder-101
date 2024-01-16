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
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.databinding.FragmentChatBinding;

import java.util.List;

public class ChatFragment extends Fragment implements OnChatItemTap {
  private FragmentChatBinding fragmentChatBinding;
  private List<UserDto> users;

  public ChatFragment(List<UserDto> users) {
    this.users = users;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentChatBinding = FragmentChatBinding.inflate(getLayoutInflater());
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    setUpNewMatches();
    setUpMessages();
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
    MessageItemAdapter messageItemAdapter = new MessageItemAdapter(users, this);
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
}
