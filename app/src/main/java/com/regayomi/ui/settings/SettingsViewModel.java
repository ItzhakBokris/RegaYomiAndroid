package com.regayomi.ui.settings;

import android.app.Application;
import android.os.Bundle;

import com.regayomi.notifications.NotificationsManager;
import com.regayomi.ui.base.BaseViewModel;

import androidx.annotation.NonNull;

public class SettingsViewModel extends BaseViewModel {

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Schedules new article notification according to the app state and the user settings.
     */
    public void scheduleArticleNotification() {
        NotificationsManager.getInstance(getApplication()).scheduleArticleNotification();
    }

    @Override
    public void writeTo(@NonNull Bundle bundle) {}

    @Override
    public void readFrom(@NonNull Bundle bundle) {}
}
