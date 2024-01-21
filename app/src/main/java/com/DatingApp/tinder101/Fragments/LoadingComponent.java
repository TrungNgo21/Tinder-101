package com.DatingApp.tinder101.Fragments;

import static com.cloudinary.android.MediaManager.init;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.DatingApp.tinder101.R;

public class LoadingComponent extends RelativeLayout {

  View root;
  AnimationDrawable animationDrawable;

  public LoadingComponent(Context context) {
    super(context);
    init();
  }

  public LoadingComponent(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public LoadingComponent(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public LoadingComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.loading_view, this, true);

    Window window = ((Activity) getContext()).getWindow();
    View decor = window.getDecorView();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false);
    } else {
      decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    root = findViewById(R.id.radial_animation);
    animationDrawable = (AnimationDrawable) root.getBackground();
    animationDrawable.setEnterFadeDuration(1000);
    animationDrawable.setExitFadeDuration(1000);
  }

  public void showLoading() {
    animationDrawable.start();
    setVisibility(VISIBLE);
  }

  public void hideLoading() {
    animationDrawable.stop();
    setVisibility(GONE);
  }
}
