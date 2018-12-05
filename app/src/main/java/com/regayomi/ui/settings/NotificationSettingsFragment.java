package com.regayomi.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.regayomi.R;
import com.regayomi.ui.base.BasePreferenceFragment;
import com.regayomi.ui.common.TimePreference;
import com.regayomi.ui.common.TimePreferenceDialog;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;

public class NotificationSettingsFragment extends BasePreferenceFragment<SettingsViewModel> {

    @Override
    protected int getPreferenceResId() {
        return R.xml.preference_notifications;
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimePreferenceDialog
                .newInstance(preference.getKey());
        }

        // If it was one of our cutom Preferences, show its dialog
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(),
                "android.support.v7.preference" +
                    ".PreferenceFragment.DIALOG");
        }
        // Could not be handled here. Try with the super method.
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
