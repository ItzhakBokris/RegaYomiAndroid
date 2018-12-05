package com.regayomi.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import androidx.annotation.NonNull;

public final class ConfigUtils {

    /**
     * Update the locale of the app according to the user's configurations.
     */
    public static Context updateBaseContextLocale(@NonNull Context context) {
        String language = "he";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfigUtils() {}
}
