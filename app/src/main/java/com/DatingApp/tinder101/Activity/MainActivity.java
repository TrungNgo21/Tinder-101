package com.DatingApp.tinder101.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Fragments.SwipeFragment;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

  private UserDto currentUser;
  private UserService userService;
  private Stack<UserDto> users;

  private final FirebaseDatabase firebaseDatabase =
      FirebaseDatabase.getInstance(Constant.KEY_DATABASE_URL);
  private final DatabaseReference realTimeUserRef =
      firebaseDatabase.getReference().child(Constant.KEY_COLLECTION_USERS);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userService = new UserService(getApplicationContext());
    setContentView(R.layout.activity_main);
    Button logoutBtn = findViewById(R.id.logoutBtn);
    Button rightBtn = findViewById(R.id.rightSwipeBtn);

    TextView email = findViewById(R.id.userEmail);
    if (userService.getCurrentUser() != null) {
      currentUser = userService.getCurrentUser();
      email.setText(currentUser.getEmail());
    }
    userService.getAllUsers(
        new FirebaseCallback<CallbackRes<List<UserDto>>>() {
          @Override
          public void callback(CallbackRes<List<UserDto>> template) {
            if (template instanceof CallbackRes.Success) {
              users = ((CallbackRes.Success<Stack<UserDto>>) template).getData();
              updateListUsers();
              Fragment fragment = new SwipeFragment(users);
              loadFragment(fragment);
            } else {
              Toast.makeText(getApplicationContext(), template.toString(), Toast.LENGTH_LONG)
                  .show();
            }
          }
        });
    userService.getMatchedUsers(
        new FirebaseCallback<CallbackRes<List<UserDto>>>() {
          @Override
          public void callback(CallbackRes<List<UserDto>> template) {
            if (template instanceof CallbackRes.Success) {
              List<UserDto> matchedUsers =
                  ((CallbackRes.Success<List<UserDto>>) template).getData();
            }
          }
        });

    logoutBtn.setOnClickListener(
        view -> {
          userService.logout();
          finish();
          startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        });
    rightBtn.setOnClickListener(
        view -> {
          userService.rightSwipe(users.peek().getId());
          detectMatch();
          users.pop();
          updateListUsers();
        });
  }

  public void updateListUsers() {
    TextView user = findViewById(R.id.users);
    if (users.isEmpty()) {
      user.setText("Co cai dau buoi ma quet");
    } else {
      user.setText(users.peek().getEmail());
    }
  }

  public void detectMatch() {
    ChildEventListener childEventListener =
        new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            realTimeUserRef
                .child(currentUser.getId())
                .child("likedList")
                .child(dataSnapshot.getKey())
                .addValueEventListener(
                    new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                          userService.handleMatch(snapshot.getKey());
                          Toast.makeText(getApplicationContext(), "Matched!!!", Toast.LENGTH_LONG)
                              .show();
                        }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {}
                    });
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
            String commentKey = dataSnapshot.getKey();

            // ...
          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
            String commentKey = dataSnapshot.getKey();

            // ...
          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
            String commentKey = dataSnapshot.getKey();

            // ...
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {}
        };
    realTimeUserRef
        .child(currentUser.getId())
        .child("likeList")
        .addChildEventListener(childEventListener);
  }

  private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.swipeScreen1, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
