package ch.nth.news2018.spopovic;


import android.content.Context;
import android.util.Log;

import java.util.List;

import ch.nth.news2018.spopovic.data.api.IApiEndpoints;
import ch.nth.news2018.spopovic.data.api.IApiResponse;
import ch.nth.news2018.spopovic.data.api.RetrofitClientInstance;
import ch.nth.news2018.spopovic.data.db.AppDatabase;
import ch.nth.news2018.spopovic.data.db.AppExecutors;
import ch.nth.news2018.spopovic.data.models.Article;
import ch.nth.news2018.spopovic.data.models.NewsResponse;
import ch.nth.news2018.spopovic.home.MainActivity;
import ch.nth.news2018.spopovic.utils.SortType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private final String TAG = Repository.class.getSimpleName();

    private Context context;
    private IApiEndpoints endpointInterface;

    public Repository(Context context) {
        this.context = context;
        this.endpointInterface = RetrofitClientInstance.getRetrofitInstance().create(IApiEndpoints.class);
    }

    public void getData(SortType sortType, String query, int page, final IApiResponse apiResponse) {
        switch (sortType) {
            case NORMAL:
                Call<NewsResponse> allArticles = endpointInterface.getAllNews(query, page);
                allArticles.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.body() != null && response.body().getArticles() != null) {
                            Log.d(TAG, "DATA RECIEVED" + String.valueOf(response.body().getArticles()));
                            apiResponse.onSuccess(response.body().getArticles());
                        } else {
                            Log.d(TAG, "API RESPONSE INCOMPLETE----->");
                            apiResponse.onFail("API ERROR");
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        apiResponse.onFail(t.getMessage().toString());
                    }
                });
                break;
            case TOP:
                Call<NewsResponse> topHeadlines = endpointInterface.getTopHeadlines(query, page);
                topHeadlines.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.body() != null && response.body().getArticles() != null) {
                            Log.d(TAG, "DATA RECIEVED" + String.valueOf(response.body().getArticles()));
                            apiResponse.onSuccess(response.body().getArticles());
                        } else {
                            Log.d(TAG, "API RESPONSE INCOMPLETE----->");
                            apiResponse.onFail("API ERROR");
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        apiResponse.onFail(t.getMessage().toString());
                    }
                });
                break;
            case FAVORITE:
                final AppExecutors appExecutors = AppExecutors.getInstance();
                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<Article> articleList = AppDatabase.getInstance(context).dataDao().getAllArticles();

                        appExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (articleList == null || articleList.size() == 0) {
                                    apiResponse.onFail(context.getString(R.string.favorites_list_empty));
                                } else {
                                    apiResponse.onSuccess(articleList);
                                }
                            }
                        });
                    }
                });
                break;
        }
    }
}
