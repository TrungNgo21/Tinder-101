package com.DatingApp.tinder101.Activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserProfileService;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ActivityAddInterestBinding;
import com.DatingApp.tinder101.databinding.ActivityProfileBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding activityProfileBinding;

    private Dialog dialog;
    private UserService userService;
    private UserProfileService userProfileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        userService = new UserService(getApplicationContext());
        userProfileService = new UserProfileService(userService);
        dialog = new Dialog(getApplicationContext());


    }


    public void showBottomSheetDialog(boolean isInterest, boolean isBasics) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView cancel = dialog.findViewById(R.id.cancel_button);
        TextView done = dialog.findViewById(R.id.done_button);

        ChipGroup group1 = dialog.findViewById(R.id.chip_group_1);
        ChipGroup group2 = dialog.findViewById(R.id.chip_group_2);
        ChipGroup group3 = dialog.findViewById(R.id.chip_group_3);
        ChipGroup group4 = dialog.findViewById(R.id.chip_group_4);

        TextView title1 = dialog.findViewById(R.id.chip_group_title_1);
        TextView title2 = dialog.findViewById(R.id.chip_group_title_2);
        TextView title3 = dialog.findViewById(R.id.chip_group_title_3);
        TextView title4 = dialog.findViewById(R.id.chip_group_title_4);
        TextView bottomSheetTitle = dialog.findViewById(R.id.bottom_sheet_title);




        if (isInterest) {
            dialog.setContentView(R.layout.bottom_sheet_interest);

            setUpInterestChipGroup();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (isBasics) {
            dialog.setContentView(R.layout.bottom_sheet_layout);

            setUpChipGroup(group1, Constant.zodiacs, "ZODIAC");
            setUpChipGroup(group2, Constant.educations, "EDUCATION");
            setUpChipGroup(group3, Constant.communications, "COMMUNICATION");
            setUpChipGroup(group4, Constant.loves, "LOVE");

            title1.setText("What is your zodiac sign ?");
            title2.setText("What is your education level ?");
            title3.setText("What is your communication style ?");
            title4.setText("How do you receive love ?");

            bottomSheetTitle.setText("More about me");

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            dialog.setContentView(R.layout.bottom_sheet_layout);
            setUpChipGroup(group1, Constant.pets, "PET");
            setUpChipGroup(group2, Constant.drinks, "DRINKING");
            setUpChipGroup(group3, Constant.smokes, "SMOKE");
            setUpChipGroup(group4, Constant.workouts, "WORKOUT");

            title1.setText("DO you have any pets ?");
            title2.setText("How often do you drink ?");
            title3.setText("How often do you smoke ?");
            title4.setText("Do you exercise ?");

            bottomSheetTitle.setText("Lifestyle");
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileService.updateUserProfileSetting();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void closeDialog() {
        dialog.cancel();
    }


    public void setUpChipGroup(ChipGroup chipGroup, List<String> list, String basicType){
        for(String value : list){
            String basicValue = userProfileService.getBasics().get(basicType);
            Chip chipView = (Chip) LayoutInflater.from(this).inflate(R.layout.custom_chip_v2, null);
            if (value.equals(basicValue)) {
                chipView.setChecked(true);
            }
            chipView.setText(value);
            chipView.setId(new Random().nextInt());
            float padding = getResources().getDimension(com.intuit.sdp.R.dimen._8sdp);
            chipView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chipView.setTypeface(null, Typeface.BOLD);
            chipGroup.addView(chipView);
            chipGroup.setSingleSelection(true);
            chipView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        userProfileService.appendBasics(basicType, chipView.getText().toString());
                    } else {
                        userProfileService.removeBasic(basicType);
                    }
                }
            });
        }
    }

    public void setUpInterestChipGroup() {
        for(String interest : Constant.interests){
            Chip chipView = (Chip) LayoutInflater.from(this).inflate(R.layout.custom_chip_v2, null);
            chipView.setText(interest);
            chipView.setId(new Random().nextInt());
            float padding = getResources().getDimension(com.intuit.sdp.R.dimen._8sdp);
            chipView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chipView.setTypeface(null, Typeface.BOLD);
            activityProfileBinding.interestChipGroup.addView(chipView);

            activityProfileBinding.interestChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                    for(int i = 0; i < activityProfileBinding.interestChipGroup.getChildCount(); i++){
                        View child = activityProfileBinding.interestChipGroup.getChildAt(i);
                        Chip chip = (Chip) child;
                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    List<Integer> ids = chipGroup.getCheckedChipIds();
                                    if(ids.size() > 5){
                                        chip.setChecked(false);
                                    }
                                    else {
                                        userProfileService.appendInterests(chip.getText().toString());
                                    }
                                } else {
                                    userProfileService.removeInterests(chip.getText().toString());
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}