package com.DatingApp.tinder101.Service;

import com.DatingApp.tinder101.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileService {
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private User currentUserModel;

    public UserProfileService() {
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
    }

    private void setUpUserProfile()

}
