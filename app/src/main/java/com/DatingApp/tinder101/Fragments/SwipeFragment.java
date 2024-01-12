package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.DatingApp.tinder101.Adapter.UserCardAdapter;
import com.DatingApp.tinder101.Adapter.UserCardsHolderAdapter;
import com.DatingApp.tinder101.Dto.UserDto;

import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.databinding.FragmentSwipeBinding;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.Collections;
import java.util.List;

public class SwipeFragment extends Fragment {

  private FragmentSwipeBinding fragmentSwipeBinding;
  private List<UserDto> users;

  private UserCardsHolderAdapter userCardsHolderAdapter;

  private float draggingValue = 0f;
  private boolean isLike;

  public SwipeFragment() {}

  public SwipeFragment(List<UserDto> users) {
    // Required empty public constructor
    this.users = users;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentSwipeBinding = FragmentSwipeBinding.inflate(getLayoutInflater());
    userCardsHolderAdapter = new UserCardsHolderAdapter(requireContext());
    userCardsHolderAdapter.setData(users);
    SwipeFlingAdapterView swipeFlingAdapterView = fragmentSwipeBinding.swipeScreen;
    swipeFlingAdapterView.setAdapter(userCardsHolderAdapter);

    swipeFlingAdapterView.setFlingListener(
        new SwipeFlingAdapterView.onFlingListener() {
          @Override
          public void removeFirstObjectInAdapter() {}

          @Override
          public void onLeftCardExit(Object o) {}

          @Override
          public void onRightCardExit(Object o) {}

          @Override
          public void onAdapterAboutToEmpty(int i) {}

          @Override
          public void onScroll(float v) {
            View view = swipeFlingAdapterView.getSelectedView();
            Log.d("latitude", String.valueOf(v));
            draggingValue = v;
            view.findViewById(R.id.likeMask).setAlpha(isLike ? draggingValue : 0);
            view.findViewById(R.id.dislikeMask).setAlpha(!isLike ? -draggingValue : 0);
          }
        });
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return fragmentSwipeBinding.getRoot();
  }
}
