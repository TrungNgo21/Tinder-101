package com.DatingApp.tinder101.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
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
import com.DatingApp.tinder101.Utils.CustomToast;
import com.DatingApp.tinder101.Utils.InputValidation;
import com.DatingApp.tinder101.databinding.ActivityRegisterBinding;
import com.DatingApp.tinder101.databinding.ActivitySignInBinding;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements UserService.CallbackListener {

  private UserService userService;
  private ActivityRegisterBinding activityRegisterBinding;
  FirebaseAuth mAuth;
  final private FirebaseFirestore db = FirebaseFirestore.getInstance();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
    setContentView(activityRegisterBinding.getRoot());
    setLoading(false);
    setErrorMessage(activityRegisterBinding.authenticatedFailedId,false);
    setInputListener();
    initialCheck();
    userService = new UserService(getApplicationContext());
    setUpButton();
  }
  private void setLoading(boolean isLoading) {
    if (isLoading) {
      activityRegisterBinding.progressBarId.setVisibility(View.VISIBLE);
    } else {
      activityRegisterBinding.progressBarId.setVisibility(View.INVISIBLE);
    }
  }
  private void setErrorMessage(TextView errorMessage, boolean isOn) {
    if (isOn) {
      errorMessage.setVisibility(View.VISIBLE);
    } else {
      errorMessage.setVisibility(View.INVISIBLE);
    }
  }
  public void initialCheck(){
    if(!InputValidation.isValidEmail(activityRegisterBinding.emailId.getText().toString().trim())){
      activityRegisterBinding.signUpButtonId.setEnabled(false);
    }
    if(!InputValidation.isValidPass(activityRegisterBinding.passwordId.getText().toString().trim(), 7, 10)){
      activityRegisterBinding.signUpButtonId.setEnabled(false);
    }

  }
  private void setInputListener(){
    TextWatcher textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(!InputValidation.isValidEmail(activityRegisterBinding.emailId.getText().toString().trim())){
          activityRegisterBinding.errorEmail.setVisibility(View.VISIBLE);
          activityRegisterBinding.signUpButtonId.setEnabled(false);
        }
        if(!InputValidation.isValidPass(activityRegisterBinding.passwordId.getText().toString().trim(), 7, 10)){
          activityRegisterBinding.errorPassword.setVisibility(View.VISIBLE);
          activityRegisterBinding.signUpButtonId.setEnabled(false);
        }
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if(InputValidation.isValidEmail(activityRegisterBinding.emailId.getText().toString().trim())){
          activityRegisterBinding.errorEmail.setVisibility(View.INVISIBLE);
          activityRegisterBinding.signUpButtonId.setEnabled(true);
        }
        if(InputValidation.isValidPass(activityRegisterBinding.passwordId.getText().toString().trim(), 7, 10)){
          activityRegisterBinding.errorPassword.setVisibility(View.INVISIBLE);
          activityRegisterBinding.signUpButtonId.setEnabled(true);
        }
      }
    };
    activityRegisterBinding.emailId.addTextChangedListener(textWatcher);
    activityRegisterBinding.passwordId.addTextChangedListener(textWatcher);

  }


  public void setUpButton() {
    activityRegisterBinding.signUpButtonId.setOnClickListener(
        view -> {
          setLoading(true);
          userService.register(
              activityRegisterBinding.emailId.getText().toString(),
              activityRegisterBinding.passwordId.getText().toString(),
              new FirebaseCallback<CallbackRes<UserDto>>() {
                @Override
                public void callback(CallbackRes<UserDto> res) {
                  if (res instanceof CallbackRes.Success) {
                    setLoading(false);
                    finish();
                    startActivity(new Intent(getApplicationContext(), CreateProfileActivity.class));
                  } else {
                    setLoading(false);
                    Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_LONG)
                        .show();
                  }
                }
              }, this);
        });
    activityRegisterBinding.toSignInButtonId.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));

      }
    });
  }

  @Override
  public void onSuccessCallBack(String message) {
    CustomToast.makeText(this, CustomToast.SHORT, "Success!", message).show();
  }

  @Override
  public void onFailureCallBack(String message) {
    CustomToast.makeText(this, CustomToast.SHORT, "Error!", message).show();
  }
}
