package com.DatingApp.tinder101.Constant;

import static java.util.Arrays.asList;

import com.DatingApp.tinder101.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {
  public static final String KEY_COLLECTION_USERS = "users";
  public static final String KEY_COLLECTION_MESSAGES = "messages";

  public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";

  public static final String KEY_GOOGLE_MAP_API = "AIzaSyBiKCD0sJBfjF8OHDT6j-8iTHh3PAdB268";

  public static final String KEY_DATABASE_URL =
      "https://kinder-c0e08-default-rtdb.asia-southeast1.firebasedatabase.app/";

  public static final String KEY_SHARED_CONTEXT = "Tinder";
  public static final String KEY_CURRENT_USER = "currentUser";

  public static final String KEY_CACHE_REGISTER = "cacheUser";

  public static final String KEY_SIGN_IN = "isSignIn";
  public static final List<String> interests = asList(new String[]{"Gaming", "Dancing", "Music", "Movie", "Photography", "Architecture", "Fashion", "Book", "Writing", "Painting",
          "Football", "People", "Animals", "Gym", "Food & Drink", "Travel & Places"});
  public static List<Integer> interestsIcon = asList(new Integer[]{R.drawable.ic_game, R.drawable.ic_dancing, R.drawable.ic_music, R.drawable.ic_movie, R.drawable.ic_photo, R.drawable.ic_architecture, R.drawable.ic_fashion, R.drawable.ic_book, R.drawable.ic_writing,
          R.drawable.ic_painting, R.drawable.ic_football, R.drawable.ic_people, R.drawable.ic_animal, R.drawable.ic_gym, R.drawable.ic_food, R.drawable.ic_travel});

  public static final String KEY_SENT_USER_ID = "sentUserId";
  public static final String KEY_RECEIVED_USED_ID = "receivedUserId";
  public static final String KEY_CONVERSION_NAME = "conversionName";
  public static final String KEY_CONVERSION_URL = "conversionUrl";

  public static final String KEY_RECEIVER_NAME = "receiverName";
  public static final String KEY_RECEIVER_URL = "receiverUrl";

  public static final String KEY_SENDER_NAME = "senderName";
  public static final String KEY_SENDER_URL = "senderUrl";
  public static final String KEY_CONVERSION_ID = "conversionId";

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
