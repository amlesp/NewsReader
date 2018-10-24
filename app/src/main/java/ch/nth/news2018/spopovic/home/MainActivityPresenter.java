package ch.nth.news2018.spopovic.home;


import android.content.Context;
import android.util.Log;

import java.util.List;

import ch.nth.news2018.spopovic.R;
import ch.nth.news2018.spopovic.Repository;
import ch.nth.news2018.spopovic.data.api.IApiResponse;
import ch.nth.news2018.spopovic.data.models.Article;
import ch.nth.news2018.spopovic.utils.NetworkUtils;
import ch.nth.news2018.spopovic.utils.SortType;

public class MainActivityPresenter implements IMainMVP.Presenter {

    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private IMainMVP.View mView;
    private List<Article> mData;
    private Repository mRepository;
    private Context context;

    public MainActivityPresenter(Context context, List<Article> mData) {
        this.mView = (IMainMVP.View) context;
        this.context = context;
        this.mData = mData;
        this.mRepository = new Repository(context);
    }

    @Override
    public void loadData(SortType sortType, final String query, int page) {

        if (!NetworkUtils.isOnline(context)){
            mView.showError(context.getString(R.string.no_internet));
            return;
        }

        /*
         * optimize api calls with removing endless listener if data set is less then 20 which means that we are
         * in the end and no more data to call
         * **/
        mRepository.getData(sortType, query, page, new IApiResponse() {
            @Override
            public void onSuccess(List<Article> data) {
                Log.d(TAG, "recieved data Size:" + data.size());
                Log.d(TAG, "recieved data Size:" + data.toString());
                if (data.size() == 0 && mData.size() == 0) {
                    mView.showError(context.getString(R.string.no_result)+ " " + query);
                } else {
                    mData.addAll(data);
                    mView.updateRecyclerView(mData);
                }
            }

            @Override
            public void onFail(String s) {
                mView.showError(s);
            }
        });
    }

    @Override
    public void newQuery() {
        mView.resetQuery();
    }
}
