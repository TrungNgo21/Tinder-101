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
<<<<<<< HEAD
import androidx.recyclerview.widget.RecyclerView;
=======
>>>>>>> aba3763 (feat: add 2 buttons to navigate)
import androidx.viewpager2.widget.ViewPager2;

import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.R;
<<<<<<< HEAD
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
=======

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
>>>>>>> aba3763 (feat: add 2 buttons to navigate)
  }

  public void setData(List<UserDto> users) {
    this.users = users;
<<<<<<< HEAD
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
=======
  }

  @Override
  public int getCount() {
>>>>>>> aba3763 (feat: add 2 buttons to navigate)
    if (users != null) {
      return users.size();
    }
    return 0;
  }

<<<<<<< HEAD
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
=======
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
>>>>>>> aba3763 (feat: add 2 buttons to navigate)
  }
}
