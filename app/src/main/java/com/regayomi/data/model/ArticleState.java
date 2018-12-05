package com.regayomi.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    foreignKeys = @ForeignKey(entity = Article.class, parentColumns = "key", childColumns = "articleKey", onDelete = ForeignKey.CASCADE),
    indices = {@Index("isBookmarked")}
)
public class ArticleState {

    // The key of the associated article.
    @NonNull
    @PrimaryKey
    public String articleKey;

    // Indicates if the article is bookmarked or not.
    public boolean isBookmarked;

    // Indicates if the article is already viewed.
    public boolean isViewed;

    /**
     * Create new article-state to the specified article.
     */
    public ArticleState(String articleKey) {
        this.articleKey = articleKey;
    }
}
