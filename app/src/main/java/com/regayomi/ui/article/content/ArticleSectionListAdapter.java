package com.regayomi.ui.article.content;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.regayomi.data.model.ArticleSection;
import com.regayomi.databinding.FragmentArticleSectionListItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleSectionListAdapter extends RecyclerView.Adapter<ArticleSectionListAdapter.ArticleSectionViewHolder>  {

    // List of article-sections handled by the adapter.
    private List<ArticleSection> sections;

    /**
     * Create article-section-list adapter with the specified article-sections.
     */
    public ArticleSectionListAdapter(@NonNull List<ArticleSection> sections) {
        this.sections = sections;
    }

    /**
     * Updates the article-section-list adapter with the specified article-sections.
     */
    public void setArticleSections(@NonNull List<ArticleSection> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public ArticleSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentArticleSectionListItemBinding binding = FragmentArticleSectionListItemBinding.inflate(inflater, parent, false);
        return new ArticleSectionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleSectionViewHolder holder, int position) {
        holder.binding.setSection(sections.get(position));
    }

    @Override
    public int getItemCount() { return sections.size(); }

    /**
     * An article-section view-holder used by the article-section-list adapter.
     */
    public class ArticleSectionViewHolder extends RecyclerView.ViewHolder {

        private final FragmentArticleSectionListItemBinding binding;

        public ArticleSectionViewHolder(FragmentArticleSectionListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
