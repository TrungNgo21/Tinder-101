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

public class MessageItemAdapter
    extends RecyclerView.Adapter<MessageItemAdapter.MessageItemViewHolder> {
  private List<UserDto> users;
  private OnChatItemTap onChatItemTap;

  public MessageItemAdapter(List<UserDto> users, OnChatItemTap onChatItemTap) {
    this.users = users;
    this.onChatItemTap = onChatItemTap;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public MessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);

    return new MessageItemViewHolder(view, onChatItemTap);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageItemViewHolder holder, int position) {
    UserDto userDto = users.get(position);
    Picasso.get().load(userDto.getImageUrlsMap().get("0")).into(holder.profileImage);
    holder.profileName.setText(userDto.getName());
    holder.lastOnline.setText("9 m");
    holder.lastMessage.setText("You: love you babe!!!");
  }

  @Override
  public int getItemCount() {
    if (users != null) {
      return users.size();
    }
    return 0;
  }

  public static class MessageItemViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {
    private ImageView profileImage;
    private TextView profileName;
    private TextView lastOnline;
    private TextView lastMessage;
    private OnChatItemTap onChatItemTap;

    public MessageItemViewHolder(@NonNull View itemView, OnChatItemTap onChatItemTap) {
      super(itemView);
      profileImage = itemView.findViewById(R.id.profileImage);
      profileName = itemView.findViewById(R.id.profileName);
      lastOnline = itemView.findViewById(R.id.lastOnline);
      lastMessage = itemView.findViewById(R.id.lastMessage);
      this.onChatItemTap = onChatItemTap;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      onChatItemTap.openChat(getAdapterPosition());
    }
  }
}
