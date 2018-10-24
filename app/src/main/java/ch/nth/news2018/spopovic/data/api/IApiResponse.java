package ch.nth.news2018.spopovic.data.api;


import java.util.List;

import ch.nth.news2018.spopovic.data.models.Article;

public interface IApiResponse {

    void onSuccess(List<Article> data);

    void onFail(String s);
}
