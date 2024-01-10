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
import com.DatingApp.tinder101.Model.User;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ActivitySignInBinding;
import com.google.firebase.FirebaseApp;

import javax.security.auth.callback.Callback;

public class SignInActivity extends AppCompatActivity {
  private UserService userService;
  private ActivitySignInBinding activitySignInBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());

    userService = new UserService(getApplicationContext());
    setContentView(activitySignInBinding.getRoot());
    if (userService.getLogInStatus()) {
      finish();
      startActivity(new Intent(this, MainActivity.class));
    }
    setUpButton();
  }

  public void setUpButton() {
    activitySignInBinding.toRegister.setOnClickListener(
        view -> {
          finish();
          startActivity(new Intent(this, RegisterActivity.class));
        });
    activitySignInBinding.loginBtn.setOnClickListener(
        view -> {
          userService.login(
              activitySignInBinding.username.getText().toString(),
              activitySignInBinding.password.getText().toString(),
              new FirebaseCallback<CallbackRes<UserDto>>() {
                @Override
                public void callback(CallbackRes<UserDto> res) {
                  if (res instanceof CallbackRes.Success) {
                    UserDto currentUser = ((CallbackRes.Success<UserDto>) res).getData();
                    userService.setCurrentUser(currentUser);
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
