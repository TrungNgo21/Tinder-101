package com.DatingApp.tinder101.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCardsHolderAdapter extends BaseAdapter implements UserCardAdapter.OnImageTap {
  private Context context;
  private List<UserDto> users;
  private ViewPager2 adapterViewFlipper;
  private AtomicInteger currentPosition = new AtomicInteger();
  private UserCardAdapter userCardAdapter;
  private UserDto user;

  private boolean isLike;

  public UserCardsHolderAdapter(Context context) {
    this.context = context;
  }

  public void setData(List<UserDto> users) {
    this.users = users;
  }

  @Override
  public int getCount() {
    if (users != null) {
      return users.size();
    }
    return 0;
  }

  @Override
  public Object getItem(int position) {
    return users.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cards_holder, parent, false);
    adapterViewFlipper = convertView.findViewById(R.id.userCardsHolder);

    user = users.get(position);
    userCardAdapter = new UserCardAdapter(context, this);
    userCardAdapter.setData(user);
    userCardAdapter.setIsDislikeDisplay(!isLike);
    userCardAdapter.setIsLikeDisplay(isLike);
    adapterViewFlipper.setAdapter(userCardAdapter);
    adapterViewFlipper.setUserInputEnabled(false);

    return convertView;
  }

  public void updateLike(boolean isDisplay) {
    this.isLike = isDisplay;
    userCardAdapter.setIsLikeDisplay(isLike);
  }

  public void updateDislike(boolean isDisplay) {
    this.isLike = isDisplay;
    userCardAdapter.setIsDislikeDisplay(isLike);
  }

  @Override
  public void tapRight() {
    if (currentPosition.get() == user.getImageUrlsMap().size() - 1) {
      currentPosition.set(user.getImageUrlsMap().size() - 1);
    } else {
      adapterViewFlipper.setCurrentItem(currentPosition.get() + 1);
      currentPosition.getAndIncrement();
    }
  }

  @Override
  public void tapLeft() {
    if (currentPosition.get() == 0) {
      currentPosition.set(0);
    } else {
      adapterViewFlipper.setCurrentItem(currentPosition.get() - 1);
      currentPosition.getAndDecrement();
    }
  }
}
