package com.regayomi.ui.article.list;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.regayomi.data.model.Article;
import com.regayomi.databinding.FragmentArticleListItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    // List of articles handled by the adapter.
    public List<Article> articles;

    // Listener for article-click events.
    private ArticleClickListener clickListener;

    // Text that will be highlighted in parts of the articles.
    private String highlightedText;

    /**
     * Create article-list adapter with the specified articles.
     */
    public ArticleListAdapter(@NonNull List<Article> articles, @NonNull ArticleClickListener listener) {
        this.articles = articles;
        this.clickListener = listener;
        setHasStableIds(true);
    }

    /**
     * Updates the article-list adapter with the specified articles.
     */
    public void setArticles(@NonNull List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    /**
     * Set text that will be highlighted in parts of the articles.
     */
    public void highlightText(String text) {
        highlightedText = text;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentArticleListItemBinding binding = FragmentArticleListItemBinding.inflate(inflater, parent, false);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.binding.setArticle(articles.get(position));
        holder.binding.setHighlightedText(highlightedText);
    }

    @BindingAdapter({"highlightableText", "highlightedText"})
    public static void setText(TextView textView, String highlightableText, String highlightedText) {
        if (!TextUtils.isEmpty(highlightedText)) {
            int start = highlightableText.indexOf(highlightedText);
            if (start >= 0) {
                int end = start + highlightedText.length();
                SpannableString spannedText = new SpannableString(highlightableText);
                spannedText.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannedText.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannedText);
                return;
            }
        }
        textView.setText(highlightableText);
    }

    @Override
    public int getItemCount() { return articles.size(); }

    /**
     * An article view-holder used by the article-list adapter.
     */
    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final FragmentArticleListItemBinding binding;

        public ArticleViewHolder(FragmentArticleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(articles.get(getAdapterPosition()));
        }
    }

    /**
     * Listener for article-click events.
     */
    public interface ArticleClickListener {

        /**
         * Called when the specified article clicked.
         */
        void onClick(Article article);
    }
}
