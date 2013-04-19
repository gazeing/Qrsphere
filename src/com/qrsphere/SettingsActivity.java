package com.qrsphere;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;

@SuppressWarnings("unused")
public class SettingsActivity extends PreferenceActivity {
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        addPreferencesFromResource(R.xml.perference);
    }
}