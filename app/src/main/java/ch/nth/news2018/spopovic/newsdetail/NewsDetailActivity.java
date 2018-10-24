package ch.nth.news2018.spopovic.newsdetail;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.nth.news2018.spopovic.R;
import ch.nth.news2018.spopovic.data.db.AppDatabase;
import ch.nth.news2018.spopovic.data.db.AppExecutors;
import ch.nth.news2018.spopovic.data.models.Article;

public class NewsDetailActivity extends AppCompatActivity {

    public static final String ARTICLE_MODEL = "article_model";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mArticleTitle;
    @BindView(R.id.tv_published_at)
    TextView mPublishedDate;
    @BindView(R.id.tv_author)
    TextView mAuthor;
    @BindView(R.id.tv_url)
    TextView mUrl;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.iv_article)
    ImageView mArticleImage;
    @BindView(R.id.fab_favorite)
    FloatingActionButton mFavoriteButton;

    private Article mArticle;
    private boolean isFavorite;
    private AppDatabase appDatabase;
    private AppExecutors appExecutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appDatabase = AppDatabase.getInstance(this);
        appExecutors = AppExecutors.getInstance();

        if (getIntent() != null && getIntent().hasExtra(ARTICLE_MODEL)) {
            mArticle = (Article) getIntent().getSerializableExtra(ARTICLE_MODEL);
            setData(mArticle);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setData(final Article article) {

        if (article != null) {

            Picasso.get().load(article.getUrlToImage()).placeholder(R.drawable.ic_newspaper).into(mArticleImage);
            mArticleTitle.setText(article.getTitle());
            if (!TextUtils.isEmpty(article.getAuthor())) {
                mAuthor.setVisibility(View.VISIBLE);
                mAuthor.setText(article.getAuthor());
            } else {
                mAuthor.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(article.getPublishedAt())) {
                mPublishedDate.setVisibility(View.VISIBLE);
                mPublishedDate.setText(article.getPublishedAt());
            } else {
                mPublishedDate.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(article.getUrl())) {
                mUrl.setVisibility(View.VISIBLE);
                mUrl.setText(article.getUrl());
            } else {
                mUrl.setVisibility(View.GONE);
            }

            mContent.setText(article.getContent());

            //here we will check if we have article in favorites (for now we use title as identifyer)
            appExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final Article article1 = appDatabase.dataDao().getArticleByTytle(article.getTitle());
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (article1 == null) {
                                isFavorite = false;
                                mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                            } else {
                                isFavorite = true;
                                mFavoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }
                        }
                    });
                }
            });
        }
    }

    @OnClick(R.id.fab_favorite)
    public void saveFavoriteArticle() {
        if (isFavorite) {
            isFavorite = false;
            appExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.dataDao().deleteArticle(mArticle);
                }
            });
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
        } else {
            isFavorite = true;
            appExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.dataDao().insertArticle(mArticle);
                }
            });
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
        }
    }
}
