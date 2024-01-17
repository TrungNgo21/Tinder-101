package com.DatingApp.tinder101.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.R;

import java.util.HashMap;
import java.util.List;

public class LifeStyleAdapter extends RecyclerView.Adapter<LifeStyleAdapter.ViewHolder> {
    private List<String> lifestyles;;

    public LifeStyleAdapter(List<String> lifestyles) {
        this.lifestyles = lifestyles;
    }

    @NonNull
    @Override
    public LifeStyleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lifestyle, parent, false);
        return new LifeStyleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String lifestyle = lifestyles.get(position);
        holder.lifecycleName.setText(lifestyle);
    }

    @Override
    public int getItemCount() {
        return lifestyles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lifecycleName;

        public ViewHolder(View itemView) {
            super(itemView);
            lifecycleName = itemView.findViewById(R.id.itemLifestyle);
        }
    }
}
