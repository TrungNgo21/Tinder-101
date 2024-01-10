package com.DatingApp.tinder101.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.ProfileSettingDto;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Enum.LookingForEnum;
import com.DatingApp.tinder101.Model.ProfileSetting;
import com.DatingApp.tinder101.Model.User;
import com.DatingApp.tinder101.Utils.EnumConverter;
import com.DatingApp.tinder101.Utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class UserService {
  private PreferenceManager preferenceManager;
  private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
  private final FirebaseDatabase firebaseDatabase =
      FirebaseDatabase.getInstance(Constant.KEY_DATABASE_URL);
  private final DatabaseReference realTimeUserRef =
      firebaseDatabase.getReference().child(Constant.KEY_COLLECTION_USERS);

  private final CollectionReference userReference =
      fireStore.collection(Constant.KEY_COLLECTION_USERS);

  public UserService(Context context) {
    this.preferenceManager = new PreferenceManager(context.getApplicationContext());
  }

  public void rightSwipe(String userId) {
    Map<String, Object> updates = new HashMap<>();
    updates.put(preferenceManager.getCurrentUser().getId() + "/" + "likeList/" + userId, true);
    updates.put(userId + "/" + "likedList/" + preferenceManager.getCurrentUser().getId(), true);
    realTimeUserRef.updateChildren(updates);
  }

  public void handleMatch(String userId) {
    Map<String, Object> updates = new HashMap<>();
    WriteBatch batch = fireStore.batch();
    updates.put(preferenceManager.getCurrentUser().getId() + "/" + "likeList/" + userId, null);
    updates.put(preferenceManager.getCurrentUser().getId() + "/" + "likedList/" + userId, null);
    updates.put(userId + "/" + "likedList/" + preferenceManager.getCurrentUser().getId(), null);
    updates.put(userId + "/" + "likeList/" + preferenceManager.getCurrentUser().getId(), null);
    batch.update(
        userReference.document(preferenceManager.getCurrentUser().getId()),
        "matchedUsers",
        FieldValue.arrayUnion(userId));
    batch.update(
        userReference.document(userId),
        "matchedUsers",
        FieldValue.arrayUnion(preferenceManager.getCurrentUser().getId()));
    batch
        .commit()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                Log.d("update successful", "Success");

              } else {
                Log.d("update failed", "Faillllll");
              }
            });

    //    updates.put(preferenceManager.getCurrentUser().getId() + "/" + "matched/" + userId, true);
    //    updates.put(userId + "/" + "matched/" + preferenceManager.getCurrentUser().getId(), true);
    realTimeUserRef.updateChildren(updates);
  }

  public void getMatchedUsers(final FirebaseCallback<CallbackRes<List<UserDto>>> callback) {
    List<UserDto> matchedUsers = new ArrayList<>();
    fireStore
        .runTransaction(
            new Transaction.Function<Void>() {
              @Nullable
              @Override
              public Void apply(@NonNull Transaction transaction)
                  throws FirebaseFirestoreException {
                List<DocumentSnapshot> userSnapshots = new ArrayList<>();

                for (String userId : preferenceManager.getCurrentUser().getMatchedUsers()) {
                  DocumentSnapshot userSnapshot = transaction.get(userReference.document(userId));
                  userSnapshots.add(userSnapshot);
                }
                for (DocumentSnapshot snapshot : userSnapshots) {
                  if (snapshot.exists()) {
                    UserDto user = snapshot.toObject(UserDto.class);
                    user.setId(snapshot.getId());
                    matchedUsers.add(user);
                  }
                }
                return null;
              }
            })
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {

                callback.callback(new CallbackRes.Success<>(matchedUsers));
              } else {
                callback.callback(new CallbackRes.Error(task.getException()));
              }
            });
  }

  public boolean getLogInStatus() {
    return preferenceManager.getBoolean(Constant.KEY_SIGN_IN);
  }

  public void setCurrentUser(UserDto currentUser) {
    preferenceManager.putCurrentUser(currentUser);
  }

  public UserDto getCurrentUser() {
    return preferenceManager.getCurrentUser();
  }

  public void register(
      String email, String password, final FirebaseCallback<CallbackRes<UserDto>> callback) {
    firebaseAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            registerTask -> {
              if (registerTask.isSuccessful()) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                User createdUser =
                    User.builder()
                        .name("Test")
                        .email(email)
                        .profileSetting(
                            ProfileSetting.builder()
                                .basics(Arrays.asList("Smoke", "Moaa"))
                                .interests(Arrays.asList("BaseBall", "LickBack"))
                                .lifestyleList(Arrays.asList("hihihihh"))
                                .lookingForEnum(LookingForEnum.SHORT_LONG_OK.toString())
                                .quotes("Qua la tuyet voi")
                                .build())
                        .imageUrlsMap(
                            new HashMap<String, String>() {
                              {
                                put(
                                    "0",
                                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fimages%2Fanimals%2Fcat&psig=AOvVaw1Drslg04h5cWQauEC9tDNi&ust=1704874937406000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLjty5nwz4MDFQAAAAAdAAAAABAE");
                                put(
                                    "1",
                                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fimages%2Fanimals%2Fcat&psig=AOvVaw1Drslg04h5cWQauEC9tDNi&ust=1704874937406000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLjty5nwz4MDFQAAAAAdAAAAABAE");
                                put(
                                    "2",
                                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fimages%2Fanimals%2Fcat&psig=AOvVaw1Drslg04h5cWQauEC9tDNi&ust=1704874937406000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLjty5nwz4MDFQAAAAAdAAAAABAE");
                                put(
                                    "3",
                                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fimages%2Fanimals%2Fcat&psig=AOvVaw1Drslg04h5cWQauEC9tDNi&ust=1704874937406000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCLjty5nwz4MDFQAAAAAdAAAAABAE");
                              }
                            })
                        .createdDate(new Date())
                        .updatedDate(new Date())
                        .build();
                HashMap<String, Object> userMap = createdUser.toMap();
                userMap.put("id", firebaseUser.getUid());
                realTimeUserRef
                    .child(firebaseUser.getUid())
                    .setValue(userMap)
                    .addOnCompleteListener(
                        updateTask -> {
                          if (updateTask.isSuccessful()) {

                          } else {
                            callback.callback(new CallbackRes.Error(updateTask.getException()));
                          }
                        });
                userReference
                    .document(firebaseUser.getUid())
                    .set(createdUser)
                    .addOnCompleteListener(
                        task -> {
                          if (task.isSuccessful()) {
                            UserDto currentUser = createdUser.toDto();
                            currentUser.setId(firebaseUser.getUid());
                            preferenceManager.putCurrentUser(currentUser);
                            preferenceManager.putBoolean(Constant.KEY_SIGN_IN, true);
                            callback.callback(new CallbackRes.Success<UserDto>(currentUser));
                          } else {
                            callback.callback(new CallbackRes.Error(task.getException()));
                          }
                        });
              }
            });
  }

  public void login(
      String email, String password, final FirebaseCallback<CallbackRes<UserDto>> callback) {
    firebaseAuth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            singInTask -> {
              if (singInTask.isSuccessful()) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                realTimeUserRef.child(firebaseUser.getUid() + "/isOnline").setValue(true);
                userReference
                    .document(firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(
                        getUserTask -> {
                          if (getUserTask.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = getUserTask.getResult();
                            UserDto currentUser = documentSnapshot.toObject(UserDto.class);
                            currentUser.setOnline(true);
                            currentUser.setId(firebaseUser.getUid());
                            preferenceManager.putCurrentUser(currentUser);
                            preferenceManager.putBoolean(Constant.KEY_SIGN_IN, true);
                            callback.callback(new CallbackRes.Success<UserDto>(currentUser));
                          } else {
                            callback.callback(new CallbackRes.Error(getUserTask.getException()));
                          }
                        });
              } else {
                callback.callback(new CallbackRes.Error(singInTask.getException()));
              }
            });
  }

  public void logout() {
    preferenceManager.clear();
  }

  public void getAllUsers(final FirebaseCallback<CallbackRes<List<UserDto>>> callback) {
    List<UserDto> users = new Stack<>();
    userReference
        .get()
        .addOnCompleteListener(
            getUsersTask -> {
              if (getUsersTask.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : getUsersTask.getResult()) {
                  if (!documentSnapshot
                      .getId()
                      .equals(preferenceManager.getCurrentUser().getId())) {
                    UserDto user = documentSnapshot.toObject(UserDto.class);
                    user.setId(documentSnapshot.getId());
                    users.add(user);
                  }
                }
                callback.callback(new CallbackRes.Success<>(users));
              } else {
                callback.callback(new CallbackRes.Error(getUsersTask.getException()));
              }
            });
  }
}
