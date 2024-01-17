package com.DatingApp.tinder101.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.DatingApp.tinder101.Adapter.CarouselAdapter;
import com.DatingApp.tinder101.Fragments.LikeAndDislikeButtonFragment;
import com.DatingApp.tinder101.R;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class CarouselActivity extends AppCompatActivity {

    List<String> carouselImageLists;

    private ViewPager2 viewPager2;
    private SpringDotsIndicator dotsLayout;
    private CarouselAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carousel);

        viewPager2 = findViewById(R.id.view_pager_2);
        dotsLayout = findViewById(R.id.spring_dots_indicator);

        carouselImageLists = new ArrayList<>();
        carouselImageLists.add("https://i.pinimg.com/564x/d5/0f/a6/d50fa66d789f3859dc853a24f1ab34a6.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/6b/d2/5f/6bd25f0de1438509734fe5d5d7735756.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/89/23/90/8923902c4a43b2413fd417e0cfcabff6.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/f4/0e/9c/f40e9c6a116ac4cf2eb8e09cbb0858df.jpg");
        carouselImageLists.add("https://i.pinimg.com/564x/22/e4/5f/22e45f8c9c1fcfa487fca6129f118fc1.jpg");

        adapter = new CarouselAdapter(carouselImageLists);
        viewPager2.setAdapter(adapter);
        dotsLayout.attachTo(viewPager2);
    }
}