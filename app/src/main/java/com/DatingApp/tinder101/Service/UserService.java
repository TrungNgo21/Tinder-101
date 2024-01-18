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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

    public interface CallbackListener {
        public void onSuccessCallBack(String message);
        public void onFailureCallBack(String message);
    }
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

    realTimeUserRef.child(getCurrentUser().getId()).child("likeList").child(userId).removeValue();
    realTimeUserRef.child(getCurrentUser().getId()).child("likedList").child(userId).removeValue();
    realTimeUserRef.child(userId).child("likeList").child(getCurrentUser().getId()).removeValue();
    realTimeUserRef.child(userId).child("likedList").child(getCurrentUser().getId()).removeValue();
    userReference
        .document(preferenceManager.getCurrentUser().getId())
        .update("matchedUsers", FieldValue.arrayUnion(userId));
    userReference
        .document(userId)
        .update("matchedUsers", FieldValue.arrayUnion(preferenceManager.getCurrentUser().getId()));
    preferenceManager.getCurrentUser().getMatchedUsers().add(userId);
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
                  if (userId.equals(preferenceManager.getCurrentUser().getId())) {
                    continue;
                  }
                  DocumentSnapshot userSnapshot = transaction.get(userReference.document(userId));
                  userSnapshots.add(userSnapshot);
                }
                for (DocumentSnapshot snapshot : userSnapshots) {
                  if (snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    UserDto userDto = user.toDto();
                    userDto.setId(snapshot.getId());
                    matchedUsers.add(userDto);
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
                //                sendVerificationEmail();
                User createdUser =
                    User.builder()
                        .name("Test")
                        .email(email)
                        .profileSetting(
                            ProfileSetting.builder()
                                .basics(
                                    new HashMap<String, String>() {
                                      {
                                        put("ZODIAC", "Cancer");
                                        put("EDUCATION", "At uni");
                                        put("COMMUNICATION", "Better in person");
                                        put("LOVE", "Touch");
                                      }
                                    })
                                .interests(Arrays.asList("BaseBall", "LickBack"))
                                .lifestyleList(
                                    new HashMap<String, String>() {
                                      {
                                        put("PET", "No pet");
                                        put("SMOKE", "Social smoker");
                                        put("DRINKING", "Occasion");
                                        put("WORKOUT", "Everyday");
                                      }
                                    })
                                .lookingForEnum(LookingForEnum.SHORT_LONG_OK.toString())
                                .quotes("Qua la tuyet voi")
                                .build())
                        .imageUrlsMap(new HashMap<String, String>())
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

  private void sendVerificationEmail() {
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    currentUser
        .sendEmailVerification()
        .addOnCompleteListener(
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  firebaseAuth.signOut();
                }
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d("Failed", e.getMessage().toString());
              }
            });
  }

  public void login(
      String email, String password, final FirebaseCallback<CallbackRes<UserDto>> callback, CallbackListener listener) {
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
                            User getUser = documentSnapshot.toObject(User.class);
                            UserDto currentUser = getUser.toDto();
                            currentUser.setOnline(true);
                            currentUser.setId(firebaseUser.getUid());
                            preferenceManager.putCurrentUser(currentUser);
                            preferenceManager.putBoolean(Constant.KEY_SIGN_IN, true);
                            callback.callback(new CallbackRes.Success<UserDto>(currentUser));

                          } else {
                            callback.callback(new CallbackRes.Error(getUserTask.getException()));
                            listener.onFailureCallBack(getUserTask.getException().getMessage());
                          }
                        });
              } else {
                callback.callback(new CallbackRes.Error(singInTask.getException()));
                listener.onFailureCallBack(singInTask.getException().getMessage());
              }
            });
  }

  public void logout() {
    preferenceManager.clear();
  }

  public void getOneUser(String id, final FirebaseCallback<CallbackRes<UserDto>> callback) {
    userReference
        .document(id)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);
                UserDto userDto = user.toDto();
                userDto.setId(task.getResult().getId());
                callback.callback(new CallbackRes.Success<UserDto>(userDto));
              } else {
                callback.callback(new CallbackRes.Error(task.getException()));
              }
            });
  }

  public void getAllUsers(final FirebaseCallback<CallbackRes<List<UserDto>>> callback) {
    List<UserDto> users = new ArrayList<>();
    userReference
        .get()
        .addOnCompleteListener(
            getUsersTask -> {
              if (getUsersTask.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : getUsersTask.getResult()) {
                  if (!documentSnapshot
                      .getId()
                      .equals(preferenceManager.getCurrentUser().getId())) {
                    User user = documentSnapshot.toObject(User.class);
                    UserDto getUser = user.toDto();
                    getUser.setId(documentSnapshot.getId());
                    users.add(getUser);
                  }
                }
                callback.callback(new CallbackRes.Success<>(users));
              } else {
                callback.callback(new CallbackRes.Error(getUsersTask.getException()));
              }
            });
  }
  public void updateProfile(final FirebaseCallback<CallbackRes<UserDto>> callback, ProfileSetting profileSetting, CallbackListener callbackListener){
      userReference.document(getCurrentUser().getId()).update("profileSetting", profileSetting).addOnCompleteListener(
              updateUserTask -> {
                  if (updateUserTask.isSuccessful()) {
                      callback.callback(new CallbackRes.Success<>(getCurrentUser()));
                      callbackListener.onSuccessCallBack("Update profile successfully");
                  } else {
                      callback.callback(new CallbackRes.Error(updateUserTask.getException()));
                      callbackListener.onFailureCallBack(updateUserTask.getException().getMessage());
                  }
              }
            });
  }

  public void updateName(String name, final FirebaseCallback<CallbackRes<UserDto>> callback) {
    userReference
        .document(getCurrentUser().getId())
        .update("name", name)
        .addOnCompleteListener(
            updateTask -> {
              if (updateTask.isSuccessful()) {
                callback.callback(new CallbackRes.Success<>(getCurrentUser()));
              } else {
                callback.callback(new CallbackRes.Error(updateTask.getException()));
              }
            });
  }

  public void updateGender(String gender, final FirebaseCallback<CallbackRes<UserDto>> callback) {
    userReference
        .document(getCurrentUser().getId())
        .update("gender", gender)
        .addOnCompleteListener(
            updateTask -> {
              if (updateTask.isSuccessful()) {
                callback.callback(new CallbackRes.Success<>(getCurrentUser()));
              } else {
                callback.callback(new CallbackRes.Error(updateTask.getException()));
              }
            });
  }

  public void updateAge(int age, final FirebaseCallback<CallbackRes<UserDto>> callback) {
    userReference
        .document(getCurrentUser().getId())
        .update("age", age)
        .addOnCompleteListener(
            updateTask -> {
              if (updateTask.isSuccessful()) {
                callback.callback(new CallbackRes.Success<>(getCurrentUser()));
              } else {
                callback.callback(new CallbackRes.Error(updateTask.getException()));
              }
            });
  }

  public void updateImages(HashMap<String, String> map) {
    userReference.document(getCurrentUser().getId()).update("imageUrlsMap", map);
  }
}
