package com.regayomi.data.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.regayomi.data.database.AppDatabase;
import com.regayomi.data.database.ArticleDao;
import com.regayomi.data.model.Article;
import com.regayomi.data.model.ArticleSection;
import com.regayomi.data.model.ArticleState;
import com.regayomi.ui.base.App;
import com.regayomi.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class ArticleRepository {

    // A singleton instance of the article-repository.
    private static ArticleRepository instance;

    // An instance of the articles DAO.
    private ArticleDao articleDao;

    /**
     * Gets the singleton instance of the article-repository.
     */
    public synchronized static ArticleRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ArticleRepository(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Gets all the articles matched to the specified search-query, sorted by their date.
     */
    @NonNull
    public LiveData<List<Article>> getArticles(@Nullable String searchQuery) {
        return TextUtils.isEmpty(searchQuery) ? articleDao.loadArticles() : articleDao.loadArticles(searchQuery);
    }

    /**
     * Gets all the bookmarked articles, sorted by their date.
     */
    @NonNull
    public LiveData<List<Article>> getBookmarkedArticles() {
        return articleDao.loadBookmarkedArticles();
    }

    /**
     * Gets the specified article.
     */
    @NonNull
    public LiveData<Article> getArticle(String articleKey) {
        return articleDao.loadArticle(articleKey);
    }

    /**
     * Gets all the sections contained in the specified article.
     */
    @NonNull
    public LiveData<List<ArticleSection>> getArticleSections(String articleKey) {
        return articleDao.loadSections(articleKey);
    }

    /**
     * Gets the state-object of the specified article.
     */
    @NonNull
    public LiveData<ArticleState> getArticleState(String articleKey) {
        return articleDao.loadState(articleKey);
    }

    /**
     * Gets synchronously (doesn't return live-data) the state-object of the specified article.
     */
    @Nullable
    public ArticleState getArticleStateSynchronously(String articleKey) {
        return articleDao.loadStateSynchronously(articleKey);
    }

    /**
     * Gets the article with the specified date.
     */
    @Nullable
    public Article getTodayArticle() {
        Calendar from = DateUtils.createCalendarWithoutTime();
        Calendar to = DateUtils.createCalendarWithoutTime();
        to.add(Calendar.DAY_OF_WEEK, 1);
        List<Article> articles = articleDao.loadArticlesBetweenDates(from.getTimeInMillis(), to.getTimeInMillis());
        return articles.isEmpty() ? null : articles.get(0);
    }

    /**
     * Sets article-state object to the associated article.
     */
    public void setArticleState(ArticleState state) {
        AsyncTask.execute(() -> articleDao.insertState(state));
    }

    /**
     * Synchronize the local-database's article-data with data from the server database.
     */
    public void syncData() {
        AsyncTask.execute(() -> {
            Article article = articleDao.loadLastModifiedArticle();
            Query firebaseArticleQuery = FirebaseDatabase.getInstance().getReference("articles");
            if (article != null) {
                firebaseArticleQuery = firebaseArticleQuery.orderByChild("lastModifiedDate").startAt(article.lastModifiedDate + 1);
            }
            firebaseArticleQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    updateData(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(App.TAG, "Can't load articles from firebase: ", databaseError.toException());
                }
            });
        });
    }

    /**
     * Update the local-database's article-data with data from the specified snapshot.
     */
    private void updateData(@NonNull DataSnapshot dataSnapshot) {
        AsyncTask.execute(() -> {
            ArticlesData articlesData = fetchArticlesDataFromSnapshot(dataSnapshot);
            if (articlesData != null) {
                articleDao.insertArticlesAndSections(articlesData.updatedArticles, articlesData.updatedSections);
            }
        });
    }

    /**
     * Fetch articles and their sections from the specified firebase data-snapshot.
     *
     * @return container of new/modified/deleted articles and their sections, null if empty or failed to fetch.
     */
    @Nullable
    private ArticlesData fetchArticlesDataFromSnapshot(@NonNull DataSnapshot dataSnapshot) {
        if (!dataSnapshot.exists()) {
            return null;
        }
        try {
            ArticlesData data = new ArticlesData();
            for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                Article article = articleSnapshot.getValue(Article.class);
                article.key = articleSnapshot.getKey();
                data.updatedArticles.add(article);
                for (DataSnapshot sectionSnapshot : articleSnapshot.child("sections").getChildren()) {
                    ArticleSection section = sectionSnapshot.getValue(ArticleSection.class);
                    section.articleKey = article.key;
                    section.order = data.updatedSections.size();
                    data.updatedSections.add(section);
                }
            }
            return data;
        }
        catch (Throwable throwable) {
            Log.e(App.TAG, "Failed to fetch articles data from firebase data-snapshot", throwable);
            return null;
        }
    }

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private ArticleRepository(Context context) {
        articleDao = AppDatabase.getInstance(context).articleDao();
    }

    /**
     * Contains lists of new/modified/deleted articles and their sections.
     */
    private class ArticlesData {

        // List of new/modified articles.
        public List<Article> updatedArticles = new ArrayList<>();

        // List of article-sections that their article is new or modified.
        public List<ArticleSection> updatedSections = new ArrayList<>();
    }
}
