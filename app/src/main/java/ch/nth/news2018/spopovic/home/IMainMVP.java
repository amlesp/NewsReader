package ch.nth.news2018.spopovic.home;

import java.util.List;

import ch.nth.news2018.spopovic.data.models.Article;
import ch.nth.news2018.spopovic.utils.SortType;


public interface IMainMVP {

    interface View {

        void showError(String error);

        void updateRecyclerView(List<Article> articleList);

        void resetQuery();
    }

    interface Presenter {

        void loadData(SortType sortType, String query, int page);

        void newQuery();

    }
}
