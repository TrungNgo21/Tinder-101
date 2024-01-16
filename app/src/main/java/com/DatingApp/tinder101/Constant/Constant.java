package com.DatingApp.tinder101.Constant;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {
  public static final String KEY_COLLECTION_USERS = "users";
  public static final String KEY_COLLECTION_MESSAGES = "messages";

  public static final String KEY_GOOGLE_MAP_API = "AIzaSyBiKCD0sJBfjF8OHDT6j-8iTHh3PAdB268";

  public static final String KEY_DATABASE_URL =
      "https://kinder-c0e08-default-rtdb.asia-southeast1.firebasedatabase.app/";

  public static final String KEY_SHARED_CONTEXT = "Tinder";
  public static final String KEY_CURRENT_USER = "currentUser";

  public static final String KEY_CACHE_REGISTER = "cacheUser";

  public static final String KEY_SIGN_IN = "isSignIn";

  public static final String KEY_SENT_USER_ID = "sentUserId";
  public static final String KEY_RECEIVED_USED_ID = "receivedUserId";
  public static final String KEY_MESSAGE_CONTENT_ID = "messageContent";

  public static final String KEY_CREATED_DATE_ID = "createDate";

  // Type looking for
  public static final String LONG_OK = "Long-term partner";
  public static final String LONG_SHORT_OK = "Long-term, but short-term OK";
  public static final String SHORT_LONG_OK = "Short-term, but long-term OK";
  public static final String SHORT_OK = "Short-term fun";
  public static final String FRIEND_OK = "New friends";
  public static final String THINKING = "Still figuring it out";

  // Type lifestyle
  public static final String PET = "Pets";
  public static final String DRINKING = "Drinking";
  public static final String SMOKE = "How often do you smoke?";
  public static final String WORKOUT = "Workout";

  // Type Basics
  public static final String ZODIAC = "Zodiac";
  public static final String EDUCATION = "Education";
  public static final String COMMUNICATION = "Communication style";
  public static final String LOVE = "Love style";

  // Conversation type
  public static final int SENT_TYPE = 1;
  public static final int RECEIVE_TYPE = 0;
}
