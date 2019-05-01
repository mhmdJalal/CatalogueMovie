package com.codepolitan.s4cataoguemovie.ui.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.codepolitan.s4cataoguemovie.R;


public class SettingFragment extends PreferenceFragment {
    public SettingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preference);
    }
}
