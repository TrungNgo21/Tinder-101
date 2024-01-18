package com.DatingApp.tinder101.Activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.Service.UserService;
import com.DatingApp.tinder101.databinding.ActivityAddPhotoBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;

public class AddPhotoActivity extends AppCompatActivity {
  private ActivityAddPhotoBinding activityAddPhotoBinding;
  private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
  private StorageReference storageReference;
  private UserService userService;

  private Uri imageUri1;
  private Uri imageUri2;
  private Uri imageUri3;
  private Uri imageUri4;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityAddPhotoBinding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
    userService = new UserService(getApplicationContext());
    setContentView(activityAddPhotoBinding.getRoot());
    setUpImagePicker();
    checkImage();
    setButton();
  }

  public void setUpImagePicker() {
    ActivityResultLauncher<Intent> activityResultLauncher1 =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                  Intent data = result.getData();
                  imageUri1 = data.getData();
                  activityAddPhotoBinding.uploadImage1.setImageURI(imageUri1);
                  checkImage();
                } else {
                  Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT)
                      .show();
                }
              }
            });
    ActivityResultLauncher<Intent> activityResultLauncher2 =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                  Intent data = result.getData();
                  imageUri2 = data.getData();
                  activityAddPhotoBinding.uploadImage2.setImageURI(imageUri2);
                  checkImage();
                } else {
                  Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT)
                      .show();
                }
              }
            });
    ActivityResultLauncher<Intent> activityResultLauncher3 =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                  Intent data = result.getData();
                  imageUri3 = data.getData();
                  checkImage();
                  activityAddPhotoBinding.uploadImage3.setImageURI(imageUri3);
                } else {
                  Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT)
                      .show();
                }
              }
            });
    ActivityResultLauncher<Intent> activityResultLauncher4 =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                  Intent data = result.getData();
                  imageUri4 = data.getData();
                  activityAddPhotoBinding.uploadImage4.setImageURI(imageUri4);
                  checkImage();
                } else {
                  Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT)
                      .show();
                }
              }
            });
    activityAddPhotoBinding.uploadImage1.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent photoPicker = new Intent();
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            photoPicker.setType("image/*");
            activityResultLauncher1.launch(photoPicker);
          }
        });
    activityAddPhotoBinding.uploadImage2.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent photoPicker = new Intent();
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            photoPicker.setType("image/*");
            activityResultLauncher2.launch(photoPicker);
          }
        });
    activityAddPhotoBinding.uploadImage3.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent photoPicker = new Intent();
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            photoPicker.setType("image/*");
            activityResultLauncher3.launch(photoPicker);
          }
        });
    activityAddPhotoBinding.uploadImage4.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent photoPicker = new Intent();
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            photoPicker.setType("image/*");
            activityResultLauncher4.launch(photoPicker);
          }
        });
  }

  public void checkImage() {
    if (imageUri1 == null && imageUri2 == null && imageUri3 == null && imageUri4 == null) {
      activityAddPhotoBinding.photoError.setVisibility(View.VISIBLE);
      activityAddPhotoBinding.continueButtonId.setEnabled(false);
    } else {
      activityAddPhotoBinding.photoError.setVisibility(View.INVISIBLE);
      activityAddPhotoBinding.continueButtonId.setEnabled(true);
    }
  }

  public void setButton() {
    storageReference = firebaseStorage.getReference().child(userService.getCurrentUser().getId());
    activityAddPhotoBinding.continueButtonId.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int nonNullUri = 0;
            HashMap<String, String> updateMap = new HashMap<>();
            List<Uri> images =
                new ArrayList<>(Arrays.asList(imageUri1, imageUri2, imageUri3, imageUri4));
            List<String> urls = new ArrayList<>();

            for (int i = 0; i < images.size(); i++) {
              if (images.get(i) != null) nonNullUri++;
            }

            for (int i = 0; i < images.size(); i++) {
              if (images.get(i) != null) {
                UUID uuid = UUID.randomUUID();
                final StorageReference imageReference =
                    storageReference.child(String.valueOf(uuid));

                UploadTask uploadTask = imageReference.putFile(images.get(i));
                int finalNonNullUri = nonNullUri;
                uploadTask
                    .continueWithTask(
                        new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                          @Override
                          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                              throws Exception {
                            if (!task.isSuccessful()) {
                              throw task.getException();
                            }
                            return imageReference.getDownloadUrl();
                          }
                        })
                    .addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                          @Override
                          public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                              Uri downloadUri = task.getResult();
                              Log.d("link", String.valueOf(downloadUri));
                              urls.add(String.valueOf(downloadUri));
                              if (urls.size() == finalNonNullUri) {
                                for (int j = 0; j < urls.size(); j++) {
                                  updateMap.put(String.valueOf(j), String.valueOf(downloadUri));
                                }
                                userService.updateImages(updateMap);
                              }

                            } else {
                              Log.d(TAG, "The bug is that " + task.getException().getMessage());
                            }
                          }
                        });
              }
            }

            startActivity(new Intent(getApplicationContext(), AddInterestActivity.class));
          }
        });
  }
}
