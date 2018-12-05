package com.regayomi.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
    foreignKeys = @ForeignKey(entity = Article.class, parentColumns = "key", childColumns = "articleKey", onDelete = ForeignKey.CASCADE),
    primaryKeys = {"articleKey", "order"}
)
public class ArticleSection {

    // The key of the container article.
    @NonNull
    public String articleKey;

    // The header of the section.
    public String header;

    // The content text of the section.
    public String content;

    // The source of the section text.
    public String source;

    // The order of the section relative to the other sections in the article.
    public int order;

    /**
     * Default constructor required by firebase.
     */
    public ArticleSection() {}
}
