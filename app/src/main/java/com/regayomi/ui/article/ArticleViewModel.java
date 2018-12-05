package com.regayomi.ui.article;

import android.app.Application;
import android.os.Bundle;

import com.regayomi.data.model.Article;
import com.regayomi.data.model.ArticleSection;
import com.regayomi.data.model.ArticleState;
import com.regayomi.data.repositories.ArticleRepository;
import com.regayomi.ui.base.BaseViewModel;
import com.regayomi.utils.SingleLiveEvent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ArticleViewModel extends BaseViewModel {

    // An intent key used for read/write the selected article id from/to a bundle.
    public static final String SELECTED_ARTICLE_KEY = "selected_article_key";

    // The singleton instance of the article-repository.
    private final ArticleRepository articleRepository;

    // Live-data of the current loaded articles.
    private final LiveData<List<Article>> articles;

    // Live-data of the current search-query.
    private final MutableLiveData<String> searchQuery;

    // Single-live-event of the current selected article.
    private final MediatorLiveData<Article> selectedArticle;

    // Live-data of the article-sections contained in the current selected article.
    private final LiveData<List<ArticleSection>> selectedArticleSections;

    // Live-data of the article-state associated to the current selected article.
    private final LiveData<ArticleState> selectedArticleState;

    // Single-live-event of message-id that should be displayed in a snackbar.
    private final SingleLiveEvent<Integer> snackbarMessage;

    /**
     * Create new view-model of articles.
     */
    public ArticleViewModel(@NonNull Application application) {
        super(application);

        articleRepository = ArticleRepository.getInstance(application);
        searchQuery = new MutableLiveData<>();
        searchQuery.setValue(null);
        articles = Transformations.switchMap(searchQuery, articleRepository::getArticles);
        selectedArticle = new MediatorLiveData<>();
        selectedArticleSections = Transformations.switchMap(selectedArticle, article -> articleRepository.getArticleSections(article.key));
        selectedArticleState = Transformations.switchMap(selectedArticle, article -> {
            LiveData<ArticleState> stateLiveData = articleRepository.getArticleState(article.key);
            return Transformations.map(stateLiveData, state -> state != null ? state : new ArticleState(article.key));
        });
        snackbarMessage = new SingleLiveEvent<>();
    }

    /**
     * Gets live-data of the current loaded articles.
     */
    public LiveData<List<Article>> getArticles() {
        return articles;
    }

    /**
     * Gets live-data of the current search-query.
     */
    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    /**
     * Gets live-data of the current selected article.
     */
    public LiveData<Article> getSelectedArticle() {
        return selectedArticle;
    }

    /**
     * Gets live-data of the article-sections contained in the current selected article.
     */
    public LiveData<List<ArticleSection>> getSelectedArticleSections() {
        return selectedArticleSections;
    }

    /**
     * Gets live-data of the article-state associated to the current selected article.
     */
    public LiveData<ArticleState> getSelectedArticleState() {
        return selectedArticleState;
    }

    /**
     * Gets single-live-event of message-id that should be displayed in a snackbar.
     */
    public SingleLiveEvent<Integer> getSnackbarMessage() {
        return snackbarMessage;
    }

    /**
     * Sets new search-query of articles.
     */
    public void setSearchQuery(String searchQuery) {
        this.searchQuery.setValue(searchQuery);
    }

    /**
     * Updates the current selected article.
     */
    public void selectArticle(Article article) {
        selectedArticle.setValue(article);
    }

    /**
     * Sets the state of the current selected article.
     */
    public void setSelectedArticleState(ArticleState state) {
        articleRepository.setArticleState(state);
    }

    /**
     * Updates the articles with new data from the server.
     */
    public void updateArticles() {
        articleRepository.syncData();
    }

    @Override
    public void writeTo(@NonNull Bundle bundle) {
        if (selectedArticle.getValue() != null) {
            bundle.putString(SELECTED_ARTICLE_KEY, selectedArticle.getValue().key);
        }
    }

    @Override
    public void readFrom(@NonNull Bundle bundle) {
        if (selectedArticle.getValue() == null) {
            String articleKey = bundle.getString(SELECTED_ARTICLE_KEY);
            selectArticle(articleKey);
        }
    }

    /**
     * Gets the specified article and update the current selected article with it.
     */
    private void selectArticle(String articleKey) {
        selectedArticle.addSource(articleRepository.getArticle(articleKey), selectedArticle::setValue);
    }
}
