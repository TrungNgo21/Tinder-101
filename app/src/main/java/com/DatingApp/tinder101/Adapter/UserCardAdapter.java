package com.DatingApp.tinder101.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Model.User;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Utils.EnumConverter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.common.api.BatchResultToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.ShortBufferException;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserCardHolder> {
  private UserDto userDto;
  private Context context;

  private boolean isLikeDisplay;
  private boolean isDislikeDisplay;

  private OnImageTap onImageTap;

  private final String DEBUG_TAG = "Action down";

  private final List<String> displayFields =
      Arrays.asList("QUOTES", "INTEREST", "BASIS", "LOOKING_FOR");

  public UserCardAdapter(Context context, OnImageTap onImageTap) {
    this.context = context;
    this.onImageTap = onImageTap;
  }

  public void setData(UserDto userDto) {
    this.userDto = userDto;
    notifyDataSetChanged();
  }

  public void setIsLikeDisplay(boolean isDisplay) {
    this.isLikeDisplay = isDisplay;
    notifyDataSetChanged();
  }

  public void setIsDislikeDisplay(boolean isDisplay) {
    this.isDislikeDisplay = isDisplay;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public UserCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
    return new UserCardHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull UserCardHolder holder, int position) {

    holder.infoDisplay.setOnClickListener(
        view -> {
          Log.d(DEBUG_TAG, " dasdasdasdsad");
        });

    //    holder.dislikeMask.setVisibility(isDislikeDisplay ? View.VISIBLE : View.GONE);
    //    holder.likeMask.setVisibility(isLikeDisplay ? View.VISIBLE : View.GONE);

    holder.nextBtn.setOnClickListener(
        view -> {
          onImageTap.tapRight();
        });
    holder.prevBtn.setOnClickListener(
        view -> {
          onImageTap.tapLeft();
        });

    String currentFieldDisplay;
    if (position > userDto.getImageUrlsMap().size() - 1) {
      currentFieldDisplay = displayFields.get(3);
    } else {
      currentFieldDisplay = displayFields.get(position);
    }
    ProfileChipsAdapter chipsAdapter = new ProfileChipsAdapter();
    holder.profileChips.setLayoutManager(constructLayoutManager());

    Picasso.get()
        .load(userDto.getImageUrlsMap().get(String.valueOf(position)))
        .into(holder.profileImage);
    holder.profileAge.setText("20");
    holder.profileName.setText(userDto.getName());

    if (currentFieldDisplay.equals("QUOTES")) {
      if (!userDto.getProfileSetting().getQuotes().isEmpty()) {
        setDisplayVisible(holder, currentFieldDisplay);
        holder.profileSingleContent.setText(userDto.getProfileSetting().getQuotes());
      } else {
        setDisplayVisible(holder, currentFieldDisplay);
        holder.profileSingleIcon.setImageResource(R.drawable.location_icon);
        holder.profileSingleContent.setText("1 km away");
      }
    } else if (currentFieldDisplay.equals("LOOKING_FOR")) {
      if (userDto.getProfileSetting().getLookingForEnum() != null) {
        setDisplayVisible(holder, currentFieldDisplay);
        holder.profileLookingForContent.setText(
            EnumConverter.toString(userDto.getProfileSetting().getLookingForEnum()));
      }
    } else if (currentFieldDisplay.equals("INTEREST")) {
      if (!userDto.getProfileSetting().getInterests().isEmpty()) {
        setDisplayVisible(holder, currentFieldDisplay);
        holder.profileChipsIcon.setImageResource(R.drawable.interest_icon);

        // set up interest chips adapter
        chipsAdapter.setData(userDto.getProfileSetting().getInterests());
        holder.profileChips.setAdapter(chipsAdapter);
      }
    } else {
      setDisplayVisible(holder, currentFieldDisplay);
      holder.profileChipsIcon.setImageResource(R.drawable.label_icon);
      holder.profileChipsType.setText("Basics & Lifestyle");
      List<String> total = new ArrayList<>();

      // set up lifestyle & basics adapters
      if (!userDto.getProfileSetting().getLifestyleList().isEmpty()
          && !userDto.getProfileSetting().getBasics().isEmpty()) {
        total.addAll(userDto.getProfileSetting().getBasics());
        total.addAll(userDto.getProfileSetting().getLifestyleList());
        if (total.size() > 5) {
          chipsAdapter.setData(total.subList(0, 5));
        } else {
          chipsAdapter.setData(total);
        }
        holder.profileChips.setAdapter(chipsAdapter);
        holder.profileChips.setLayoutManager(constructLayoutManager());
      } else if (!userDto.getProfileSetting().getLifestyleList().isEmpty()) {
        total.addAll(userDto.getProfileSetting().getLifestyleList());
        if (total.size() > 5) {
          chipsAdapter.setData(total.subList(0, 5));
        } else {
          chipsAdapter.setData(total);
        }
        holder.profileChips.setAdapter(chipsAdapter);
        holder.profileChips.setLayoutManager(constructLayoutManager());
      } else if (!userDto.getProfileSetting().getBasics().isEmpty()) {
        total.addAll(userDto.getProfileSetting().getBasics());
        if (total.size() > 5) {
          chipsAdapter.setData(total.subList(0, 5));
        } else {
          chipsAdapter.setData(total);
        }
        holder.profileChips.setAdapter(chipsAdapter);
        holder.profileChips.setLayoutManager(constructLayoutManager());
      }
    }
  }

  @Override
  public int getItemCount() {
    return userDto.getImageUrlsMap().size();
  }

  private FlexboxLayoutManager constructLayoutManager() {
    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setFlexWrap(FlexWrap.WRAP);
    layoutManager.setAlignItems(AlignItems.STRETCH);
    return layoutManager;
  }

  private void setDisplayVisible(UserCardHolder userCardHolder, String displayField) {
    if (displayField.equals("QUOTES")) {
      userCardHolder.profileSingleDisplay.setVisibility(View.VISIBLE);
      userCardHolder.profileChipsDisplay.setVisibility(View.GONE);
      userCardHolder.profileLookingForDisplay.setVisibility(View.GONE);
    } else if (displayField.equals("LOOKING_FOR")) {
      userCardHolder.profileSingleDisplay.setVisibility(View.GONE);
      userCardHolder.profileChipsDisplay.setVisibility(View.GONE);
      userCardHolder.profileLookingForDisplay.setVisibility(View.VISIBLE);
    } else {
      userCardHolder.profileSingleDisplay.setVisibility(View.GONE);
      userCardHolder.profileChipsDisplay.setVisibility(View.VISIBLE);
      userCardHolder.profileLookingForDisplay.setVisibility(View.GONE);
    }
  }

  public static class UserCardHolder extends RecyclerView.ViewHolder {
    private ImageView profileImage;
    private TextView profileName;
    private TextView profileAge;

    private View profileSingleDisplay;
    private View profileChipsDisplay;
    private View profileLookingForDisplay;
    private RecyclerView profileChips;

    private ImageView profileSingleIcon;
    private TextView profileSingleContent;
    private ImageView profileChipsIcon;
    private TextView profileLookingForContent;

    private TextView profileChipsType;

    private View infoDisplay;

    private View likeMask;

    private View dislikeMask;
    private Button prevBtn;
    private Button nextBtn;

    private UserCardHolder(@NonNull View itemView) {
      super(itemView);

      profileImage = itemView.findViewById(R.id.profileImage);
      profileName = itemView.findViewById(R.id.profileName);
      profileAge = itemView.findViewById(R.id.profileAge);
      profileChipsDisplay = itemView.findViewById(R.id.profileChipsDisplay);
      profileSingleDisplay = itemView.findViewById(R.id.profileSingleInfoDisplay);
      profileLookingForDisplay = itemView.findViewById(R.id.profileLookingForDisplay);
      profileChips = itemView.findViewById(R.id.profileChips);
      profileChipsType = itemView.findViewById(R.id.profileChipsType);
      profileChipsIcon = itemView.findViewById(R.id.profileChipsTypeIcon);
      profileSingleIcon = itemView.findViewById(R.id.profileSingleInfoIcon);
      profileSingleContent = itemView.findViewById(R.id.profileSingleInfoContent);
      profileLookingForContent = itemView.findViewById(R.id.profileLookingFor);
      likeMask = itemView.findViewById(R.id.likeMask);
      dislikeMask = itemView.findViewById(R.id.dislikeMask);
      infoDisplay = itemView.findViewById(R.id.info);
      prevBtn = itemView.findViewById(R.id.prevBtn);
      nextBtn = itemView.findViewById(R.id.nextBtn);
    }
  }

  public interface OnImageTap {
    void tapRight();

    void tapLeft();
  }
}
