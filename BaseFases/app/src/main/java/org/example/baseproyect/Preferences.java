package org.example.baseproyect;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by usuwi on 17/04/2017.
 */

public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
