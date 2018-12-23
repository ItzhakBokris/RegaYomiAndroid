package com.regayomi.ui.article.content;

import com.regayomi.R;
import com.regayomi.databinding.FragmentArticleContentBinding;
import com.regayomi.ui.article.ArticleViewModel;
import com.regayomi.ui.base.BaseFragment;

import java.util.Collections;

import androidx.annotation.NonNull;

public class ArticleContentFragment extends BaseFragment<FragmentArticleContentBinding, ArticleViewModel> {

    @Override
    protected int getLayoutId() { return R.layout.fragment_article_content; }

    @NonNull
    @Override
    protected Class<ArticleViewModel> getViewModelClass() { return ArticleViewModel.class; }

    @Override
    protected void onSetUp(@NonNull FragmentArticleContentBinding binding, @NonNull ArticleViewModel model) {
        model.getSelectedArticle().observe(this, binding::setArticle);
        initSectionList(binding, model);
    }

    /**
     * Initialize the fragment's article-section-list and bind it to the fragment's view-model.
     */
    private void initSectionList(@NonNull FragmentArticleContentBinding binding, @NonNull ArticleViewModel model) {
        binding.sectionList.setNestedScrollingEnabled(false);
        ArticleSectionListAdapter adapter = new ArticleSectionListAdapter(Collections.emptyList());
        binding.sectionList.setAdapter(adapter);
        model.getSelectedArticleSections().observe(this, adapter::setArticleSections);
    }
}
