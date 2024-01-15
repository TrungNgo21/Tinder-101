package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

  private ProfileImagesAdapter profileImagesAdapter;

  private OnBackSwipePress onBackSwipePress;

  public ViewProfileFragment(UserDto userDto, OnBackSwipePress onBackSwipePress) {
    this.userDto = userDto;
    this.onBackSwipePress = onBackSwipePress;
  }

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
    fragmentViewProfileBinding.profileAge.setText("19");
    fragmentViewProfileBinding.profileName.setText(userDto.getName());
    fragmentViewProfileBinding.viewProfileBtn.setOnClickListener(
        view -> {
          onBackSwipePress.backToSwipe();
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
    //    if (userDto.getProfileSetting().getQuotes() != null) {
    //      fragmentViewProfileBinding.aboutMeDisplay.setVisibility(View.VISIBLE);
    //
    // fragmentViewProfileBinding.aboutMeContent.setText(userDto.getProfileSetting().getQuotes());
    //    } else {
    //      fragmentViewProfileBinding.aboutMeDisplay.setVisibility(View.GONE);
    //    }
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

  public interface OnBackSwipePress {
    void backToSwipe();
  }
}
