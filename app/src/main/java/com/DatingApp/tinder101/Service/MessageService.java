package com.DatingApp.tinder101.Service;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.Adapter.ConversationAdapter;
import com.DatingApp.tinder101.Adapter.MessageItemAdapter;
import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.MessageDto;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Model.Conversation;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageService {
  private PreferenceManager preferenceManager;

  private String conversationId;

  private UserService userService;
  private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
  private final FirebaseDatabase firebaseDatabase =
      FirebaseDatabase.getInstance(Constant.KEY_DATABASE_URL);
  private final DatabaseReference realTimeUserRef =
      firebaseDatabase.getReference().child(Constant.KEY_COLLECTION_MESSAGES);

  private final CollectionReference messageRef =
      fireStore.collection(Constant.KEY_COLLECTION_MESSAGES);

  private final CollectionReference conversationRef =
      fireStore.collection(Constant.KEY_COLLECTION_CONVERSATIONS);

  public MessageService(Context context) {
    this.preferenceManager = new PreferenceManager(context.getApplicationContext());
  }

  public MessageService(Context context, UserService userService) {
    this.userService = userService;
    this.preferenceManager = new PreferenceManager(context.getApplicationContext());
  }

  public void setConversationListener(
      List<Conversation> conversations,
      MessageItemAdapter messageItemAdapter,
      RecyclerView conversationDisplay) {
    EventListener<QuerySnapshot> conversationListener =
        (value, error) -> {
          if (error != null) {
            return;
          } else {
            if (value != null) {
              for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                  Conversation conversation =
                      Conversation.builder()
                          .createDate(
                              documentChange.getDocument().getDate(Constant.KEY_CREATED_DATE_ID))
                          .sentUserId(
                              documentChange.getDocument().getString(Constant.KEY_SENT_USER_ID))
                          .receivedUserId(
                              documentChange.getDocument().getString(Constant.KEY_RECEIVED_USED_ID))
                          .messageContent(
                              documentChange
                                  .getDocument()
                                  .getString(Constant.KEY_MESSAGE_CONTENT_ID))
                          .build();
                  if (userService.getCurrentUser().getId().equals(conversation.getSentUserId())) {
                    conversation.setConversionId(
                        documentChange.getDocument().getString(Constant.KEY_RECEIVED_USED_ID));
                    conversation.setConversionName(
                        documentChange.getDocument().getString(Constant.KEY_RECEIVER_NAME));
                    conversation.setConversionUrl(
                        documentChange.getDocument().getString(Constant.KEY_RECEIVER_URL));
                  } else {
                    conversation.setConversionId(
                        documentChange.getDocument().getString(Constant.KEY_SENT_USER_ID));
                    conversation.setConversionName(
                        documentChange.getDocument().getString(Constant.KEY_SENDER_NAME));
                    conversation.setConversionUrl(
                        documentChange.getDocument().getString(Constant.KEY_SENDER_URL));
                  }
                  conversations.add(conversation);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                  for (int i = 0; i < conversations.size(); i++) {
                    String senderId =
                        documentChange.getDocument().getString(Constant.KEY_SENT_USER_ID);
                    String receiverId =
                        documentChange.getDocument().getString(Constant.KEY_RECEIVED_USED_ID);
                    if (conversations.get(i).getSentUserId().equals(senderId)
                        && conversations.get(i).getReceivedUserId().equals(receiverId)) {
                      conversations
                          .get(i)
                          .setMessageContent(
                              documentChange
                                  .getDocument()
                                  .getString(Constant.KEY_MESSAGE_CONTENT_ID));
                      conversations
                          .get(i)
                          .setCreateDate(
                              documentChange.getDocument().getDate(Constant.KEY_CREATED_DATE_ID));
                      break;
                    }
                  }
                }
              }

              conversations.sort(
                  (obj1, obj2) -> obj2.getCreateDate().compareTo(obj1.getCreateDate()));
              messageItemAdapter.notifyDataSetChanged();
              conversationDisplay.smoothScrollToPosition(0);
            }
          }
        };
    conversationRef
        .whereEqualTo(Constant.KEY_SENT_USER_ID, userService.getCurrentUser().getId())
        .addSnapshotListener(conversationListener);
    conversationRef
        .whereEqualTo(Constant.KEY_RECEIVED_USED_ID, userService.getCurrentUser().getId())
        .addSnapshotListener(conversationListener);
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
              }
            }
            messages.sort(Comparator.comparing(MessageDto::getCreateDate));
            if (numMessage == 0) {
              conversationAdapter.notifyDataSetChanged();
            } else {
              conversationAdapter.notifyItemRangeInserted(messages.size(), messages.size());
              messageDisplay.smoothScrollToPosition(messages.size() - 1);
            }
          }
          if (conversationId == null) {
            checkForConversation(messages, userService.getCurrentUser().getId(), receivedId);
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
      Message message,
      UserDto receiver,
      UserDto sender,
      final FirebaseCallback<CallbackRes<MessageDto>> callback) {
    messageRef
        .add(message)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                MessageDto messageDto = message.toDto();
                messageDto.setId(task.getResult().getId());
                if (conversationId != null) {
                  updateConversation(message.getMessageContent());
                } else {
                  HashMap<String, Object> conversation = new HashMap<>();
                  conversation.put(Constant.KEY_SENT_USER_ID, message.getSentUserId());
                  conversation.put(Constant.KEY_SENDER_NAME, sender.getName());
                  conversation.put(Constant.KEY_SENDER_URL, sender.getImageUrlsMap().get("0"));
                  conversation.put(Constant.KEY_RECEIVED_USED_ID, message.getReceivedUserId());
                  conversation.put(Constant.KEY_RECEIVER_NAME, receiver.getName());
                  conversation.put(Constant.KEY_RECEIVER_URL, receiver.getImageUrlsMap().get("0"));
                  conversation.put(Constant.KEY_MESSAGE_CONTENT_ID, message.getMessageContent());
                  conversation.put(Constant.KEY_CREATED_DATE_ID, new Date());
                  addConversation(
                      conversation,
                      new FirebaseCallback<CallbackRes<Conversation>>() {
                        @Override
                        public void callback(CallbackRes<Conversation> template) {
                          if (template instanceof CallbackRes.Success) {
                            callback.callback(new CallbackRes.Success<MessageDto>(messageDto));
                          } else {
                            callback.callback(
                                new CallbackRes.Error(new Exception(template.toString())));
                          }
                        }
                      });
                }
                callback.callback(new CallbackRes.Success<MessageDto>(messageDto));
              } else {
                callback.callback(new CallbackRes.Error(task.getException()));
              }
            });
  }

  private void checkForConversation(List<MessageDto> messages, String senderId, String receiverId) {
    if (!messages.isEmpty()) {
      checkForConversationRemote(senderId, receiverId);
      checkForConversationRemote(receiverId, senderId);
    }
  }

  private void checkForConversationRemote(String senderId, String receiverId) {
    conversationRef
        .whereEqualTo(Constant.KEY_SENT_USER_ID, senderId)
        .whereEqualTo(Constant.KEY_RECEIVED_USED_ID, receiverId)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()
                  && task.getResult() != null
                  && task.getResult().getDocuments().size() > 0) {
                conversationId = task.getResult().getDocuments().get(0).getId();
                Log.d("FInish", "Finish oi");
              }
            });
  }

  private void addConversation(
      HashMap<String, Object> conversation,
      final FirebaseCallback<CallbackRes<Conversation>> callback) {
    conversationRef
        .add(conversation)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                conversationId = task.getResult().getId();
                callback.callback(new CallbackRes.Success<HashMap<String, Object>>(conversation));
              } else {
                callback.callback(new CallbackRes.Error(task.getException()));
              }
            });
  }

  private void updateConversation(String message) {
    conversationRef
        .document(conversationId)
        .update(Constant.KEY_MESSAGE_CONTENT_ID, message, Constant.KEY_CREATED_DATE_ID, new Date());
  }
}
