package com.nganlth.freshfoodadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class FlashScreen extends AppCompatActivity {
    public static int SPLASH_SCREEN = 6000;
    Animation topAnim, bottomAnim;
    LottieAnimationView ivWelcome;
    TextView tvFreshFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Animation
//        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Tham chiếu
//        ivWelcome = findViewById(R.id.ivFlash);
//        tvFreshFood = findViewById(R.id.tvFreshFood);
        // Set Animation
//        tvFreshFood.setAnimation(bottomAnim);
        //Set animation
//        ivWelcome.animate().translationY(-1600).setDuration(1000).setStartDelay(5000);
//        tvFreshFood.animate().translationY(1400).setDuration(1000).setStartDelay(5000);
        // Chuyển hướng sang Login

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FlashScreen.this,Login.class);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN);
    }
}