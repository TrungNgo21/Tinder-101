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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.R;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.relex.circleindicator.CircleIndicator3;

public class UserCardsHolderAdapter
        extends RecyclerView.Adapter<UserCardsHolderAdapter.CardStackViewHolder>
        implements UserCardAdapter.OnImageTap {
  private Context context;
  private List<UserDto> users;

  private List<UserCardAdapter> userCardAdapters = new ArrayList<>();

  private AtomicInteger currentPosition = new AtomicInteger();
  private UserCardAdapter userCardAdapter;
  private OnTapDetail onTapDetail;
  private UserDto user;

  public UserCardsHolderAdapter(Context context, OnTapDetail onTapDetail) {
    this.context = context;
    this.onTapDetail = onTapDetail;
  }

  public void setData(List<UserDto> users) {
    this.users = users;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public CardStackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cards_holder, parent, false);
    return new CardStackViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CardStackViewHolder holder, int position) {
    user = users.get(position);
    userCardAdapter = new UserCardAdapter(context, UserCardsHolderAdapter.this);
    userCardAdapters.add(userCardAdapter);
    userCardAdapter.setData(user);
    holder.adapterViewFlipper.setAdapter(userCardAdapter);
    holder.circleIndicator3.setViewPager(holder.adapterViewFlipper);
    holder.adapterViewFlipper.setUserInputEnabled(false);
    holder.adapterViewFlipper.setOnTouchListener(
            new View.OnTouchListener() {
              GestureDetector gestureDetector =
                      new GestureDetector(
                              context,
                              new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onSingleTapUp(@NonNull MotionEvent event) {
                                  if (event.getRawX() >= holder.adapterViewFlipper.getWidth() / 2) {
                                    if (currentPosition.get() == user.getImageUrlsMap().size() - 1) {
                                      currentPosition.set(user.getImageUrlsMap().size() - 1);
                                    } else {
                                      holder.adapterViewFlipper.setCurrentItem(currentPosition.get() + 1);
                                      currentPosition.getAndIncrement();
                                    }
                                  } else {
                                    if (currentPosition.get() == 0) {
                                      currentPosition.set(0);
                                    } else {
                                      holder.adapterViewFlipper.setCurrentItem(currentPosition.get() - 1);
                                      currentPosition.getAndDecrement();
                                    }
                                  }
                                  return super.onSingleTapUp(event);
                                }
                              });

              @Override
              public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
              }
            });
  }

  @Override
  public int getItemCount() {
    if (users != null) {
      return users.size();
    }
    return 0;
  }

  public class CardStackViewHolder extends RecyclerView.ViewHolder {
    private ViewPager2 adapterViewFlipper;
    private CircleIndicator3 circleIndicator3;

    public CardStackViewHolder(@NonNull View itemView) {
      super(itemView);
      adapterViewFlipper = itemView.findViewById(R.id.userCardsHolder);
      circleIndicator3 = itemView.findViewById(R.id.imageIndicators);
    }
  }

  public interface OnTapDetail {
    void showDetail(UserDto userDto);
  }

  @Override
  public void tapDown(UserDto userDto) {
    onTapDetail.showDetail(userDto);
  }
}