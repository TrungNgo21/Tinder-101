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
}
