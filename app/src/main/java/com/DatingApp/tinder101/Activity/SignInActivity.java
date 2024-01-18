package com.DatingApp.tinder101.Activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.DatingApp.tinder101.Fragments.LoadingComponent;
import com.DatingApp.tinder101.Model.User;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.Utils.CustomToast;
import com.DatingApp.tinder101.Utils.InputValidation;
import com.DatingApp.tinder101.databinding.ActivitySignInBinding;
import com.google.firebase.FirebaseApp;

import org.w3c.dom.Text;

import java.util.Arrays;

import javax.security.auth.callback.Callback;

public class SignInActivity extends AppCompatActivity implements UserService.CallbackListener {
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


      LoadingComponent loadingComponent = findViewById(R.id.loadingView);
    loadingComponent.hideLoading();

    setUpButton();
    setErrorMessage(activitySignInBinding.authenticatedFailedId, false);
    setLoading(false);
    TextWatcher inputListener = setInputListener();
    activitySignInBinding.emailId.addTextChangedListener(inputListener);
    activitySignInBinding.passwordId.addTextChangedListener(inputListener);
  }
  private void setLoading(boolean isLoading) {
    if (isLoading) {
      activitySignInBinding.progressBarId.setVisibility(View.VISIBLE);
    } else {
      activitySignInBinding.progressBarId.setVisibility(View.INVISIBLE);
    }
  }
  private void setErrorMessage(TextView errorMessage, boolean isOn) {
    if (isOn) {
      errorMessage.setVisibility(View.VISIBLE);
    } else {
      errorMessage.setVisibility(View.INVISIBLE);
    }
  }
  private TextWatcher setInputListener() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        activitySignInBinding.loginButtonId.setEnabled(
                !activitySignInBinding.emailId.getText().toString().isEmpty()
                        && !activitySignInBinding.passwordId.getText().toString().isEmpty());
        if(activitySignInBinding.emailId.getText().toString().isEmpty()){
            activitySignInBinding.errorEmail.setVisibility(View.VISIBLE);
        }
        if(activitySignInBinding.passwordId.getText().toString().isEmpty()){
          activitySignInBinding.errorPassword.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}

      @Override
      public void afterTextChanged(Editable s) {
        activitySignInBinding.loginButtonId.setEnabled(
                !activitySignInBinding.emailId.getText().toString().isEmpty()
                        && !activitySignInBinding.passwordId.getText().toString().isEmpty());
        if(InputValidation.isValidEmail(activitySignInBinding.emailId.getText().toString())){
          activitySignInBinding.errorEmail.setVisibility(View.INVISIBLE);
        }
        if(!activitySignInBinding.passwordId.getText().toString().isEmpty()){
          activitySignInBinding.errorPassword.setVisibility(View.INVISIBLE);
        }
      }

    };
  }




  public void setUpButton() {
    activitySignInBinding.loginButtonId.setOnClickListener(
        view -> {
          setLoading(true);
          setErrorMessage(activitySignInBinding.authenticatedFailedId, false);
          userService.login(
              activitySignInBinding.emailId.getText().toString(),
              activitySignInBinding.passwordId.getText().toString(),

              new FirebaseCallback<CallbackRes<UserDto>>() {
                @Override
                public void callback(CallbackRes<UserDto> res) {
                  if (res instanceof CallbackRes.Success) {
                    UserDto currentUser = ((CallbackRes.Success<UserDto>) res).getData();
                    userService.setCurrentUser(currentUser);
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    setLoading(false);
                  } else {
                    setLoading(false);
                    setErrorMessage(activitySignInBinding.authenticatedFailedId,true);
//                    Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_LONG)
//                        .show();
                  }
                }
              }, this);
        });
    activitySignInBinding.toSignUpButtonId.setOnClickListener(
            view -> {
              startActivity(new Intent(this, RegisterActivity.class));
            });
  }

  @Override
  public void onSuccessCallBack(String message) {

  }

  @Override
  public void onFailureCallBack(String message) {
    CustomToast.makeText(this, CustomToast.SHORT,"Error", message).show();
  }
}
