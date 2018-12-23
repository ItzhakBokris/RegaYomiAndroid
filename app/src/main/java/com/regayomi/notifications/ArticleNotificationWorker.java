package com.regayomi.notifications;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.regayomi.R;
import com.regayomi.data.model.Article;
import com.regayomi.data.model.ArticleState;
import com.regayomi.data.repositories.ArticleRepository;
import com.regayomi.ui.article.content.ArticleContentActivity;
import com.regayomi.ui.article.ArticleViewModel;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ArticleNotificationWorker extends Worker {

    // The unique ID of the article notifications.
    private static final int NOTIFICATION_ID = ArticleNotificationWorker.class.getSimpleName().hashCode();

    // Private request code for the sender.
    private static final int INTENT_REQUEST_CODE = NOTIFICATION_ID;

    public ArticleNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ArticleRepository articleRepository = ArticleRepository.getInstance(getApplicationContext());
        Article todayArticle = articleRepository.getTodayArticle();
        if (todayArticle != null) {
            ArticleState state = articleRepository.getArticleStateSynchronously(todayArticle.key);
            if (state == null || !state.isViewed) {
                postNotification(todayArticle);
            }
        }
        NotificationsManager.getInstance(getApplicationContext()).scheduleArticleNotification();
        return Result.SUCCESS;
    }

    /**
     * Post an article notification of the specified article.
     */
    private void postNotification(Article article) {

        // Creates the intent that will fire when the user taps the notification.
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ArticleContentActivity.class);
        intent.putExtra(ArticleViewModel.SELECTED_ARTICLE_KEY, article.key);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(INTENT_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification appearance and behavior.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationsManager.ARTICLE_NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.star_big_on)
            .setContentTitle(article.topic)
            .setContentText(article.title)
            .setColor(context.getResources().getColor(R.color.colorPrimary))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        // Post the notification.
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }
}
