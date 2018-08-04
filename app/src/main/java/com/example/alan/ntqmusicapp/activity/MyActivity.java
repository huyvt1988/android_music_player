package com.example.alan.ntqmusicapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import com.example.alan.ntqmusicapp.R;

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

        //RunBackGround
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isRunBackGround = sharedPreferences.getBoolean(ActivitySetting.KEY_RUN_BACKGROUND_SWITCH, false);

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
        isRunBackGround = sharedPreferences.getBoolean(ActivitySetting.KEY_RUN_BACKGROUND_SWITCH, false);
        if (!isRunBackGround && ActivityListSong.isMusicBound()) {
            if (ActivityListSong.getMusicSrv().isPng())
                ActivityListSong.getMusicSrv().pausePlayer();
            if (ActivityPlayer.getImg_disk() != null)
                ActivityPlayer.getImg_disk().clearAnimation();
            if (!ActivityListSong.playbackPaused)
                ActivityListSong.playbackPaused = true;

        }
    }
}
