package com.DatingApp.tinder101.Service;

import android.content.Context;
import android.util.Log;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Model.Notification;
import com.DatingApp.tinder101.Utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class NotificationService {
  private UserService userService;

  private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
  private final CollectionReference notificationRef =
      fireStore.collection(Constant.KEY_COLLECTION_NOTIFICATIONS);

  public NotificationService(UserService userService) {
    this.userService = userService;
  }

  public void sendNotification(String userId, String content) {
    notificationRef
        .add(
            Notification.builder()
                .receiverId(userId)
                .notiContent(content)
                .createdDate(new Date())
                .build())
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                Log.d("Notification Service:", "Send success!!!");
              }
            });
  }
}
