package com.regayomi.ui.settings;

import com.regayomi.R;
import com.regayomi.databinding.ActivitySettingsBinding;
import com.regayomi.ui.base.BaseActivity;

import androidx.annotation.NonNull;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding, SettingsViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @NonNull
    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }

    @Override
    protected void onSetUp(@NonNull ActivitySettingsBinding binding, @NonNull SettingsViewModel model) {}
}
