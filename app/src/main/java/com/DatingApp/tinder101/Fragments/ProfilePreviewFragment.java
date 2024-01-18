package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.InterestsAdapter;
import com.DatingApp.tinder101.Adapter.LifeStyleAdapter;
import com.DatingApp.tinder101.Adapter.ProfileItemAdapter;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ProfilePreviewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfilePreviewFragment extends Fragment {
    private ProfilePreviewBinding profilePreviewBinding;
    private UserDto currentUser;
    private UserService userService;
    private RecyclerView interestRecyclerView;
    private RecyclerView lifestyleRecyclerView;
    private InterestsAdapter interestsAdapter;
    private LifeStyleAdapter lifeStyleAdapter;
    private ProfileItemAdapter profileItemAdapter;

    private List<String> lifeStyles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePreviewBinding = ProfilePreviewBinding.inflate(getLayoutInflater());
        userService = new UserService(requireContext());
        currentUser = userService.getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_preview, container, false);
        lifeStyles = new ArrayList<>();

        // Initialize RecyclerView
        interestRecyclerView = rootView.findViewById(R.id.interestRecyclerView);
        LinearLayoutManager interestLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        interestRecyclerView.setLayoutManager(interestLayoutManager);
//
//        // Create and set the adapter
        interestsAdapter = new InterestsAdapter(currentUser.getProfileSetting().getInterests());
        interestRecyclerView.setAdapter(interestsAdapter);
//
        lifestyleRecyclerView = rootView.findViewById(R.id.lifeStyleRecyclerView);
        LinearLayoutManager lifeStyleLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        lifestyleRecyclerView.setLayoutManager(lifeStyleLayoutManager);

        for (Map.Entry<LifestyleEnum, String> entry : currentUser.getProfileSetting().getLifestyleList().entrySet()) {
            String value = entry.getValue();
            lifeStyles.add(value);
        }

        System.out.println(lifeStyles);

        // Create and set the adapter
        lifeStyleAdapter = new LifeStyleAdapter(lifeStyles);
        lifestyleRecyclerView.setAdapter(lifeStyleAdapter);



        return rootView;
    }
}