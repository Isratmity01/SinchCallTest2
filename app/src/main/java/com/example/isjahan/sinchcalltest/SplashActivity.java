package com.example.isjahan.sinchcalltest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.isjahan.sinchcalltest.dbhelper.DatabaseHelper;
import com.example.isjahan.sinchcalltest.model.UserCalls;


public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS = 1000;
    private Handler mHandler;
    private Runnable mRunnable;
     UserCalls userCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        final DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
userCalls=databaseHelper.getMe();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                // check if user is already logged in or not
                if(userCalls.getIs_me()==1) {
                    openCallActivity();
                        // if logged in redirect the user to user listing activity

                    } else {
                    openLoginActivity();

                    }
                    finish();
                }


        };

        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }*/
    private void openLoginActivity() {

        Intent mainActivity = new Intent(this, LoginActivity.class);
        this.finish();
        startActivity(mainActivity);
    }
    private void openCallActivity() {

        Intent mainActivity = new Intent(this, LogActivity.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LoginName", userCalls.getUserName());
        editor.apply();
        this.finish();
        startActivity(mainActivity);
    }
}

