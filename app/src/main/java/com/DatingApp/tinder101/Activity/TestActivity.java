package com.DatingApp.tinder101.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.DatingApp.tinder101.Fragments.LikeAndDislikeButtonFragment;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Adapter.CarouselAdapter;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    ImageView likeBtn;
    ImageView dislikeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LikeAndDislikeButtonFragment likeDislikeFragment = new LikeAndDislikeButtonFragment();
        fragmentTransaction.add(R.id.fragmentContainer, likeDislikeFragment); // R.id.fragmentContainer is the ID of the container view in your activity layout
        fragmentTransaction.commit();

//        likeBtn.setOnClickListener;

    }
}