package com.regayomi.ui.article.content;

import com.regayomi.R;
import com.regayomi.data.model.ArticleState;
import com.regayomi.databinding.ActivityArticleContentBinding;
import com.regayomi.ui.article.ArticleViewModel;
import com.regayomi.ui.base.BaseActivity;

import androidx.annotation.NonNull;

public class ArticleContentActivity extends BaseActivity<ActivityArticleContentBinding, ArticleViewModel> {

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().updateArticles();
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_article_content; }

    @NonNull
    @Override
    protected Class<ArticleViewModel> getViewModelClass() { return ArticleViewModel.class; }

    @Override
    protected void onSetUp(@NonNull ActivityArticleContentBinding binding, @NonNull ArticleViewModel model) {
        model.getSelectedArticle().observe(this, article -> setTitle(article.topic));
        model.getSelectedArticleState().observe(this, state -> {
            if (!state.isViewed) {
                state.isViewed = true;
                model.setSelectedArticleState(state);
            }
            binding.setState(state);
            binding.setPresenter(new ArticlePresenter(state));
        });
    }

    public class ArticlePresenter {

        // A state-object associated to the article of the presenter.
        private ArticleState state;

        /**
         * Create new article-presenter with the specified article-state.
         */
        public ArticlePresenter(ArticleState state) {
            this.state = state;
        }

        /**
         * Called when the bookmark button clicked.
         */
        public void onBookmarkClick(boolean isBookmarked) {
            state.isBookmarked = isBookmarked;
            getViewModel().setSelectedArticleState(state);
            getViewModel().getSnackbarMessage().setValue(isBookmarked ? R.string.article_bookmarked : R.string.article_unbookmarked);
        }
    }
}