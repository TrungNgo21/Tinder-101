package com.DatingApp.tinder101.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.DatingApp.tinder101.R;
import com.DatingApp.tinder101.databinding.ActivityAddPhotoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AddPhotoActivity extends AppCompatActivity {
    private ActivityAddPhotoBinding activityAddPhotoBinding;
    private Uri imageUri1;
    private Uri imageUri2;
    private Uri imageUri3;
    private Uri imageUri4;
    StorageReference storageReference;
    private HashMap<Integer, String> imageString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddPhotoBinding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
        setContentView(activityAddPhotoBinding.getRoot());
        setUpImagePicker();
        checkImage();
        setButton();


    }
    public void setUpImagePicker(){
        ActivityResultLauncher<Intent> activityResultLauncher1 = registerForActivityResult(
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
                            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        ActivityResultLauncher<Intent> activityResultLauncher2 = registerForActivityResult(
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
                            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        ActivityResultLauncher<Intent> activityResultLauncher3 = registerForActivityResult(
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
                            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        ActivityResultLauncher<Intent> activityResultLauncher4 = registerForActivityResult(
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
                            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        activityAddPhotoBinding.uploadImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher1.launch(photoPicker);
            }
        });
        activityAddPhotoBinding.uploadImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher2.launch(photoPicker);
            }
        });
        activityAddPhotoBinding.uploadImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher3.launch(photoPicker);
            }
        });
        activityAddPhotoBinding.uploadImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher4.launch(photoPicker);
            }
        });
    }
    public void checkImage(){
        if(imageUri1 == null && imageUri2 == null && imageUri3 == null && imageUri4 == null){
            activityAddPhotoBinding.photoError.setVisibility(View.VISIBLE);
            activityAddPhotoBinding.continueButtonId.setEnabled(false);
        }
        else {
            activityAddPhotoBinding.photoError.setVisibility(View.INVISIBLE);
            activityAddPhotoBinding.continueButtonId.setEnabled(true);
        }
    }
    public void setButton(){
        activityAddPhotoBinding.continueButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), AddInterestActivity.class));

            }
        });
    }

}