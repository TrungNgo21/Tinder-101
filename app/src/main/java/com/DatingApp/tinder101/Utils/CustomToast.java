package com.DatingApp.tinder101.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.DatingApp.tinder101.R;

public class CustomToast extends Toast {
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int SHORT = 4000;
    public static final int LONG = 7000;


    public CustomToast(Context context) {
        super(context);
    }
    public static Toast makeText(Context context, int duration, int type){
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        View layout = LayoutInflater.from(context).inflate(R.layout.edit_profile_custom_toast, null, false);
        TextView textView = layout.findViewById(R.id.status_text);
        TextView notification = layout.findViewById(R.id.notification_text);
        if(type == 1){
            textView.setText("Success!");
            notification.setText("Your selections have been updated");
        }
        else {
            textView.setText("Failed");
            notification.setText("Your selections have not been updated");
        }
        toast.setView(layout);
        return toast;
    }
}
