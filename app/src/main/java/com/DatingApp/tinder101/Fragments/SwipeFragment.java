package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.DatingApp.tinder101.Adapter.RoundChatItemAdapter;
import com.DatingApp.tinder101.Adapter.UserCardAdapter;
import com.DatingApp.tinder101.Adapter.UserCardsHolderAdapter;
import com.DatingApp.tinder101.Dto.UserDto;

import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.FragmentSwipeBinding;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.internal.CardStackState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SwipeFragment extends Fragment
    implements UserCardsHolderAdapter.OnTapDetail, ViewProfileFragment.OnSwipeButton {

  private FragmentSwipeBinding fragmentSwipeBinding;
  private List<UserDto> users;

  private CardStackView swipeFlingAdapterView;

  private UserCardsHolderAdapter userCardsHolderAdapter;

  private CardStackLayoutManager cardStackLayoutManager;

  private UserService userService;

  private View dislikeView;
  private View likeView;

  private boolean isLike;

  private boolean isDislike;

  public SwipeFragment() {}

  public SwipeFragment(List<UserDto> users) {
    // Required empty public constructor
    this.users = users;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userService = new UserService(requireContext());
    fragmentSwipeBinding = FragmentSwipeBinding.inflate(getLayoutInflater());
    userCardsHolderAdapter = new UserCardsHolderAdapter(requireContext(), this);
    userCardsHolderAdapter.setData(users);
    swipeFlingAdapterView = fragmentSwipeBinding.swipeScreen;
    cardStackLayoutManager =
        new CardStackLayoutManager(
            requireContext(),
            new CardStackListener() {

              @Override
              public void onCardDragging(Direction direction, float ratio) {
                if (direction == Direction.Right) {
                  isLike = true;
                  isDislike = false;
                  fragmentSwipeBinding.likeIcon.setImageResource(R.drawable.like_icon_on);
                  fragmentSwipeBinding.likeBtn.setBackgroundResource(R.drawable.bg_like);
                  fragmentSwipeBinding.dislikeIcon.setImageResource(R.drawable.dislike_icon_new);
                  fragmentSwipeBinding.dislikeBtn.setBackgroundResource(R.drawable.bg_dislike_norm);

                } else if (direction == Direction.Left) {
                  isLike = false;
                  isDislike = true;
                  fragmentSwipeBinding.dislikeIcon.setImageResource(R.drawable.dislike_icon_on);
                  fragmentSwipeBinding.dislikeBtn.setBackgroundResource(R.drawable.bg_dislike);
                  fragmentSwipeBinding.likeIcon.setImageResource(R.drawable.like_icon_new);
                  fragmentSwipeBinding.likeBtn.setBackgroundResource(R.drawable.bg_dislike_norm);
                }

                likeView.setVisibility(isLike ? View.VISIBLE : View.INVISIBLE);
                dislikeView.setVisibility(isDislike ? View.VISIBLE : View.INVISIBLE);
              }

              @Override
              public void onCardSwiped(Direction direction) {
                int currentPos = cardStackLayoutManager.getTopPosition();
                if (direction == Direction.Right) {
                  userService.rightSwipe(users.get(currentPos - 1).getId());
                  users.remove(0);
                  if (users.isEmpty()) {
                    fragmentSwipeBinding.likeDislikeBtn.setVisibility(View.GONE);
                  } else {
                    fragmentSwipeBinding.likeDislikeBtn.setVisibility(View.VISIBLE);
                  }
                  userCardsHolderAdapter.notifyItemRemoved(0);
                } else {
                  users.remove(0);
                  if (users.isEmpty()) {
                    fragmentSwipeBinding.likeDislikeBtn.setVisibility(View.GONE);
                  } else {
                    fragmentSwipeBinding.likeDislikeBtn.setVisibility(View.VISIBLE);
                  }
                  userCardsHolderAdapter.notifyItemRemoved(0);
                  Log.d("delete ne", "hehee");
                }
              }

              @Override
              public void onCardRewound() {
                Log.d("rewound r ne", "heehee");
              }

              @Override
              public void onCardCanceled() {
                likeView.setVisibility(View.INVISIBLE);
                dislikeView.setVisibility(View.INVISIBLE);
                fragmentSwipeBinding.likeIcon.setImageResource(R.drawable.like_icon_new);
                fragmentSwipeBinding.likeBtn.setBackgroundResource(R.drawable.bg_dislike_norm);
                fragmentSwipeBinding.dislikeIcon.setImageResource(R.drawable.dislike_icon_new);
                fragmentSwipeBinding.dislikeBtn.setBackgroundResource(R.drawable.bg_dislike_norm);
              }

              @Override
              public void onCardAppeared(View view, int position) {
                likeView = view.findViewById(R.id.likeMask);
                dislikeView = view.findViewById(R.id.dislikeMask);
              }

              @Override
              public void onCardDisappeared(View view, int position) {
                Log.d("position", String.valueOf(position));
              }
            });

    cardStackLayoutManager.setCanScrollHorizontal(true);
    cardStackLayoutManager.setCanScrollVertical(false);
    cardStackLayoutManager.setMaxDegree(40.0f);
    cardStackLayoutManager.setStackFrom(StackFrom.Bottom);
    cardStackLayoutManager.setTranslationInterval(4.0f);
    swipeFlingAdapterView.setAdapter(userCardsHolderAdapter);
    swipeFlingAdapterView.setLayoutManager(cardStackLayoutManager);
  }

  private void setDesireButton() {
    if (users.isEmpty()) {
      fragmentSwipeBinding.likeDislikeBtn.setVisibility(View.GONE);
    } else {
      fragmentSwipeBinding.likeDislikeBtn.setVisibility(View.VISIBLE);
    }
    fragmentSwipeBinding.likeBtn.setOnClickListener(
        view -> {
          users.remove(0);
          swipeFlingAdapterView.swipe();
          Toast.makeText(requireContext(), "You swipe right!!!", Toast.LENGTH_SHORT).show();
          userService.rightSwipe(users.get(cardStackLayoutManager.getTopPosition()).getId());
        });
    fragmentSwipeBinding.dislikeBtn.setOnClickListener(
        view -> {
          swipeFlingAdapterView.swipe();
          users.remove(0);
          userCardsHolderAdapter.notifyItemRemoved(0);
          Toast.makeText(requireContext(), "You swipe left!!!", Toast.LENGTH_SHORT).show();
        });
  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.mainView, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void swipeLeft() {
    swipeFlingAdapterView.swipe();
    users.remove(0);
    userCardsHolderAdapter.notifyItemRemoved(0);
    Toast.makeText(requireContext(), "You swipe left!!!", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void swipeRight(String userId) {
    users.remove(0);
    swipeFlingAdapterView.swipe();
    Toast.makeText(requireContext(), "You swipe right!!!", Toast.LENGTH_SHORT).show();
    userService.rightSwipe(userId);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    setDesireButton();

    // Inflate the layout for this fragment
    return fragmentSwipeBinding.getRoot();
  }

  @Override
  public void showDetail(UserDto userDto) {
    ViewProfileFragment viewProfileFragment = new ViewProfileFragment(userDto, users, true, this);
    loadFragment(viewProfileFragment);
  }
}
