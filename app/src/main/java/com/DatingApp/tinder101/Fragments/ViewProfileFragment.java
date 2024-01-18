package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.DatingApp.tinder101.Adapter.ProfileChipsAdapter;
import com.DatingApp.tinder101.Adapter.ProfileImagesAdapter;
import com.DatingApp.tinder101.Adapter.ProfileItemAdapter;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Utils.EnumConverter;
import com.DatingApp.tinder101.databinding.FragmentViewProfileBinding;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import me.relex.circleindicator.CircleIndicator3;

public class ViewProfileFragment extends Fragment {
  private FragmentViewProfileBinding fragmentViewProfileBinding;

  private UserDto userDto;

  private List<UserDto> users;

  private ProfileImagesAdapter profileImagesAdapter;

  private OnSwipeButton onSwipeButton;

  private boolean isSwiping;

  public ViewProfileFragment(
      UserDto userDto, List<UserDto> users, boolean isSwiping, OnSwipeButton onSwipeButton) {
    this.userDto = userDto;
    this.users = users;
    this.isSwiping = isSwiping;
    this.onSwipeButton = onSwipeButton;
  }

  public ViewProfileFragment(UserDto userDto, boolean isSwiping) {
    this.userDto = userDto;
    this.isSwiping = isSwiping;
  }

  public ViewProfileFragment(UserDto userDto, List<UserDto> users, boolean isSwiping) {
    this.userDto = userDto;
    this.isSwiping = isSwiping;
    this.users = users;
  }

  public ViewProfileFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentViewProfileBinding = FragmentViewProfileBinding.inflate(getLayoutInflater());

    profileImagesAdapter = new ProfileImagesAdapter(getProfileImages());
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    setUpProfileHeader();
    setUpProfileImages();
    setUpProfileInfo();
    setUpButton();
    setUpSwipeUI(isSwiping);
    return fragmentViewProfileBinding.getRoot();
  }

  private List<String> getProfileImages() {
    List<String> imageUrls = new ArrayList<>();
    for (Map.Entry<String, String> entry : userDto.getImageUrlsMap().entrySet()) {
      imageUrls.add(entry.getValue());
    }
    return imageUrls;
  }

  private List<BasicEnum> getBasics() {
    List<BasicEnum> basics = new ArrayList<>();
    for (Map.Entry<BasicEnum, String> entry : userDto.getProfileSetting().getBasics().entrySet()) {
      basics.add(entry.getKey());
    }
    return basics;
  }

  private List<LifestyleEnum> getLifestyles() {
    List<LifestyleEnum> lifeStyles = new ArrayList<>();
    for (Map.Entry<LifestyleEnum, String> entry :
        userDto.getProfileSetting().getLifestyleList().entrySet()) {
      lifeStyles.add(entry.getKey());
    }
    return lifeStyles;
  }

  private List<String> getBasicContents() {
    List<String> contents = new ArrayList<>();
    for (Map.Entry<BasicEnum, String> entry : userDto.getProfileSetting().getBasics().entrySet()) {
      contents.add(entry.getValue());
    }
    return contents;
  }

  private List<String> getLifeStyleContents() {
    List<String> contents = new ArrayList<>();
    for (Map.Entry<LifestyleEnum, String> entry :
        userDto.getProfileSetting().getLifestyleList().entrySet()) {
      contents.add(entry.getValue());
    }
    return contents;
  }

  private void setUpSwipeUI(boolean isSwiping) {
    if (isSwiping) {
      fragmentViewProfileBinding.likeDislikeBtn.setVisibility(View.VISIBLE);
      fragmentViewProfileBinding.viewProfileBtn.setVisibility(View.VISIBLE);
    } else {
      fragmentViewProfileBinding.likeDislikeBtn.setVisibility(View.GONE);
      fragmentViewProfileBinding.viewProfileBtn.setVisibility(View.GONE);
    }
    fragmentViewProfileBinding.likeBtn.setOnClickListener(
        view -> {
          onSwipeButton.swipeRight(userDto.getId());
          backToSwipe();
        });
    fragmentViewProfileBinding.dislikeBtn.setOnClickListener(
        view -> {
          onSwipeButton.swipeLeft();
          backToSwipe();
        });
  }

  private FlexboxLayoutManager constructLayoutManager() {
    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setFlexWrap(FlexWrap.WRAP);
    layoutManager.setAlignItems(AlignItems.STRETCH);
    return layoutManager;
  }

  private void setUpProfileImages() {
    AtomicInteger position = new AtomicInteger();

    fragmentViewProfileBinding.imageSlider.setAdapter(profileImagesAdapter);
    fragmentViewProfileBinding.imageIndicators.setViewPager(fragmentViewProfileBinding.imageSlider);
    fragmentViewProfileBinding.leftBtn.setOnClickListener(
        view -> {
          if (position.get() == 0) {
            position.set(0);
          } else {
            fragmentViewProfileBinding.imageSlider.setCurrentItem(position.get() - 1);
            position.getAndDecrement();
          }
        });
    fragmentViewProfileBinding.rightBtn.setOnClickListener(
        view -> {
          if (position.get() == userDto.getImageUrlsMap().size() - 1) {
            position.set(userDto.getImageUrlsMap().size() - 1);
          } else {
            fragmentViewProfileBinding.imageSlider.setCurrentItem(position.get() + 1);
            position.getAndIncrement();
          }
        });
  }

  private void setUpProfileHeader() {
    fragmentViewProfileBinding.profileAge.setText(String.valueOf(userDto.getAge()));
    fragmentViewProfileBinding.profileName.setText(userDto.getName());
    fragmentViewProfileBinding.viewProfileBtn.setOnClickListener(
        view -> {
          backToSwipe();
        });
  }

  private void setUpProfileInfo() {
    setUpLookingFor();
    setUpAboutMe();
    setUpEssential();
    setUpBasics();
    setUpInterests();
    setUpLifestyle();
  }

  private void setUpLookingFor() {
    if (userDto.getProfileSetting().getLookingForEnum() != null) {
      fragmentViewProfileBinding.lookingForDisplay.setVisibility(View.VISIBLE);
      fragmentViewProfileBinding.lookingForContent.setText(
          EnumConverter.toString(userDto.getProfileSetting().getLookingForEnum()));
      fragmentViewProfileBinding.lookingForIcon.setImageResource(
          EnumConverter.getIconResource(userDto.getProfileSetting().getLookingForEnum()));
    } else {
      fragmentViewProfileBinding.lookingForDisplay.setVisibility(View.GONE);
    }
  }

  private void setUpAboutMe() {
    if (userDto.getProfileSetting().getQuotes() != null) {
      fragmentViewProfileBinding.aboutMeDisplay.setVisibility(View.VISIBLE);
      fragmentViewProfileBinding.aboutMeContent.setText(userDto.getProfileSetting().getQuotes());
    } else {
      fragmentViewProfileBinding.aboutMeDisplay.setVisibility(View.GONE);
    }
  }

  private void setUpEssential() {
    fragmentViewProfileBinding.essentialDisplay.setVisibility(View.VISIBLE);
  }

  private void setUpInterests() {
    if (userDto.getProfileSetting().getInterests() != null) {
      ProfileChipsAdapter profileChipsAdapter = new ProfileChipsAdapter();
      fragmentViewProfileBinding.interestDisplay.setVisibility(View.VISIBLE);
      profileChipsAdapter.setData(userDto.getProfileSetting().getInterests(), null, null);
      fragmentViewProfileBinding.interestChips.setAdapter(profileChipsAdapter);
      fragmentViewProfileBinding.interestChips.setLayoutManager(constructLayoutManager());
    } else {
      fragmentViewProfileBinding.interestDisplay.setVisibility(View.GONE);
    }
  }

  private void setUpBasics() {
    if (userDto.getProfileSetting().getBasics() != null) {
      ProfileItemAdapter profileItemAdapter =
          new ProfileItemAdapter(getBasicContents(), null, getBasics());
      fragmentViewProfileBinding.basicsDisplay.setVisibility(View.VISIBLE);
      fragmentViewProfileBinding.basicInfo.setLayoutManager(
          new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

      fragmentViewProfileBinding.basicInfo.setAdapter(profileItemAdapter);
    } else {
      fragmentViewProfileBinding.basicsDisplay.setVisibility(View.GONE);
    }
  }

  private void setUpLifestyle() {
    if (userDto.getProfileSetting().getLifestyleList() != null) {
      ProfileItemAdapter profileItemAdapter =
          new ProfileItemAdapter(getLifeStyleContents(), getLifestyles(), null);
      fragmentViewProfileBinding.lifeStyleDisplay.setVisibility(View.VISIBLE);
      fragmentViewProfileBinding.lifeStyleInfo.setAdapter(profileItemAdapter);
      fragmentViewProfileBinding.lifeStyleInfo.setLayoutManager(
          new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    } else {
      fragmentViewProfileBinding.lifeStyleDisplay.setVisibility(View.GONE);
    }
  }

  private void setUpButton() {
    fragmentViewProfileBinding.nameReport.setText(userDto.getName());
    fragmentViewProfileBinding.nameBlock.setText(userDto.getName());
  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.mainView, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  private void backToSwipe() {
    SwipeFragment swipeFragment = new SwipeFragment(users);
    loadFragment(swipeFragment);
  }

  public interface OnBackSwipePress {
    void backToSwipe();
  }

  public interface OnSwipeButton {
    void swipeLeft();

    void swipeRight(String userId);
  }
}
