package com.DatingApp.tinder101.Service;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Enum.BasicEnum;
import com.DatingApp.tinder101.Enum.LifestyleEnum;
import com.DatingApp.tinder101.Enum.LookingForEnum;
import com.DatingApp.tinder101.Model.Notification;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class SystemService {
  private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
  private final CollectionReference userReference =
      fireStore.collection(Constant.KEY_COLLECTION_USERS);
  private final CollectionReference notificationRef =
      fireStore.collection(Constant.KEY_COLLECTION_USERS);

  private UserService userService;

  private NotificationService notificationService;
  private List<UserDto> userDtoList;
  private UserDto currentUser;

  public SystemService(UserService userService) {
    this.userService = userService;
    this.notificationService = new NotificationService(userService);
    this.currentUser = userService.getCurrentUser();
  }

  private int compareInterests(List<String> currentUserInterests, List<String> otherInterests) {
    int currentScore = 0;
    if (currentUserInterests == null && otherInterests == null) {
      return currentScore;
    }
    if (currentUserInterests.isEmpty() && otherInterests.isEmpty()) {
      return currentScore;
    }

    currentUserInterests = new ArrayList<String>(currentUserInterests);
    otherInterests = new ArrayList<String>(otherInterests);

    for (String interest : currentUserInterests) {
      if (otherInterests.contains(interest)) {
        currentScore += 1;
      }
    }
    return currentScore;
  }

  // Method to compare to basics list and return the score;
  private int compareBasics(
      HashMap<BasicEnum, String> currentUserData, HashMap<BasicEnum, String> otherData) {
    int currentScore = 0;
    if (currentUserData == null && otherData == null) {
      return currentScore;
    }

    if (currentUserData.isEmpty() && otherData.isEmpty()) {
      return currentScore;
    }

    for (Map.Entry<BasicEnum, String> entry : currentUserData.entrySet()) {
      BasicEnum key = entry.getKey();
      String currentUserValue = entry.getValue();
      if (otherData.containsKey(key)) {
        String otherValue = otherData.get(key);
        if (Objects.equals(otherValue, currentUserValue)) {
          currentScore += 1;
        }
      }
    }
    return currentScore;
  }

  private int compareLifeStyle(
      HashMap<LifestyleEnum, String> currentUserData, HashMap<LifestyleEnum, String> otherData) {
    int currentScore = 0;
    if (currentUserData == null && otherData == null) {
      return currentScore;
    }

    if (currentUserData.isEmpty() && otherData.isEmpty()) {
      return currentScore;
    }

    for (Map.Entry<LifestyleEnum, String> entry : currentUserData.entrySet()) {
      LifestyleEnum key = entry.getKey();
      String currentUserValue = entry.getValue();
      if (otherData.containsKey(key)) {
        String otherValue = otherData.get(key);
        if (Objects.equals(otherValue, currentUserValue)) {
          currentScore += 1;
        }
      }
    }
    return currentScore;
  }

  // Method to generate users that have commons with the current users
  public void setUsersListDtoForMatching() {
    this.userService.getAllUsers(
        new FirebaseCallback<CallbackRes<List<UserDto>>>() {
          @Override
          public void callback(CallbackRes<List<UserDto>> template) {
            if (template instanceof CallbackRes.Success) {
              userDtoList = ((CallbackRes.Success<Stack<UserDto>>) template).getData();
              for (UserDto userDto : userDtoList) {
                int scoreInterest =
                    compareInterests(
                        currentUser.getProfileSetting().getInterests(),
                        userDto.getProfileSetting().getInterests());
                int scoreBasic =
                    compareBasics(
                        currentUser.getProfileSetting().getBasics(),
                        userDto.getProfileSetting().getBasics());
                int scoreLifeStyle =
                    compareLifeStyle(
                        currentUser.getProfileSetting().getLifestyleList(),
                        userDto.getProfileSetting().getLifestyleList());
                int finalScore = scoreInterest + scoreBasic + scoreLifeStyle;
                if (currentUser.getProfileSetting().getLookingForEnum()
                    == userDto.getProfileSetting().getLookingForEnum()) {
                  finalScore += 5;
                }
                userDto.setScore(finalScore);
              }
              userDtoList.sort(
                  (userDTO1, userDTO2) ->
                      Integer.compare(userDTO2.getScore(), userDTO1.getScore()));
            }
          }
        });
  }

  public void setNotiListener(TextView indicator, TextView explainedText) {
    EventListener<QuerySnapshot> listener =
        ((value, error) -> {
          if (error != null) {
            Log.w("err", "listen:error", error);
            return;
          }

          for (DocumentChange dc : value.getDocumentChanges()) {
            switch (dc.getType()) {
              case ADDED:
                String last = indicator.getText().toString().replace("+", "").trim();
                String next = "+" + (Integer.parseInt(last) + 1);
                explainedText.setText(next);
                indicator.setText(next + " likes");
                break;
            }
          }
        });

    EventListener<QuerySnapshot> matchListener =
        ((value, error) -> {
          if (error != null) {
            Log.w("err", "listen:error", error);
            return;
          }

          for (DocumentChange dc : value.getDocumentChanges()) {
            switch (dc.getType()) {
              case ADDED:
                String last = indicator.getText().toString().replace("+", "").trim();
                String next = "+ " + (Integer.parseInt(last) - 1);
                explainedText.setText(next);
                indicator.setText(next + " likes");
                break;
            }
          }
        });

    notificationRef
        .whereEqualTo("receiverId", userService.getCurrentUser().getId())
        .whereEqualTo("mode", "MATCH")
        .addSnapshotListener(matchListener);

    notificationRef
        .whereEqualTo("receiverId", userService.getCurrentUser().getId())
        .whereEqualTo("mode", "LIKE")
        .addSnapshotListener(listener);
  }

  public void updateNumOfLiked(
      String userId, int mode, final FirebaseCallback<CallbackRes<Integer>> callback) {
    if (mode == 0) {
      userReference
          .document(userId)
          .update("numOfLiked", FieldValue.increment(1))
          .addOnCompleteListener(
              task -> {
                if (task.isSuccessful()) {
                  callback.callback(new CallbackRes.Success<>(0));
                  notificationService.sendNotification(userId, "Someone likes you!");
                } else {
                  callback.callback(new CallbackRes.Error(task.getException()));
                }
              });
    }
    if (mode == 1) {
      fireStore
          .runTransaction(
              new Transaction.Function<Void>() {
                @Override
                public Void apply(@NonNull Transaction transaction)
                    throws FirebaseFirestoreException {
                  DocumentSnapshot snapshot = transaction.get(userReference.document(userId));
                  Long newNumOfLiked = snapshot.getLong("numOfLiked") - 1;
                  transaction.update(userReference.document(userId), "numOfLiked", newNumOfLiked);

                  // Success
                  return null;
                }
              })
          .addOnCompleteListener(
              task -> {
                if (task.isSuccessful()) {
                  Log.d("Success!!!", "updated");
                }
              });
    }
  }
}
