package com.swapnil.yummly_proj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.swapnil.yummly_proj.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Timer().schedule(new TimerTask(){
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        }, 3000);

    }
}
