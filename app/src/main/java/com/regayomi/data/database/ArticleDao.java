package com.regayomi.data.database;

import com.regayomi.data.model.Article;
import com.regayomi.data.model.ArticleSection;
import com.regayomi.data.model.ArticleState;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class ArticleDao {

    /**
     * Loads all the articles sorted by their date.
     */
    @Query("SELECT * FROM article WHERE isActive == 1 ORDER BY date DESC")
    public abstract LiveData<List<Article>> loadArticles();

    /**
     * Loads all the articles matched to the specified search-query, sorted by their date.
     */
    @Query("SELECT * FROM article WHERE isActive == 1 AND " +
        "(topic LIKE '%' || :searchQuery || '%'  OR title LIKE '%' || :searchQuery || '%') ORDER BY date DESC")
    public abstract LiveData<List<Article>> loadArticles(@NonNull String searchQuery);

    /**
     * Loads the specified article.
     */
    @Query("SELECT * FROM article WHERE `key` == :articleKey LIMIT 1")
    public abstract LiveData<Article> loadArticle(@NonNull String articleKey);

    /**
     * Loads all the sections contained in the specified article.
     */
    @Query("SELECT * FROM articleSection WHERE articleKey == :articleKey")
    public abstract LiveData<List<ArticleSection>> loadSections(@NonNull String articleKey);

    /**
     * Loads the state-object of the specified article.
     */
    @Query("SELECT * FROM articlestate WHERE articleKey == :articleKey LIMIT 1")
    public abstract LiveData<ArticleState> loadState(@NonNull String articleKey);

    /**
     * Loads synchronously (doesn't return live-data) the state-object of the specified article.
     */
    @Query("SELECT * FROM articlestate WHERE articleKey == :articleKey LIMIT 1")
    public abstract ArticleState loadStateSynchronously(@NonNull String articleKey);

    /**
     * Loads all the articles that their dates are between the specified from-to dates.
     */
    @Query("SELECT * FROM article WHERE date BETWEEN :from AND :to")
    public abstract List<Article> loadArticlesBetweenDates(long from, long to);

    /**
     * Loads the article that was last modified.
     */
    @Query("SELECT * FROM article ORDER BY lastModifiedDate DESC LIMIT 1")
    public abstract Article loadLastModifiedArticle();

    /**
     * Inserts the specified sections to the article-section room-table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSections(@NonNull List<ArticleSection> sections);

    /**
     * Inserts the specified articles to the article room-table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertArticles(@NonNull List<Article> articles);

    /**
     * Inserts the specified state to the article-state room-table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertState(@NonNull ArticleState state);

    /**
     * Inserts the specified articles and their sections to the article/section room-tables.
     */
    @Transaction
    public void insertArticlesAndSections(@NonNull List<Article> articles, @NonNull List<ArticleSection> sections) {
        insertArticles(articles);
        insertSections(sections);
    }
}