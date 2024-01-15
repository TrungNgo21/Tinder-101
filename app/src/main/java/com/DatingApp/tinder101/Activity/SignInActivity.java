package com.DatingApp.tinder101.Activity;

import static com.DatingApp.tinder101.Activity.RegisterActivity.EMAIL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;

import org.w3c.dom.Text;

import java.util.Arrays;

import javax.security.auth.callback.Callback;

public class SignInActivity extends AppCompatActivity {
  private UserService userService;
  private ActivitySignInBinding activitySignInBinding;

  private  CallbackManager callbackManager;

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
    LoginButton fbLogin = (LoginButton) findViewById(R.id.login_button);
    TextView toSignupPage = findViewById(R.id.toSignupButton);
    toSignupPage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
        startActivity(intent);
      }
    });

    setUpButton();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  public void setUpButton() {
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
