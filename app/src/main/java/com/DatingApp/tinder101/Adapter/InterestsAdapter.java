package com.DatingApp.tinder101.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.R;

import java.util.ArrayList;
import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {
    private List<String> interests;;
    public InterestsAdapter(List<String> interests) {
        this.interests = interests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String interest = interests.get(position);
        holder.interestName.setText(interest);
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView interestName;

        public ViewHolder(View itemView) {
            super(itemView);
            interestName = itemView.findViewById(R.id.itemInterest);
        }
    }
}
