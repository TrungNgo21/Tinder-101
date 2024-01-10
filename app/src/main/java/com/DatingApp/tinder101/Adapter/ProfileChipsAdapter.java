package com.DatingApp.tinder101.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.R;
import java.util.List;

public class ProfileChipsAdapter extends RecyclerView.Adapter<ProfileChipsAdapter.ChipViewHolder> {
  private List<String> chipContents;

  public ProfileChipsAdapter() {}

  public void setData(List<String> chipContents) {
    this.chipContents = chipContents;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_chip_item, parent, false);
    return new ChipViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
    holder.chipContent.setText(chipContents.get(position));
  }

  @Override
  public int getItemCount() {
    if (chipContents != null) {
      return chipContents.size();
    }
    return 0;
  }

  public static class ChipViewHolder extends RecyclerView.ViewHolder {
    private TextView chipContent;

    public ChipViewHolder(@NonNull View itemView) {
      super(itemView);
      chipContent = itemView.findViewById(R.id.chipContent);
    }
  }
}
