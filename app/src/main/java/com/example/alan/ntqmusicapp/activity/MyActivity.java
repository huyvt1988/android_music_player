package com.example.alan.ntqmusicapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {

    public static boolean isAppInFg = false;
    public static boolean isScrInFg = false;
    public static boolean isChangeScrFg = false;
    public static boolean isRunBackGround = false;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        if (!isAppInFg) {
            isAppInFg = true;
            isChangeScrFg = false;
            onAppStart();
        } else {
            isChangeScrFg = true;
        }
        isScrInFg = true;

        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        isRunBackGround = sharedPreferences.getBoolean("isRunBackGround", false);

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!isScrInFg || !isChangeScrFg) {
            isAppInFg = false;
            onAppPause();
        }
        isScrInFg = false;
    }

    public void onAppStart() {

    }

    public void onAppPause() {
        if(!isRunBackGround && ActivityListSong.isMusicBound()) {
            ActivityListSong.getMusicSrv().pausePlayer();
            ActivityPlayer.getImg_disk().clearAnimation();
            if(!ActivityListSong.playbackPaused)
                ActivityListSong.playbackPaused = true;

        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
