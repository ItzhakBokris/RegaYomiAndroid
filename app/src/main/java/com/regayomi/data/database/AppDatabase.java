package com.regayomi.data.database;

import android.content.Context;

import com.regayomi.data.model.Article;
import com.regayomi.data.model.ArticleSection;
import com.regayomi.data.model.ArticleState;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Article.class, ArticleSection.class, ArticleState.class}, version = AppDatabase.VERSION)
public abstract class AppDatabase extends RoomDatabase {

    // The current version of the database.
    public static final int VERSION = 1;

    // The name of the database.
    public static final String NAME = "rega_yomi_db";

    // A singleton instance of the database class.
    private static AppDatabase appDatabase;

    /**
     * Gets the singleton instance of the database class.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME).build();
        }
        return appDatabase;
    }

    /**
     * Gets instance of articles DAO.
     */
    public abstract ArticleDao articleDao();
}