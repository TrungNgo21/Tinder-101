package com.DatingApp.tinder101.Service;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.ConversationAdapter;
import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.MessageDto;
import com.DatingApp.tinder101.Model.Message;
import com.DatingApp.tinder101.Utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageService {
  private PreferenceManager preferenceManager;

  private UserService userService;
  private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
  private final FirebaseDatabase firebaseDatabase =
      FirebaseDatabase.getInstance(Constant.KEY_DATABASE_URL);
  private final DatabaseReference realTimeUserRef =
      firebaseDatabase.getReference().child(Constant.KEY_COLLECTION_MESSAGES);

  private final CollectionReference messageRef =
      fireStore.collection(Constant.KEY_COLLECTION_MESSAGES);

  public MessageService(Context context) {
    this.preferenceManager = new PreferenceManager(context.getApplicationContext());
  }

  public MessageService(Context context, UserService userService) {
    this.userService = userService;
    this.preferenceManager = new PreferenceManager(context.getApplicationContext());
  }

  public void setMessagesListener(
      String receivedId,
      List<MessageDto> messages,
      ConversationAdapter conversationAdapter,
      RecyclerView messageDisplay) {
    EventListener<QuerySnapshot> messageListener =
        (value, error) -> {
          if (error != null) {
            return;
          } else {
            int numMessage = messages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
              if (documentChange.getType() == DocumentChange.Type.ADDED) {
                MessageDto messageDto =
                    MessageDto.builder()
                        .id(documentChange.getDocument().getId())
                        .receivedUserId(
                            documentChange.getDocument().getString(Constant.KEY_RECEIVED_USED_ID))
                        .sentUserId(
                            documentChange.getDocument().getString(Constant.KEY_SENT_USER_ID))
                        .messageContent(
                            documentChange.getDocument().getString(Constant.KEY_MESSAGE_CONTENT_ID))
                        .createDate(
                            documentChange.getDocument().getDate(Constant.KEY_CREATED_DATE_ID))
                        .build();
                messages.add(messageDto);
                messages.sort(Comparator.comparing(MessageDto::getCreateDate));
              }
            }
            if (numMessage == 0) {
              conversationAdapter.notifyDataSetChanged();
            } else {
              conversationAdapter.notifyItemRangeChanged(messages.size(), messages.size());
              messageDisplay.smoothScrollToPosition(messages.size() - 1);
            }
          }
        };
    messageRef
        .whereEqualTo(Constant.KEY_SENT_USER_ID, userService.getCurrentUser().getId())
        .whereEqualTo(Constant.KEY_RECEIVED_USED_ID, receivedId)
        .addSnapshotListener(messageListener);
    messageRef
        .whereEqualTo(Constant.KEY_SENT_USER_ID, receivedId)
        .whereEqualTo(Constant.KEY_RECEIVED_USED_ID, userService.getCurrentUser().getId())
        .addSnapshotListener(messageListener);
  }

  public void sentMessage(
      Message message, final FirebaseCallback<CallbackRes<MessageDto>> callback) {
    messageRef
        .add(message)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                MessageDto messageDto = message.toDto();
                messageDto.setId(task.getResult().getId());
                callback.callback(new CallbackRes.Success<MessageDto>(messageDto));
              } else {
                callback.callback(new CallbackRes.Error(task.getException()));
              }
            });
  }
}
