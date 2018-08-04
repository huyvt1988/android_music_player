package com.example.alan.ntqmusicapp.activity;

import android.os.Bundle;

import com.example.alan.ntqmusicapp.fragment.SettingsFragment;

public class ActivitySetting extends MyActivity {
    public static final String KEY_RUN_BACKGROUND_SWITCH = "RunBackground";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
