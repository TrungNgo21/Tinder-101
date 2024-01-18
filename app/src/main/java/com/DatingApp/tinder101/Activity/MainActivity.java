package com.DatingApp.tinder101.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.DatingApp.tinder101.Adapter.UserCardAdapter;
import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Fragments.ChatFragment;
import com.DatingApp.tinder101.Fragments.ProfilePreviewFragment;
import com.DatingApp.tinder101.Fragments.SwipeFragment;
import com.DatingApp.tinder101.Fragments.ViewProfileFragment;
import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity
    implements SwipeFragment.OnMainEventHandle, ViewProfileFragment.OnBackSwipePress {

  private ActivityMainBinding activityMainBinding;
  private UserDto currentUser;
  private UserService userService;
  private List<UserDto> users;

  private List<UserDto> matchedUsers;

  private final FirebaseDatabase firebaseDatabase =
      FirebaseDatabase.getInstance(Constant.KEY_DATABASE_URL);
  private final DatabaseReference realTimeUserRef =
      firebaseDatabase.getReference().child(Constant.KEY_COLLECTION_USERS);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
    userService = new UserService(getApplicationContext());
    currentUser = userService.getCurrentUser();
    setMatchListener();
    setContentView(activityMainBinding.getRoot());
    Button logoutBtn = findViewById(R.id.logoutBtn);
    userService.getAllUsers(
        new FirebaseCallback<CallbackRes<List<UserDto>>>() {
          @Override
          public void callback(CallbackRes<List<UserDto>> template) {
            if (template instanceof CallbackRes.Success) {
              users = ((CallbackRes.Success<List<UserDto>>) template).getData();
              Fragment fragment = new SwipeFragment(users, MainActivity.this);
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
              matchedUsers = ((CallbackRes.Success<List<UserDto>>) template).getData();
            }
          }
        });

    logoutBtn.setOnClickListener(
        view -> {
          userService.logout();
          finish();
          startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        });

    setUpNavigation();
  }

  public void setMatchListener() {
    realTimeUserRef
        .child(currentUser.getId())
        .child("likeList")
        .addChildEventListener(
            new ChildEventListener() {

              @Override
              public void onChildAdded(
                  @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                realTimeUserRef
                    .child(currentUser.getId())
                    .child("likedList")
                    .child(snapshot.getKey())
                    .addValueEventListener(
                        new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            if (snapshot1.exists()) {
                              userService.handleMatch(snapshot1.getKey());
                              Toast.makeText(
                                      getApplicationContext(), "Matched!!!", Toast.LENGTH_LONG)
                                  .show();
                            }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {}
                        });
              }

              @Override
              public void onChildChanged(
                  @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

              @Override
              public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

              @Override
              public void onChildMoved(
                  @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });
  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.swipeScreen1, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void showDetail(UserDto userDto) {
    Fragment fragment = new ViewProfileFragment(userDto, true, this);
    loadFragment(fragment);
  }

  @Override
  public void dragRight(boolean isOn) {}

  @Override
  public void dragLeft(boolean isOn) {}

  @Override
  public void backToSwipe() {
    Fragment fragment = new SwipeFragment(users, this);
    loadFragment(fragment);
  }

  private void setUpNavigation() {
    activityMainBinding.menu.setOnItemSelectedListener(
        item -> {
          Fragment fragment;
          if (item.getItemId() == R.id.home) {
            fragment = new SwipeFragment(users, this);
            loadFragment(fragment);
            return true;
          } else if (item.getItemId() == R.id.profile) {
            fragment = new ProfilePreviewFragment();
            loadFragment(fragment);
            return true;
          } else if (item.getItemId() == R.id.message) {
            fragment = new ChatFragment(users);
            loadFragment(fragment);
            return true;
          } else {
            return false;
          }
        });
  }
}
