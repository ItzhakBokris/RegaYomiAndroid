package com.regayomi.ui.base;


import android.os.Bundle;

import androidx.annotation.XmlRes;
import androidx.preference.PreferenceFragmentCompat;

public abstract class BasePreferenceFragment<V extends BaseViewModel> extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(getPreferenceResId(), rootKey);
    }

    /**
     * Gets the resource-id of the preference hierarchy xml.
     */
    @XmlRes
    protected abstract int getPreferenceResId();
}
