package com.regayomi.ui.article;

import com.regayomi.R;
import com.regayomi.databinding.FragmentArticleListBinding;
import com.regayomi.ui.base.BaseFragment;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;

public class ArticleListFragment extends BaseFragment<FragmentArticleListBinding, ArticleViewModel> {

    @Override
    protected int getLayoutId() { return R.layout.fragment_article_list; }

    @NonNull
    @Override
    protected Class<ArticleViewModel> getViewModelClass() { return ArticleViewModel.class; }

    @Override
    protected void onSetUp(@NonNull FragmentArticleListBinding binding, @NonNull ArticleViewModel model) {
        initList(binding, model);
    }

    /**
     * Initialize the fragment's article-list and bind it to the fragment's view-model.
     */
    private void initList(@NonNull FragmentArticleListBinding binding, @NonNull ArticleViewModel model) {
        binding.list.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.list.setHasFixedSize(true);
        ArticleListAdapter adapter = new ArticleListAdapter(Collections.emptyList(), model::selectArticle);
        binding.list.setAdapter(adapter);
        model.getArticles().observe(this, articles -> {
            adapter.setArticles(articles);
            binding.setNoArticles(articles.isEmpty());
        });
        model.getSearchQuery().observe(this, adapter::highlightText);
    }
}
