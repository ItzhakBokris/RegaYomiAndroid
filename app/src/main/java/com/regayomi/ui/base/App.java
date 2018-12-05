package com.regayomi.ui.base;

import android.content.Context;

import com.regayomi.utils.ConfigUtils;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    // A global tag used for logs.
    public final static String TAG = "rega_yomi";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(ConfigUtils.updateBaseContextLocale(base));
    }
}
