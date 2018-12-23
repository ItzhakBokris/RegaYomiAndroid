package com.regayomi.ui.common;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import com.regayomi.R;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

public class TimePreferenceDialog extends PreferenceDialogFragmentCompat {

    // The time-preference that requested this dialog.
    private TimePreference timePreference;

    // The time-picker widget contained in the dialog.
    private TimePicker timePicker;

    /**
     * Creates new instance of this fragment associated to the specified preference.
     */
    public static TimePreferenceDialog newInstance(String preferenceKey) {
        final TimePreferenceDialog fragment = new TimePreferenceDialog();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, preferenceKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        DialogPreference preference = getPreference();
        if (preference instanceof TimePreference) {
            timePreference = (TimePreference) preference;
            timePicker = view.findViewById(R.id.timePicker);

            int totalMinutes = timePreference.getTime();
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;
            boolean is24hour = DateFormat.is24HourFormat(getContext());
            timePicker.setCurrentHour(hours);
            timePicker.setCurrentMinute(minutes);
            timePicker.setIs24HourView(is24hour);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult && timePicker != null && timePreference != null) {
            int hours = timePicker.getCurrentHour();
            int minutes = timePicker.getCurrentMinute();
            int totalMinutes = (hours * 60) + minutes;
            if (timePreference.callChangeListener(totalMinutes)) {
                timePreference.setTime(totalMinutes);
            }
        }
    }
}
