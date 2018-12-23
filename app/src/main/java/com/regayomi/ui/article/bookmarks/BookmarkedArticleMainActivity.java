package com.regayomi.ui.article.bookmarks;

import android.view.Menu;

import com.regayomi.R;
import com.regayomi.databinding.ActivityArticleMainBinding;
import com.regayomi.ui.article.ArticleMainActivity;
import com.regayomi.ui.article.ArticleViewModel;

import androidx.annotation.NonNull;

public class BookmarkedArticleMainActivity extends ArticleMainActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onSetUp(@NonNull ActivityArticleMainBinding binding, @NonNull ArticleViewModel model) {
        super.onSetUp(binding, model);
        model.setFilterType(ArticleViewModel.FilterType.Bookmarks);
        binding.setNoArticlesMessages(getString(R.string.no_bookmarked_articles_found));
    }
}
