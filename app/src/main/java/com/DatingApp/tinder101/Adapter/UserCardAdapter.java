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
import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.ShortBufferException;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserCardHolder> {
    private UserDto userDto;
    private Context context;

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

    private List<LifestyleEnum> extractLifestyles(HashMap<LifestyleEnum, String> LifeStyleList) {
        List<LifestyleEnum> listTypes = new ArrayList<>();
        for (Map.Entry<LifestyleEnum, String> entry : LifeStyleList.entrySet()) {
            listTypes.add(entry.getKey());
        }
        return listTypes;
    }

    private List<BasicEnum> extractBasics(HashMap<BasicEnum, String> LifeStyleList) {
        List<BasicEnum> listTypes = new ArrayList<>();
        for (Map.Entry<BasicEnum, String> entry : LifeStyleList.entrySet()) {
            listTypes.add(entry.getKey());
        }
        return listTypes;
    }

    private List<String> extractBasicContents(HashMap<BasicEnum, String> basics) {
        List<String> listContents = new ArrayList<>();
        for (Map.Entry<BasicEnum, String> entry : basics.entrySet()) {
            listContents.add(entry.getValue());
        }
        return listContents;
    }

    private List<String> extractLifestyleContents(HashMap<LifestyleEnum, String> lifeStyles) {
        List<String> listContents = new ArrayList<>();
        for (Map.Entry<LifestyleEnum, String> entry : lifeStyles.entrySet()) {
            listContents.add(entry.getValue());
        }
        return listContents;
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
                    onImageTap.tapDown(userDto);
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
                chipsAdapter.setData(userDto.getProfileSetting().getInterests(), null, null);
                holder.profileChips.setAdapter(chipsAdapter);
            }
        } else {
            setDisplayVisible(holder, currentFieldDisplay);
            holder.profileChipsIcon.setImageResource(R.drawable.label_icon);
            holder.profileChipsType.setText("Basics & Lifestyle");

            HashMap<LifestyleEnum, String> lifeStyleChips =
                    userDto.getProfileSetting().getLifestyleList();
            HashMap<BasicEnum, String> basicChips = userDto.getProfileSetting().getBasics();
            List<String> lifeStyleContents = extractLifestyleContents(lifeStyleChips);
            List<String> basicContents = extractBasicContents(basicChips);
            List<String> contents = new ArrayList<>();
            // set up lifestyle & basics adapters
            if (!userDto.getProfileSetting().getLifestyleList().isEmpty()
                    && !userDto.getProfileSetting().getBasics().isEmpty()) {

                if (lifeStyleChips.size() + basicChips.size() > 5) {
                    contents.addAll(lifeStyleContents.subList(0, lifeStyleContents.size()));
                    contents.addAll(basicContents.subList(0, 5 - lifeStyleContents.size()));
                    chipsAdapter.setData(
                            contents,
                            extractLifestyles(lifeStyleChips).subList(0, lifeStyleContents.size()),
                            extractBasics(basicChips).subList(0, 5 - lifeStyleContents.size()));
                } else {
                    contents.addAll(lifeStyleContents);
                    contents.addAll(basicContents);
                    chipsAdapter.setData(
                            contents, extractLifestyles(lifeStyleChips), extractBasics(basicChips));
                }
                holder.profileChips.setAdapter(chipsAdapter);
                holder.profileChips.setLayoutManager(constructLayoutManager());
            } else if (!userDto.getProfileSetting().getLifestyleList().isEmpty()) {
                chipsAdapter.setData(lifeStyleContents, extractLifestyles(lifeStyleChips), null);
                holder.profileChips.setAdapter(chipsAdapter);
                holder.profileChips.setLayoutManager(constructLayoutManager());
            } else if (!userDto.getProfileSetting().getBasics().isEmpty()) {
                chipsAdapter.setData(basicContents, null, extractBasics(basicChips));
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

    public class UserCardHolder extends RecyclerView.ViewHolder {
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
            infoDisplay = itemView.findViewById(R.id.info);
        }
    }

    public interface OnImageTap {
        void tapDown(UserDto userDto);
    }
}