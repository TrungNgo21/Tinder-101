package com.DatingApp.tinder101.Activity;

import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.DatingApp.tinder101.Adapter.ConversationAdapter;
import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.MessageDto;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Fragments.ViewProfileFragment;
import com.DatingApp.tinder101.Model.Message;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.MessageService;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ActivityConversationBinding;
import com.DatingApp.tinder101.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

public class ConversationActivity extends AppCompatActivity {
  private ActivityConversationBinding activityConversationBinding;

  private final String TAG = "Conversation activity";

  private List<MessageDto> messages = new ArrayList<>();

  private ConversationAdapter conversationAdapter;
  private UserService userService;

  private MessageService messageService;
  private UserDto receivedUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityConversationBinding = ActivityConversationBinding.inflate(getLayoutInflater());
    userService = new UserService(this);
    messageService = new MessageService(this, userService);
    String receivedId = getIntent().getStringExtra(Constant.KEY_RECEIVED_USED_ID);
    setUpButton();
    userService.getOneUser(
        receivedId,
        new FirebaseCallback<CallbackRes<UserDto>>() {
          @Override
          public void callback(CallbackRes<UserDto> template) {
            if (template instanceof CallbackRes.Success) {
              receivedUser = ((CallbackRes.Success<UserDto>) template).getData();
              setUpReceivedUserDisplay();
              setUpMessage(receivedUser);

            } else {
              Log.d(TAG, "Fail get user");
            }
          }
        });
    setContentView(activityConversationBinding.getRoot());
  }

  private void setUpReceivedUserDisplay() {
    Picasso.get()
        .load(receivedUser.getImageUrlsMap().get("0"))
        .into(activityConversationBinding.receivedUserImage);
    activityConversationBinding.receivedUserName.setText(receivedUser.getName());
    activityConversationBinding.receivedUserAge.setText("19");
  }

  private void setUpMessage(UserDto receivedUser) {
    conversationAdapter = new ConversationAdapter(messages, receivedUser);
    activityConversationBinding.messageDisplay.setAdapter(conversationAdapter);
    messageService.setMessagesListener(
        receivedUser.getId(),
        messages,
        conversationAdapter,
        activityConversationBinding.messageDisplay);
  }

  private void setUpButton() {
    activityConversationBinding.viewMoreIcon.setOnClickListener(
        view -> {
          ViewProfileFragment viewProfileFragment = new ViewProfileFragment(receivedUser);
          viewProfileFragment.hideBackToSwipe();
          activityConversationBinding.profileDisplay.setVisibility(View.VISIBLE);
          activityConversationBinding.mainDisplay.setVisibility(View.GONE);
          loadFragment(viewProfileFragment);
        });

    activityConversationBinding.backBtn.setOnClickListener(
        view -> {
          finish();
        });
    activityConversationBinding.sendBtn.setOnClickListener(
        view -> {
          if (activityConversationBinding.messageChatBox.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please chat something!!!", Toast.LENGTH_SHORT)
                .show();
          } else {
            messageService.sentMessage(
                Message.builder()
                    .createDate(new Date())
                    .receivedUserId(receivedUser.getId())
                    .sentUserId(userService.getCurrentUser().getId())
                    .messageContent(activityConversationBinding.messageChatBox.getText().toString())
                    .build(),
                receivedUser,
                userService.getCurrentUser(),
                new FirebaseCallback<CallbackRes<MessageDto>>() {
                  @Override
                  public void callback(CallbackRes<MessageDto> template) {
                    if (template instanceof CallbackRes.Success) {
                      activityConversationBinding.messageChatBox.setText(null);
                    } else {
                      Toast.makeText(
                              getApplicationContext(), "Network error!!!", Toast.LENGTH_SHORT)
                          .show();
                    }
                  }
                });
          }
        });
  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.profileDisplay, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
