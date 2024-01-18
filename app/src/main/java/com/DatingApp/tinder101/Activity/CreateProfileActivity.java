package com.DatingApp.tinder101.Activity;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.util.Arrays.asList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.Utils.InputValidation;
import com.DatingApp.tinder101.databinding.ActivityCreateProfileBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateProfileActivity extends AppCompatActivity {
    private ActivityCreateProfileBinding activityCreateProfileBinding;

    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateProfileBinding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(activityCreateProfileBinding.getRoot());
        setInputText();
        setUpSpinner();
        setUpButton();
        initialCheck();
        userService = new UserService(getApplicationContext());

    }

    public void setInputText(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(activityCreateProfileBinding.nameId.getText().toString())){
                    activityCreateProfileBinding.errorName.setVisibility(View.VISIBLE);
                    activityCreateProfileBinding.continueButtonId.setEnabled(false);
                }

                if(TextUtils.isEmpty(activityCreateProfileBinding.ageId.getText().toString())){
                    activityCreateProfileBinding.errorAge.setVisibility(View.VISIBLE);
                    activityCreateProfileBinding.continueButtonId.setEnabled(false);
                }
                else {
                    if(Integer.parseInt(activityCreateProfileBinding.ageId.getText().toString()) == 0){
                        activityCreateProfileBinding.errorAge.setVisibility(View.VISIBLE);
                        activityCreateProfileBinding.continueButtonId.setEnabled(false);
                    }
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(activityCreateProfileBinding.nameId.getText().toString())){
                    activityCreateProfileBinding.errorName.setVisibility(View.INVISIBLE);
                    activityCreateProfileBinding.continueButtonId.setEnabled(true);
                }
                if(!TextUtils.isEmpty(activityCreateProfileBinding.ageId.getText().toString())){
                    if(Integer.parseInt(activityCreateProfileBinding.ageId.getText().toString()) != 0){
                        activityCreateProfileBinding.errorAge.setVisibility(View.INVISIBLE);
                        activityCreateProfileBinding.continueButtonId.setEnabled(true);
                    }
                }

            }
        };
        activityCreateProfileBinding.nameId.addTextChangedListener(textWatcher);
        activityCreateProfileBinding.ageId.addTextChangedListener(textWatcher);
    }
    public void setUpSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityCreateProfileBinding.genderCate.setAdapter(adapter);
        activityCreateProfileBinding.genderCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initialCheck(){
        if(TextUtils.isEmpty(activityCreateProfileBinding.nameId.getText().toString())){
            activityCreateProfileBinding.continueButtonId.setEnabled(false);
        }
    }
    public void setUpButton(){
        activityCreateProfileBinding.continueButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = activityCreateProfileBinding.nameId.getText().toString().trim();
                String gender = activityCreateProfileBinding.genderCate.getSelectedItem().toString();
                int age = Integer.parseInt(activityCreateProfileBinding.ageId.getText().toString());
                userService.updateName(name, new FirebaseCallback<CallbackRes<UserDto>>() {
                    @Override
                    public void callback(CallbackRes<UserDto> template) {
                        if(template instanceof CallbackRes.Success){
                            userService.updateGender(gender, new FirebaseCallback<CallbackRes<UserDto>>() {
                                @Override
                                public void callback(CallbackRes<UserDto> template) {
                                    if(template instanceof CallbackRes.Success){
                                        userService.updateAge(age, new FirebaseCallback<CallbackRes<UserDto>>() {
                                            @Override
                                            public void callback(CallbackRes<UserDto> template) {
                                                if(template instanceof CallbackRes.Success){
                                                    finish();
                                                    startActivity(new Intent(getApplicationContext(), AddPhotoActivity.class));
                                                }
                                                else {
                                                    Toast.makeText(CreateProfileActivity.this, template.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(CreateProfileActivity.this, template.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(CreateProfileActivity.this, template.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}