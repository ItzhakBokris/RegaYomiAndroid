package com.regayomi.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.regayomi.R;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class TimePreference extends DialogPreference {

    private int mTime;

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimePreference(Context context) {
        super(context);
        init(context);
    }

    public int getTime() {
        return mTime;
    }
    public void setTime(int time) {
        mTime = time;
        // Save to Shared Preferences
        persistInt(time);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.preference_time_picker;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setTime((Integer) defaultValue);
    }

    /**
     * Initialize the time preference dialog.
     */
    private void init(Context context) {

    }
}
