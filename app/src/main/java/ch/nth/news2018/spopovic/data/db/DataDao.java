package ch.nth.news2018.spopovic.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ch.nth.news2018.spopovic.data.models.Article;

@Dao
public interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(Article article);

    @Delete
    void deleteArticle(Article article);

    @Query("SELECT * FROM article")
    List<Article> getAllArticles();

    @Query("SELECT * FROM article WHERE title =:title")
    Article getArticleByTytle(String title);
}
