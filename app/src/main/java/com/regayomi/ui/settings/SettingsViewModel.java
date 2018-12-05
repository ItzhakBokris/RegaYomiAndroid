package com.regayomi.ui.settings;

import android.app.Application;
import android.os.Bundle;

import com.regayomi.ui.base.BaseViewModel;

import androidx.annotation.NonNull;

public class SettingsViewModel extends BaseViewModel {

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void writeTo(@NonNull Bundle bundle) {}

    @Override
    public void readFrom(@NonNull Bundle bundle) {}
}
