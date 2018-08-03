package com.example.alan.ntqmusicapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.example.alan.ntqmusicapp.R;

public class ActivitySetting extends MyActivity {
    CheckBox ckb_setting;
    boolean isCheked = false;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_setting);
        initControl();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCheked = ckb_setting.isChecked();
//        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        sharedPreferences = MyActivity.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRunBackGround", isCheked);
        editor.commit();
    }

    private void initData() {
        isCheked = MyActivity.isRunBackGround;
        ckb_setting.setChecked(isCheked);

    }

    private void initControl() {
        ckb_setting = findViewById(R.id.ckb_setting);
    }
}
