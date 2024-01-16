package com.DatingApp.tinder101.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.InterestAdapter;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Utils.SpacingItemDecoration;
import com.DatingApp.tinder101.databinding.ActivityAddInterestBinding;

public class AddInterestActivity extends AppCompatActivity {
    private RecyclerView revInterest;
    private InterestAdapter interestAdapter;
    private ActivityAddInterestBinding activityAddInterestBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddInterestBinding = ActivityAddInterestBinding.inflate(getLayoutInflater());
        setContentView(activityAddInterestBinding.getRoot());
        interestAdapter = new InterestAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        activityAddInterestBinding.interestRec.setLayoutManager(gridLayoutManager);
        SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(50);
        activityAddInterestBinding.interestRec.addItemDecoration(spacingItemDecoration);
        interestAdapter.setData(Constant.interests);
        activityAddInterestBinding.interestRec.setAdapter(interestAdapter);
    }
}