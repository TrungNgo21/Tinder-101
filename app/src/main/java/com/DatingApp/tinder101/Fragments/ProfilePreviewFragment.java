package com.DatingApp.tinder101.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.InterestsAdapter;
import com.DatingApp.tinder101.Adapter.LifeStyleAdapter;
import com.DatingApp.tinder101.Adapter.ProfileItemAdapter;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserProfileService;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ProfilePreviewBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProfilePreviewFragment extends Fragment {
    private ProfilePreviewBinding profilePreviewBinding;
    private UserDto currentUser;
    private UserService userService;
    private UserProfileService userProfileService;
//    private RecyclerView interestRecyclerView;
//    private RecyclerView lifestyleRecyclerView;
    private InterestsAdapter interestsAdapter;
    private LifeStyleAdapter lifeStyleAdapter;
    private ProfileItemAdapter profileItemAdapter;

    private List<String> lifeStyles;

    private Dialog dialog;

    private View rootView;

    private ImageView memberImg,mainImg, img1, img2, img3;

    private TextView name, age;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePreviewBinding = ProfilePreviewBinding.inflate(getLayoutInflater());
        userService = new UserService(requireContext());
        currentUser = userService.getCurrentUser();
        userProfileService = new UserProfileService(userService);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile_preview, container, false);
        lifeStyles = new ArrayList<>();

        dialog = new Dialog(requireContext());


        LinearLayoutManager interestLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

//
//        // Create and set the adapter

        memberImg = rootView.findViewById(R.id.member_image);
        mainImg = rootView.findViewById(R.id.main_img);
        img1 = rootView.findViewById(R.id.img_1);
        img2 = rootView.findViewById(R.id.img_2);
        img3 = rootView.findViewById(R.id.img_3);

        List<String> imgList = new ArrayList<>(userProfileService.getImgURL().values());

        Picasso.get().load(imgList.get(0))
                .into(memberImg);
        Picasso.get().load(imgList.get(0))
                .into(mainImg);
        Picasso.get().load(imgList.get(1))
                .into(img1);
        Picasso.get().load(imgList.get(2))
                .into(img2);
        Picasso.get().load(imgList.get(3))
                .into(img3);


        TextView addBasics = rootView.findViewById(R.id.add_basic);

        TextView addInterest = rootView.findViewById(R.id.add_interest);

        TextView addLifeStyle = rootView.findViewById(R.id.add_lifeStyle);

        name = rootView.findViewById(R.id.my_name);
        name.setText(userProfileService.getCurrentUser().getName());

        age = rootView.findViewById(R.id.my_age);
//        age.setText(userProfileService.getCurrentUser().getAge());

        addInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(true, false);
            }
        });
        addBasics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(false, true);
            }
        });

        addLifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(false, false);
            }
        });
        setupProfileInterestChip();
        return rootView;
    }

    public void showBottomSheetDialog(boolean isInterest, boolean isBasics) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (isInterest) {
            dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.bottom_sheet_interest);
        } else if (isBasics) {
            dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.bottom_sheet_layout);
        } else {
            dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.bottom_sheet_layout);
        }

        TextView cancel = dialog.findViewById(R.id.cancel_button);
        TextView done = dialog.findViewById(R.id.done_button);

        ChipGroup group1 = dialog.findViewById(R.id.chip_group_1);
        ChipGroup group2 = dialog.findViewById(R.id.chip_group_2);
        ChipGroup group3 = dialog.findViewById(R.id.chip_group_3);
        ChipGroup group4 = dialog.findViewById(R.id.chip_group_4);

        TextView title1 = dialog.findViewById(R.id.chip_group_title_1);
        TextView title2 = dialog.findViewById(R.id.chip_group_title_2);
        TextView title3 = dialog.findViewById(R.id.chip_group_title_3);
        TextView title4 = dialog.findViewById(R.id.chip_group_title_4);
        TextView bottomSheetTitle = dialog.findViewById(R.id.bottom_sheet_title);




        if (isInterest) {
//            dialog.setContentView(R.layout.bottom_sheet_interest);
            setUpInterestChipGroup();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (isBasics) {
//            dialog.setContentView(R.layout.bottom_sheet_layout);

            setUpChipGroup(group1, Constant.zodiacs, "ZODIAC", isBasics);
            setUpChipGroup(group2, Constant.educations, "EDUCATION", isBasics);
            setUpChipGroup(group3, Constant.communications, "COMMUNICATION", isBasics);
            setUpChipGroup(group4, Constant.loves, "LOVE", isBasics);

            title1.setText("What is your zodiac sign ?");
            title2.setText("What is your education level ?");
            title3.setText("What is your communication style ?");
            title4.setText("How do you receive love ?");

            bottomSheetTitle.setText("More about me");

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {

            setUpChipGroup(group1, Constant.pets, "PET", false);
            setUpChipGroup(group2, Constant.drinks, "DRINKING", false);
            setUpChipGroup(group3, Constant.smokes, "SMOKE", false);
            setUpChipGroup(group4, Constant.workouts, "WORKOUT", false);

            title1.setText("Do you have any pets ?");
            title2.setText("How often do you drink ?");
            title3.setText("How often do you smoke ?");
            title4.setText("Do you exercise ?");

            bottomSheetTitle.setText("Lifestyle");
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileService.updateUserProfileSetting();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_bg);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void closeDialog() {
        dialog.cancel();
        dialog = new Dialog(requireContext());
    }


    public void setUpChipGroup(ChipGroup chipGroup, List<String> list, String type, boolean isBasic){
        List<String> basicsList = new ArrayList<>(userProfileService.getBasics().values());
        List<String> lifeStyleList = new ArrayList<>(userProfileService.getLifeStyleList().values());
        List<String> valueList;
        if (isBasic) {
            valueList = new ArrayList<>(userProfileService.getBasics().values());
        } else {
            valueList = new ArrayList<>(userProfileService.getLifeStyleList().values());
        }
        for(String value : list){

            Chip chipView = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.custom_chip_v2, null);
            chipView.setText(value);

            boolean isChecked = valueList.contains(value);
            chipView.setId(new Random().nextInt());

            float padding = getResources().getDimension(com.intuit.sdp.R.dimen._8sdp);
            chipView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chipView.setTypeface(null, Typeface.BOLD);

//            chipView.setChecked(isChecked);




            chipGroup.addView(chipView);
            chipGroup.setSingleSelection(true);
            if (isChecked) {
                chipGroup.check(chipView.getId());
            }
            chipView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isBasic) {
                        if (isChecked) {
                            userProfileService.appendBasics(type, chipView.getText().toString());
                        }
                    } else {
                        if (isChecked) {
                            userProfileService.appendLifeStyleList(type, chipView.getText().toString());
                        }
                    }
                }
            });
        }
    }

    public void setUpInterestChipGroup() {
        List<String> interestList = userProfileService.getInterests();
        ChipGroup chipGroup = dialog.findViewById(R.id.interestChipGroup);

        for(String interest : Constant.interests){
            Chip chipView = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.custom_chip_v2, null);
            chipView.setText(interest);
            chipView.setId(new Random().nextInt());
            float padding = getResources().getDimension(com.intuit.sdp.R.dimen._8sdp);
            chipView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chipView.setTypeface(null, Typeface.BOLD);
            chipGroup.addView(chipView);
            if (interestList.contains(interest)) {
                chipGroup.check(chipView.getId());
            }

            chipView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!interestList.contains(interest)) {
                            if (interestList.size() != 5) {
                                chipView.setChecked(true);
                                userProfileService.appendInterests(interest);
                            } else {
                                chipView.setChecked(false);
                            }
                        }
                    } else {
                        userProfileService.removeInterests(interest);
                    }
                }
            });
//
        }
    }


    public void setupProfileInterestChip() {
        List<String> basicValues = new ArrayList<>(userProfileService.getBasics().values());
        List<String> lifeStyleValues = new ArrayList<>(userProfileService.getLifeStyleList().values());
        List<String> interests = userProfileService.getInterests();
        ChipGroup interestGroup = rootView.findViewById(R.id.interestChipGroup);
        ChipGroup basicGroup = rootView.findViewById(R.id.basicsChipGroup);
        ChipGroup lifeStyleGroup = rootView.findViewById(R.id.lifeStylesChipGroup);

        int[] backgroundColors = {R.color.light_purple_400, R.color.slate_blue_600, R.color.like_middle_color};

        setupProfileChipGroup(interests, interestGroup, backgroundColors[0]);
        setupProfileChipGroup(basicValues, basicGroup, backgroundColors[1]);
        setupProfileChipGroup(lifeStyleValues, lifeStyleGroup, backgroundColors[2]);
    }

    @SuppressLint("ResourceAsColor")
    public void setupProfileChipGroup(List<String> values, ChipGroup chipGroup, int backgroundColor) {
        int colorIndex = 0;

        for (String value : values) {
            Chip chipView = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.custom_chip_v2, null);
            chipView.setText(value);
            chipView.setId(new Random().nextInt());
            float padding = getResources().getDimension(com.intuit.sdp.R.dimen._8sdp);
            chipView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chipView.setTypeface(null, Typeface.BOLD);
            chipView.setChipBackgroundColorResource(backgroundColor);
            chipGroup.addView(chipView);
        }
    }
}