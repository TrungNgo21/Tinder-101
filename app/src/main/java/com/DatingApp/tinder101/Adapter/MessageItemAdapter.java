package com.DatingApp.tinder101.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Model.Conversation;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageItemAdapter
    extends RecyclerView.Adapter<MessageItemAdapter.MessageItemViewHolder> {
  private List<Conversation> conversations;
  private Context context;

  private UserService userService;
  private OnChatItemTap onChatItemTap;

  public MessageItemAdapter(
      List<Conversation> conversations, Context context, OnChatItemTap onChatItemTap) {
    this.conversations = conversations;
    this.onChatItemTap = onChatItemTap;
    this.context = context;
    userService = new UserService(context);
  }

  @NonNull
  @Override
  public MessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);

    return new MessageItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageItemViewHolder holder, int position) {
    Conversation conversation = conversations.get(position);
    Picasso.get().load(conversation.getConversionUrl()).into(holder.profileImage);
    holder.profileName.setText(conversation.getConversionName());
    holder.lastOnline.setText("9 m");

    holder.lastMessage.setText(conversation.getMessageContent());

    holder.container.setOnClickListener(
        view -> {
          onChatItemTap.openChatId(conversation.getConversionId());
        });
  }

  @Override
  public int getItemCount() {
    if (conversations != null) {
      return conversations.size();
    }
    return 0;
  }

  public static class MessageItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView profileImage;
    private TextView profileName;
    private TextView lastOnline;
    private TextView lastMessage;

    private View container;

    public MessageItemViewHolder(@NonNull View itemView) {
      super(itemView);
      profileImage = itemView.findViewById(R.id.profileImage);
      profileName = itemView.findViewById(R.id.profileName);
      lastOnline = itemView.findViewById(R.id.lastOnline);
      lastMessage = itemView.findViewById(R.id.lastMessage);
      container = itemView.findViewById(R.id.container);
    }
  }
}
