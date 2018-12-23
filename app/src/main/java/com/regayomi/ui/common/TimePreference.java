package com.regayomi.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import com.regayomi.R;
import com.regayomi.utils.DateUtils;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class TimePreference extends DialogPreference {

    // The current time value (total minutes after midnight) of the preference.
    private int time;

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePreference(Context context) {
        super(context);
    }

    /**
     * Gets the current time value (total minutes after midnight) of the preference.
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets new time value (total minutes after midnight) to the preference.
     */
    public void setTime(int time) {
        this.time = time;
        Date date = DateUtils.createTimeCalendar(time / 60, time % 60, 0).getTime();
        setSummary(DateFormat.getTimeFormat(getContext()).format(date.getTime()));
        persistInt(time);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.preference_time_picker;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray typedArray, int index) {
        return typedArray.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        int time = getPersistedInt(defaultValue != null ? (int) defaultValue : 0);
        setTime(time);
    }
}
