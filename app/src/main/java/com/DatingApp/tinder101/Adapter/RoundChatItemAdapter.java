package com.DatingApp.tinder101.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoundChatItemAdapter
    extends RecyclerView.Adapter<RoundChatItemAdapter.RoundChatItemViewHolder> {

  private List<UserDto> users;

  private OnChatItemTap onChatItemTap;

  public RoundChatItemAdapter(List<UserDto> users, OnChatItemTap onChatItemTap) {
    this.users = users;
    this.onChatItemTap = onChatItemTap;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RoundChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_round_item, parent, false);

    return new RoundChatItemViewHolder(view, onChatItemTap);
  }

  @Override
  public void onBindViewHolder(@NonNull RoundChatItemViewHolder holder, int position) {
    UserDto userDto = users.get(position);
    Picasso.get().load(userDto.getImageUrlsMap().get("0")).into(holder.profileImage);
    holder.lastOnline.setText("8 m");
    holder.profileName.setText(userDto.getName());
  }

  @Override
  public int getItemCount() {
    if (users != null) {
      return users.size();
    }
    return 0;
  }

  public static class RoundChatItemViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {
    private ImageView profileImage;
    private TextView profileName;
    private TextView lastOnline;

    private OnChatItemTap onChatItemTap;

    public RoundChatItemViewHolder(@NonNull View itemView, OnChatItemTap onChatItemTap) {
      super(itemView);
      profileImage = itemView.findViewById(R.id.profileImage);
      profileName = itemView.findViewById(R.id.profileName);
      lastOnline = itemView.findViewById(R.id.lastOnline);
      this.onChatItemTap = onChatItemTap;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      onChatItemTap.openChat(getAdapterPosition());
    }
  }
}
