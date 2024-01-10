package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.Toast;

import com.DatingApp.tinder101.Adapter.UserCardAdapter;
import com.DatingApp.tinder101.Adapter.UserCardsHolderAdapter;
import com.DatingApp.tinder101.Dto.UserDto;

import com.DatingApp.tinder101.databinding.FragmentSwipeBinding;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.Collections;
import java.util.List;

public class SwipeFragment extends Fragment {

  private FragmentSwipeBinding fragmentSwipeBinding;
  private List<UserDto> users;

  private Double lastX;

  public SwipeFragment() {}

  public SwipeFragment(List<UserDto> users) {
    // Required empty public constructor
    this.users = users;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentSwipeBinding = FragmentSwipeBinding.inflate(getLayoutInflater());
    UserCardsHolderAdapter userCardsHolderAdapter = new UserCardsHolderAdapter(requireContext());
    userCardsHolderAdapter.setData(users);
    SwipeFlingAdapterView swipeFlingAdapterView = fragmentSwipeBinding.swipeScreen;
    swipeFlingAdapterView.setAdapter(userCardsHolderAdapter);
    //        .
    //        .setOnTouchListener(
    //            new View.OnTouchListener() {
    //              @Override
    //              public boolean onTouch(View v, MotionEvent event) {
    //                int action = event.getActionMasked();
    //                Log.d("fsdfdsf", String.valueOf(action));
    //                return false;
    //              }
    //            });

    swipeFlingAdapterView.setFlingListener(
        new SwipeFlingAdapterView.onFlingListener() {
          @Override
          public void removeFirstObjectInAdapter() {}

          @Override
          public void onLeftCardExit(Object o) {

            Toast.makeText(requireContext(), "dislike", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onRightCardExit(Object o) {

            Toast.makeText(requireContext(), "like", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onAdapterAboutToEmpty(int i) {}

          @Override
          public void onScroll(float v) {}
        });

    swipeFlingAdapterView.setOnItemClickListener(
        new SwipeFlingAdapterView.OnItemClickListener() {

          @Override
          public void onItemClicked(int i, Object o) {
            Log.d("fsdfds", o.toString());
            Toast.makeText(requireContext(), "data is " + "hihi", Toast.LENGTH_SHORT).show();
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
