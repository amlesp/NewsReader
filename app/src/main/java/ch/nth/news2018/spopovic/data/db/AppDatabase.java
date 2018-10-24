package ch.nth.news2018.spopovic.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import ch.nth.news2018.spopovic.data.models.Article;

@Database(entities = Article.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "news";

    private static final Object LOCK = new Object();
    private static volatile AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME)
                           // .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract DataDao dataDao();
}
