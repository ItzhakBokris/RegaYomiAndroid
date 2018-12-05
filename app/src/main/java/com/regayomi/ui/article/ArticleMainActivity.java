package com.regayomi.ui.article;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;
import com.regayomi.R;
import com.regayomi.databinding.ActivityArticleMainBinding;
import com.regayomi.notifications.NotificationsManager;
import com.regayomi.ui.base.BaseActivity;
import com.regayomi.ui.settings.SettingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

public class ArticleMainActivity extends BaseActivity<ActivityArticleMainBinding, ArticleViewModel> {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        initSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_article_main; }

    @NonNull
    @Override
    protected Class<ArticleViewModel> getViewModelClass() { return ArticleViewModel.class; }

    @Override
    protected void onSetUp(@NonNull ActivityArticleMainBinding binding, @NonNull ArticleViewModel model) {
        listenToSelectedArticle(model);
        initSnackbar(binding, model);

        goToSettings();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().updateArticles();
    }

    /**
     * Initialize the search-view that its expand-button located in the specified menu.
     */
    private void initSearchView(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) { return false; }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                getViewModel().setSearchQuery(searchQuery);
                return false;
            }
        });
    }

    /**
     * Listens to selected-article events and display the article-content if selected.
     */
    private void listenToSelectedArticle(@NonNull ArticleViewModel model) {
        model.getSelectedArticle().observe(this, article -> {
            if (article != null) {
                Intent intent = new Intent(this, ArticleContentActivity.class);
                intent.putExtra(ArticleViewModel.SELECTED_ARTICLE_KEY, article.key);
                startActivity(intent);
            }
            else {
                model.getSnackbarMessage().setValue(R.string.general_error_message);
            }
        });
    }

    /**
     * Listens to Snackbar messages that could displayed at some scenarios.
     */
    private void initSnackbar(@NonNull ActivityArticleMainBinding binding, @NonNull ArticleViewModel model) {
        model.getSnackbarMessage().observe(this,
            messageId -> Snackbar.make(binding.container, messageId, Snackbar.LENGTH_LONG).show());
    }

    /**
     * Redirects to the settings screen.
     */
    private void goToSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
