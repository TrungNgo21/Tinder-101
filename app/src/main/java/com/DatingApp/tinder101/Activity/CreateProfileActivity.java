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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.databinding.ActivityCreateProfileBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

public class CreateProfileActivity extends AppCompatActivity {
    private ActivityCreateProfileBinding activityCreateProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateProfileBinding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(activityCreateProfileBinding.getRoot());
        setInputText();
        setUpSpinner();
        setUpAutoCompleteAddress();

    }
    public void setUpAutoCompleteAddress() {
        Places.initialize(this, Constant.KEY_GOOGLE_MAP_API);
        Places.createClient(this);
        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autoCompleteAddress);
        //    AutocompleteSessionToken.newInstance();
        autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteSupportFragment.setPlaceFields(
                asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onError(@NonNull Status status) {}

                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                        activityCreateProfileBinding.addressDisplay.setText(place.getAddress());
                    }
                });
    }
    public void setInputText(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(TextUtils.isEmpty(activityCreateProfileBinding.nameId.getText().toString())){
                    activityCreateProfileBinding.errorName.setVisibility(View.VISIBLE);
                    activityCreateProfileBinding.continueButtonId.setEnabled(false);
                }
                if(TextUtils.isEmpty(activityCreateProfileBinding.phoneNumId.getText().toString())){
                    activityCreateProfileBinding.errorNum.setVisibility(View.VISIBLE);
                    activityCreateProfileBinding.continueButtonId.setEnabled(false);
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
                if(!TextUtils.isEmpty(activityCreateProfileBinding.phoneNumId.getText().toString())){
                    activityCreateProfileBinding.errorNum.setVisibility(View.INVISIBLE);
                    activityCreateProfileBinding.continueButtonId.setEnabled(true);
                }
            }
        };
        activityCreateProfileBinding.nameId.addTextChangedListener(textWatcher);
        activityCreateProfileBinding.phoneNumId.addTextChangedListener(textWatcher);
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
    public void setUpButton(){
        activityCreateProfileBinding.continueButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), AddPhotoActivity.class));
            }
        });
    }
}