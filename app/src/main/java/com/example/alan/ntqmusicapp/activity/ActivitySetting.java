package com.example.alan.ntqmusicapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.example.alan.ntqmusicapp.R;

public class ActivitySetting extends AppCompatActivity {
    CheckBox ckb_setting;
    boolean isCheked = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_setting);
        initControl();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCheked = ckb_setting.isChecked();
        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isBackGround", isCheked);
        editor.commit();
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        isCheked = sharedPreferences.getBoolean("isBackGround", false);
        ckb_setting.setChecked(isCheked);

    }

    private void initControl() {
        ckb_setting = findViewById(R.id.ckb_setting);
    }
}
