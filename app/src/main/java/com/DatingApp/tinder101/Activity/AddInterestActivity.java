package com.DatingApp.tinder101.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.InterestAdapter;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserProfileService;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.Utils.SpacingItemDecoration;
import com.DatingApp.tinder101.databinding.ActivityAddInterestBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Random;

public class AddInterestActivity extends AppCompatActivity {
    private ActivityAddInterestBinding activityAddInterestBinding;
    private UserService userService;
    private UserProfileService userProfileService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddInterestBinding = ActivityAddInterestBinding.inflate(getLayoutInflater());
        setContentView(activityAddInterestBinding.getRoot());
        activityAddInterestBinding.choiceAnnounce.setVisibility(View.INVISIBLE);

        userService = new UserService(this);
        userProfileService = new UserProfileService(userService);
        setUpChipGroup();

    }
    public void setUpChipGroup(){
        for(String interest : Constant.interests){
            TextView choiceAnnounce = findViewById(R.id.choiceAnnounce);
            Chip chipView = (Chip) LayoutInflater.from(this).inflate(R.layout.custom_chip_v2, null);
            chipView.setText(interest);
            chipView.setId(new Random().nextInt());
            float padding = getResources().getDimension(com.intuit.sdp.R.dimen._8sdp);
//            chipView.setPadding((int)padding, (int)padding, (int)padding, (int)padding);
//            float width = getResources().getDimension(com.intuit.sdp.R.dimen._140sdp);
//            chipView.setLayoutParams(new ViewGroup.LayoutParams((int)width, ViewGroup.LayoutParams.WRAP_CONTENT));
            chipView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chipView.setTypeface(null, Typeface.BOLD);
            activityAddInterestBinding.interestGroup.addView(chipView);

            activityAddInterestBinding.interestGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                    for(int i = 0; i < activityAddInterestBinding.interestGroup.getChildCount(); i++){
                        View child = activityAddInterestBinding.interestGroup.getChildAt(i);
                        Chip chip = (Chip) child;
                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){

                                    List<Integer> ids = chipGroup.getCheckedChipIds();
                                    if(ids.size() > 5){
                                        chip.setChecked(false);
                                        choiceAnnounce.setText("You have selected " +  userProfileService.getInterests().size());
                                        activityAddInterestBinding.choiceAnnounce.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        userProfileService.appendInterests(chip.getText().toString());
                                        Log.d("Chip selected", chip.getText().toString());

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