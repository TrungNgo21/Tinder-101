package com.DatingApp.tinder101.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.MessageDto;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Model.Message;
import com.DatingApp.tinder101.Utils.DateConverter;
import com.DatingApp.tinder101.databinding.ReceivedMessageBubbleBinding;
import com.DatingApp.tinder101.databinding.SentMessageBubbleBinding;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<MessageDto> messages;
  private final UserDto receivedUser;

  public ConversationAdapter(List<MessageDto> messages, UserDto receivedUser) {
    this.messages = messages;
    this.receivedUser = receivedUser;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == Constant.SENT_TYPE) {
      return new SentMessageViewHolder(
          SentMessageBubbleBinding.inflate(
              LayoutInflater.from(parent.getContext()), parent, false));
    }
    return new ReceivedMessageViewHolder(
        ReceivedMessageBubbleBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == Constant.SENT_TYPE) {
      ((SentMessageViewHolder) holder).setData(messages.get(position));
    } else {
      ((ReceivedMessageViewHolder) holder).setData(messages.get(position), receivedUser);
    }
  }

  @Override
  public int getItemCount() {
    if (messages != null) {
      return messages.size();
    }
    return 0;
  }

  @Override
  public int getItemViewType(int position) {
    if (messages.get(position).getReceivedUserId().equals(receivedUser.getId())) {
      return Constant.SENT_TYPE;
    } else {
      return Constant.RECEIVE_TYPE;
    }
  }

  public static class SentMessageViewHolder extends RecyclerView.ViewHolder {

    private final SentMessageBubbleBinding sentMessageBubbleBinding;

    public SentMessageViewHolder(SentMessageBubbleBinding sentMessageBubbleBinding) {
      super(sentMessageBubbleBinding.getRoot());
      this.sentMessageBubbleBinding = sentMessageBubbleBinding;
    }

    private void setData(MessageDto messageDto) {
      sentMessageBubbleBinding.messageContent.setText(messageDto.getMessageContent());
      sentMessageBubbleBinding.sentDate.setText(DateConverter.toString(messageDto.getCreateDate()));
    }
  }

  public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

    private final ReceivedMessageBubbleBinding receivedMessageBubbleBinding;

    public ReceivedMessageViewHolder(
        @NonNull ReceivedMessageBubbleBinding receivedMessageBubbleBinding) {
      super(receivedMessageBubbleBinding.getRoot());
      this.receivedMessageBubbleBinding = receivedMessageBubbleBinding;
    }

    private void setData(MessageDto messageDto, UserDto receivedUser) {
      receivedMessageBubbleBinding.messageContent.setText(messageDto.getMessageContent());
      receivedMessageBubbleBinding.sentDate.setText(
          DateConverter.toString(messageDto.getCreateDate()));
      Picasso.get()
          .load(receivedUser.getImageUrlsMap().get("0"))
          .into(receivedMessageBubbleBinding.profileImage);
    }
  }
}
