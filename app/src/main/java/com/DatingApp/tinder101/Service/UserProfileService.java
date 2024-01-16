package com.DatingApp.tinder101.Service;

import androidx.annotation.NonNull;

import com.DatingApp.tinder101.Callback.CallbackRes;
import com.DatingApp.tinder101.Callback.FirebaseCallback;
import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.ProfileSettingDto;
import com.DatingApp.tinder101.Dto.UserDto;
import com.DatingApp.tinder101.Model.ProfileSetting;
import com.DatingApp.tinder101.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserProfileService {
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private UserDto currentUser;
    private UserService userService;
    private User currentUserModel;

    //Profile setting attributes
    private String quotes;
    private List<String> interests;
    private HashMap<String,String> lifeStyleList;
    private HashMap<String, String> basics;
    private String lookingForEnum;

    public UserProfileService(UserService userService) {
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userService = userService;
        this.currentUser = this.userService.getCurrentUser();
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void appendInterests(String interest) {
        this.interests.add(interest);
    }

    public void setLifeStyleList(HashMap<String, String> lifeStyleList) {
        this.lifeStyleList = lifeStyleList;
    }

    public void appendLifeStyleList(String key, String value) {
        this.lifeStyleList.put(key, value);
    }

    public void setBasics(HashMap<String, String> basics) {
        this.basics = basics;
    }

    public void appendBasics(String key, String value) {
        this.basics.put(key, value);
    }

    public void setLookingForEnum(String lookingForEnum) {
        this.lookingForEnum = lookingForEnum;
    }

    public ProfileSetting setProfileSetting() {
        return  ProfileSetting.builder()
                .quotes(quotes)
                .lifestyleList(lifeStyleList)
                .interests(interests)
                .basics(basics)
                .lookingForEnum(lookingForEnum)
                .build();
    }



    public void updateUserProfileSetting() {
        this.userService.updateProfile(new FirebaseCallback<CallbackRes<UserDto>>() {
            @Override
            public void callback(CallbackRes<UserDto> template) {
                if(template instanceof CallbackRes.Success){
                    currentUser.setProfileSetting(setProfileSetting().toDto());
                    userService.setCurrentUser(currentUser);
                }
            }
        }, this.setProfileSetting());

    }

}
