package com.regayomi.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.regayomi.R;
import com.regayomi.ui.base.App;
import com.regayomi.utils.DateUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.annotation.WorkerThread;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class NotificationsManager {

    static final String ARTICLE_NOTIFICATIONS_CHANNEL_ID = App.TAG + "_articles_channel";

    private static final String ARTICLE_NOTIFICATION_WORKER_TAG = "article_notification_worker_tag";

    // A singleton instance of the jobs-manager class.
    private static NotificationsManager notificationsManager;

    // Instance of the default shared-preference.
    private SharedPreferences sharedPreferences;

    // Instance of resources of the application context.
    private Resources resources;

    /**
     * Gets the singleton instance of the jobs-manager class.
     */
    public static synchronized NotificationsManager getInstance(Context context) {
        if (notificationsManager == null) {
            notificationsManager = new NotificationsManager(context.getApplicationContext());
        }
        return notificationsManager;
    }

    /**
     * Schedules new article notification according to the app state and the user settings.
     */
    @WorkerThread
    public void scheduleArticleNotification() {
        OneTimeWorkRequest worker = new OneTimeWorkRequest.Builder(ArticleNotificationWorker.class)
                .addTag(ARTICLE_NOTIFICATION_WORKER_TAG)
                .setInitialDelay(calculateArticleNotificationDelay(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance().enqueueUniqueWork(ARTICLE_NOTIFICATION_WORKER_TAG, ExistingWorkPolicy.REPLACE, worker);
    }

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private NotificationsManager(Context context) {
        createArticleNotificationsChannel(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
    }

    /**
     * Creates channel which all the article notifications will assigned to it.
     */
    private void createArticleNotificationsChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.article_notifications_channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ARTICLE_NOTIFICATIONS_CHANNEL_ID, name, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Calculates the initial delay before the article-notification should triggered.
     */
    private long calculateArticleNotificationDelay() {
        String notificationTimeKey = resources.getString(R.string.notification_time_key);
        String saturdayNotificationsKey = resources.getString(R.string.saturday_notifications_key);
        int notificationDefaultTime = resources.getInteger(R.integer.notification_default_time);
        int totalMinutes = sharedPreferences.getInt(notificationTimeKey, notificationDefaultTime);
        boolean inSaturday = sharedPreferences.getBoolean(saturdayNotificationsKey, false);

        Calendar current = Calendar.getInstance();
        Calendar nextTime = DateUtils.createTimeCalendar(totalMinutes / 60, totalMinutes % 60, 0);
        if (nextTime.before(current)) {
            int daysOffset = nextTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && !inSaturday ? 2 : 1;
            nextTime.add(Calendar.DAY_OF_WEEK, daysOffset);
        }
        return nextTime.getTimeInMillis() - current.getTimeInMillis();
    }
}
