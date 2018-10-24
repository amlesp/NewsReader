package ch.nth.news2018.spopovic.home;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.nth.news2018.spopovic.R;
import ch.nth.news2018.spopovic.data.models.Article;
import ch.nth.news2018.spopovic.newsdetail.NewsDetailActivity;
import ch.nth.news2018.spopovic.utils.EndlessRecyclerViewScrollListener;
import ch.nth.news2018.spopovic.utils.SortType;

public class MainActivity extends AppCompatActivity implements NewsAdapter.IOnItemClick, IMainMVP.View, SearchView.OnQueryTextListener {

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_news)
    RecyclerView mRecViewNews;
    @BindView(R.id.bottom_nav)
    BottomNavigationView mBottomNavigation;

    private NewsAdapter mAdapter;
    private IMainMVP.Presenter mPresenter;
    private List<Article> mData = new ArrayList<>();
    private String mQuery = "android";
    private SortType sortType = SortType.NORMAL;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener mEndlessScrollListener;
    private Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();
        if (sortType == SortType.FAVORITE && mPresenter != null) {
            mPresenter.newQuery();
            mPresenter.loadData(sortType, mQuery, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        initUI();
    }

    private void initUI() {
        mPresenter = new MainActivityPresenter(this, mData);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecViewNews.setLayoutManager(layoutManager);
        mAdapter = new NewsAdapter(this, mData);
        mRecViewNews.setAdapter(mAdapter);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_all:
                        sortType = SortType.NORMAL;
                        break;
                    case R.id.navigation_top:
                        sortType = SortType.TOP;
                        break;
                    case R.id.navigation_favorite:
                        sortType = SortType.FAVORITE;
                        break;
                }

                mPresenter.newQuery();
                mPresenter.loadData(sortType, mQuery, 1);
                return false;
            }
        });

        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPresenter.loadData(sortType, mQuery, page);
            }
        };

        mRecViewNews.addOnScrollListener(mEndlessScrollListener);

        mPresenter.loadData(sortType, mQuery, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(Article article) {
        Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.ARTICLE_MODEL, article);
        startActivity(intent);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateRecyclerView(List<Article> articleList) {
        //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mData.size() - 1);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * We need to reset previous state of adapter and recycler view before every new query
     * **/
    @Override
    public void resetQuery() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        mEndlessScrollListener.resetState();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 0) {
            mQuery = s;
            handler.removeCallbacksAndMessages(null);
            // in task it is specified 500ms but here I set to 1000 it feels more natural and user friendly
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.newQuery();
                    mPresenter.loadData(sortType, mQuery, 1);
                }
            }, 1000);
        }
        return false;
    }
}
