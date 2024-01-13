package com.DatingApp.tinder101.Activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.adapter.CarouselAdapter;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    List<String> carouselImageLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carousel);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        carouselImageLists = new ArrayList<>();

        carouselImageLists.add("https://i.pinimg.com/564x/d5/0f/a6/d50fa66d789f3859dc853a24f1ab34a6.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/6b/d2/5f/6bd25f0de1438509734fe5d5d7735756.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/89/23/90/8923902c4a43b2413fd417e0cfcabff6.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/f4/0e/9c/f40e9c6a116ac4cf2eb8e09cbb0858df.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/22/e4/5f/22e45f8c9c1fcfa487fca6129f118fc1.jpg");


        viewPager.setAdapter(new CarouselAdapter(this, carouselImageLists));

        autoImageSlide();
    }

    private void autoImageSlide() {
        final long DELAY_MS = 3000;
        final long PERIOD_MS = 3000;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(viewPager.getCurrentItem() == carouselImageLists.size() - 1) {
                    viewPager.setCurrentItem(0);
                }else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
            }
        };

        Timer timer = new Time();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, DELAY_MS, PERIOD_MS);
    }
}