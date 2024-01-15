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

public class SwipeFragment extends Fragment implements UserCardsHolderAdapter.OnTapDetail {

  private FragmentSwipeBinding fragmentSwipeBinding;
  private List<UserDto> users;

  private UserCardsHolderAdapter userCardsHolderAdapter;

  private CardStackLayoutManager cardStackLayoutManager;

  private OnMainEventHandle onMainTapDetail;

  private View dislikeView;
  private View likeView;

  private boolean isLike;

  private boolean isDislike;

  public SwipeFragment() {}

  public SwipeFragment(List<UserDto> users, OnMainEventHandle onMainTapDetail) {
    // Required empty public constructor
    this.users = users;
    this.onMainTapDetail = onMainTapDetail;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentSwipeBinding = FragmentSwipeBinding.inflate(getLayoutInflater());
    userCardsHolderAdapter = new UserCardsHolderAdapter(requireContext(), this);
    userCardsHolderAdapter.setData(users);
    CardStackView swipeFlingAdapterView = fragmentSwipeBinding.swipeScreen;
    CardStackState cardStackState = new CardStackState();

    cardStackLayoutManager =
        new CardStackLayoutManager(
            requireContext(),
            new CardStackListener() {

              @Override
              public void onCardDragging(Direction direction, float ratio) {
                Log.d(
                    "ratio",
                    direction + String.valueOf(ratio) + "  " + String.valueOf(cardStackState.dx));

                if (direction == Direction.Right) {
                  isLike = true;
                  isDislike = false;
                } else if (direction == Direction.Left) {
                  isLike = false;
                  isDislike = true;
                }

                likeView.setVisibility(isLike ? View.VISIBLE : View.INVISIBLE);

                dislikeView.setVisibility(isDislike ? View.VISIBLE : View.INVISIBLE);
              }

              @Override
              public void onCardSwiped(Direction direction) {
                onMainTapDetail.popCard();
              }

              @Override
              public void onCardRewound() {
                Log.d("rewound r ne", "heehee");
                likeView.setVisibility(View.INVISIBLE);
                dislikeView.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onCardCanceled() {
                likeView.setVisibility(View.INVISIBLE);
                dislikeView.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onCardAppeared(View view, int position) {
                likeView = view.findViewById(R.id.likeMask);
                dislikeView = view.findViewById(R.id.dislikeMask);
              }

              @Override
              public void onCardDisappeared(View view, int position) {}
            });

    cardStackLayoutManager.setCanScrollHorizontal(true);
    cardStackLayoutManager.setCanScrollVertical(false);
    cardStackLayoutManager.setDirections(
        new ArrayList<>(Arrays.asList(Direction.Left, Direction.Right)));
    cardStackLayoutManager.setMaxDegree(40.0f);
    cardStackLayoutManager.setStackFrom(StackFrom.Bottom);
    cardStackLayoutManager.setVisibleCount(3);
    cardStackLayoutManager.setTranslationInterval(4.0f);

    swipeFlingAdapterView.setAdapter(userCardsHolderAdapter);
    swipeFlingAdapterView.setLayoutManager(cardStackLayoutManager);
  }

  public interface OnMainEventHandle {
    void showDetail(UserDto userDto);

    void popCard();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return fragmentSwipeBinding.getRoot();
  }

  @Override
  public void showDetail(UserDto userDto) {
    onMainTapDetail.showDetail(userDto);
  }
}
