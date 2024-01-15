package com.DatingApp.tinder101.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Utils.EnumConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileChipsAdapter extends RecyclerView.Adapter<ProfileChipsAdapter.ChipViewHolder> {

  private final int MAXIMUM_ELE = 5;
  private List<String> chipContents;

  private List<LifestyleEnum> chipLifeStyles;
  private List<BasicEnum> chipBasics;

  public ProfileChipsAdapter() {}

  public void setData(
      List<String> chipContents,
      @Nullable List<LifestyleEnum> chipLifeStyles,
      @Nullable List<BasicEnum> chipBasics) {
    this.chipContents = chipContents;
    this.chipLifeStyles = chipLifeStyles;
    this.chipBasics = chipBasics;
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
    if (chipLifeStyles != null && chipBasics != null) {

      int numOfMissingBasicEle = MAXIMUM_ELE - chipBasics.size();
      int numOfMissingLifeEle = MAXIMUM_ELE - chipLifeStyles.size();

      List<BasicEnum> filledBasicChips = new ArrayList<>();
      for (int i = 0; i < numOfMissingBasicEle; i++) {
        filledBasicChips.add(BasicEnum.LOVE);
      }
      filledBasicChips.addAll(chipBasics);

      List<LifestyleEnum> filledLifestyleChips = new ArrayList<>(chipLifeStyles);
      for (int i = 0; i < numOfMissingLifeEle; i++) {
        filledLifestyleChips.add(LifestyleEnum.PET);
      }
      if (position <= chipLifeStyles.size() - 1) {
        holder.chipIcon.setImageResource(
            EnumConverter.getIconResource(filledLifestyleChips.get(position)));
        holder.chipIcon.setVisibility(View.VISIBLE);
      } else {
        holder.chipIcon.setImageResource(
            EnumConverter.getIconResource(filledBasicChips.get(position)));
        holder.chipIcon.setVisibility(View.VISIBLE);
      }
    } else if (chipLifeStyles != null) {
      holder.chipIcon.setImageResource(EnumConverter.getIconResource(chipLifeStyles.get(position)));
      holder.chipIcon.setVisibility(View.VISIBLE);
    } else if (chipBasics != null) {
      holder.chipIcon.setImageResource(EnumConverter.getIconResource(chipBasics.get(position)));
      holder.chipIcon.setVisibility(View.VISIBLE);
    }
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
    private ImageView chipIcon;

    public ChipViewHolder(@NonNull View itemView) {
      super(itemView);
      chipContent = itemView.findViewById(R.id.chipContent);
      chipIcon = itemView.findViewById(R.id.chipIcon);
    }
  }
}
