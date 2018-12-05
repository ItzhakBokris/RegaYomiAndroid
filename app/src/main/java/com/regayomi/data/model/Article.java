package com.regayomi.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("title"), @Index("date"), @Index("lastModifiedDate")})
public class Article {

    // The unique key of the article.
    @PrimaryKey
    @NonNull
    public String key;

    // The title of the article.
    public String title;

    // The topic of the article.
    public String topic;

    // The published date of the article.
    public long date;

    // The last time the article was modified.
    public long lastModifiedDate;

    // The published hebrew-data of this article.
    public String hebrewDate;

    // Indicates if the article is currently active and can be displayed.
    public boolean isActive;

    /**
     * Default constructor required by firebase.
     */
    public Article() {}
}
