package com.example.alan.ntqmusicapp.fragment;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.alan.ntqmusicapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
