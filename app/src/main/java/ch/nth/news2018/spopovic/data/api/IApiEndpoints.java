package ch.nth.news2018.spopovic.data.api;

import ch.nth.news2018.spopovic.BuildConfig;
import ch.nth.news2018.spopovic.data.models.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IApiEndpoints {

    @GET("/v2/everything?language=en&sortBy=publishedAt&apiKey=" + BuildConfig.ApiKey)
    Call<NewsResponse> getAllNews(@Query("q") String q, @Query("page") int page);

    @GET("/v2/top-headlines?apiKey=" + BuildConfig.ApiKey)
    Call<NewsResponse> getTopHeadlines(@Query("q") String q, @Query("page") int page);
}
