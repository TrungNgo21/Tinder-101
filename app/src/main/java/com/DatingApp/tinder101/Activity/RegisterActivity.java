package com.DatingApp.tinder101.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ActivityRegisterBinding;
import com.DatingApp.tinder101.databinding.ActivitySignInBinding;
import com.google.firebase.FirebaseApp;

public class RegisterActivity extends AppCompatActivity {

  private UserService userService;
  private ActivityRegisterBinding activityRegisterBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
    setContentView(activityRegisterBinding.getRoot());
    userService = new UserService(getApplicationContext());
    setUpButton();
  }

  public void setUpButton() {
    activityRegisterBinding.registerBtn.setOnClickListener(
        view -> {
          userService.register(
              activityRegisterBinding.username.getText().toString(),
              activityRegisterBinding.password.getText().toString(),
              new FirebaseCallback<CallbackRes<UserDto>>() {
                @Override
                public void callback(CallbackRes<UserDto> res) {
                  if (res instanceof CallbackRes.Success) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  } else {
                    Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_LONG)
                        .show();
                  }
                }
              });
        });
  }
}
