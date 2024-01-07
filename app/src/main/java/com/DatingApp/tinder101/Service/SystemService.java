package com.DatingApp.tinder101.Service;

import com.DatingApp.tinder101.Constant.Constant;
import com.DatingApp.tinder101.Dto.UserDto;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Stack;

public class SystemService {
  private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
  private final CollectionReference userReference =
      fireStore.collection(Constant.KEY_COLLECTION_USERS);
}
