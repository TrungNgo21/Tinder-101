package com.DatingApp.tinder101.Utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPass(String password, int lowerBound, int upperBound) {
        return password.length() >= lowerBound && password.length() <= upperBound;
    }

    public static boolean isValidNumCharacters(String input, int lowerBound, int upperBound) {
        return input.length() >= lowerBound && input.length() <= upperBound;
    }
    public static boolean isValidPhoneNum(String input){
        Pattern pattern =Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
