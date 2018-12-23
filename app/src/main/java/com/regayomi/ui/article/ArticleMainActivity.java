package com.regayomi.ui.article;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.regayomi.R;
import com.regayomi.databinding.ActivityArticleMainBinding;
import com.regayomi.ui.article.bookmarks.BookmarkedArticleMainActivity;
import com.regayomi.ui.article.content.ArticleContentActivity;
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

            case R.id.myBookmarks:
                goToBookmarks();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().updateArticles();
    }

    @Override
    protected void onSetUp(@NonNull ActivityArticleMainBinding binding, @NonNull ArticleViewModel model) {
        model.getArticles().observe(this, articles -> binding.setNoArticles(articles.isEmpty()));
        model.getSelectedArticle().observe(this, article -> {
            if (article != null) {
                Intent intent = new Intent(this, ArticleContentActivity.class);
                intent.putExtra(ArticleViewModel.SELECTED_ARTICLE_KEY, article.key);
                startActivity(intent);
            } else {
                model.getSnackbarMessage().setValue(R.string.general_error_message);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_main;
    }

    @NonNull
    @Override
    protected Class<ArticleViewModel> getViewModelClass() {
        return ArticleViewModel.class;
    }

    /**
     * Initialize the search-view that its expand-button located in the specified menu.
     */
    private void initSearchView(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                getViewModel().setSearchQuery(searchQuery);
                return false;
            }
        });
    }

    /**
     * Redirects to the settings screen.
     */
    private void goToSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * Redirects to the bookmarked-articles screen.
     */
    private void goToBookmarks() {
        startActivity(new Intent(this, BookmarkedArticleMainActivity.class));
    }
}
