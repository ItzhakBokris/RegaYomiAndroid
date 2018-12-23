package com.regayomi.ui.settings;

import android.content.SharedPreferences;

import com.regayomi.R;
import com.regayomi.ui.base.BasePreferenceFragment;

import androidx.annotation.NonNull;

public class NotificationSettingsFragment extends BasePreferenceFragment<SettingsViewModel> {

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getViewModel().scheduleArticleNotification();
    }

    @Override
    protected int getPreferenceResId() {
        return R.xml.preference_notifications;
    }

    @NonNull
    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }
}
