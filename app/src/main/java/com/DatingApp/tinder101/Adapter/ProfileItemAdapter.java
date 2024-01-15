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

import java.util.HashMap;
import java.util.List;

public class ProfileItemAdapter
    extends RecyclerView.Adapter<ProfileItemAdapter.ProfileItemViewHolder> {
  private List<String> contents;
  private List<LifestyleEnum> lifeStyleItems;
  private List<BasicEnum> basicItems;

  public ProfileItemAdapter(
      List<String> contents,
      @Nullable List<LifestyleEnum> lifeStyleItems,
      @Nullable List<BasicEnum> basicItems) {
    this.contents = contents;
    this.lifeStyleItems = lifeStyleItems;
    this.basicItems = basicItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ProfileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
    return new ProfileItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProfileItemViewHolder holder, int position) {

    if (lifeStyleItems == null) {
      holder.typeContent.setText(contents.get(position));
      holder.typeIcon.setImageResource(EnumConverter.getIconResource(basicItems.get(position)));
      holder.type.setText(EnumConverter.toString(basicItems.get(position)));
    } else {
      holder.typeContent.setText(contents.get(position));
      holder.typeIcon.setImageResource(EnumConverter.getIconResource(lifeStyleItems.get(position)));
      holder.type.setText(EnumConverter.toString(lifeStyleItems.get(position)));
    }
  }

  @Override
  public int getItemCount() {
    if (lifeStyleItems == null) {
      return basicItems.size();
    } else if (basicItems == null) {
      return lifeStyleItems.size();
    }
    return 0;
  }

  public final class ProfileItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView typeIcon;
    private TextView type;
    private TextView typeContent;

    public ProfileItemViewHolder(@NonNull View itemView) {
      super(itemView);
      typeIcon = itemView.findViewById(R.id.typeIcon);
      type = itemView.findViewById(R.id.type);
      typeContent = itemView.findViewById(R.id.typeContent);
    }
  }
}
