package com.DatingApp.tinder101.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.DatingApp.tinder101.R;

public class LikeAndDislikeButtonFragment extends Fragment {
    private ImageView likeButton;
    private ImageView dislikeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_and_dislike_button, container, false);

        likeButton = view.findViewById(R.id.likeButton);
        dislikeButton = view.findViewById(R.id.dislikeButton);

        // Set click listeners for the like and dislike buttons
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle like button click event
                // You can perform any desired action here
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle dislike button click event
                // You can perform any desired action here
            }
        });

        return view;
    }
}