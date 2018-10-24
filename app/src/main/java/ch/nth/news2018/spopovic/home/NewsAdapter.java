package ch.nth.news2018.spopovic.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.nth.news2018.spopovic.R;
import ch.nth.news2018.spopovic.data.models.Article;
import ch.nth.news2018.spopovic.utils.DateConverter;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private List<Article> mData = new ArrayList<>();
    private IOnItemClick mListener;

    public NewsAdapter(Context mContext, List<Article> data) {
        this.mContext = mContext;
        this.mData = data;
        this.mListener = (IOnItemClick) mContext;
    }

    public interface IOnItemClick {
        void onItemClick(Article article);
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_main, viewGroup, false);
        return new NewsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder viewHolder, int position) {

        final Article article = mData.get(position);
        viewHolder.mTime.setText(DateConverter.convertDateStringToPastTimeString(article.getPublishedAt()));
        viewHolder.mTitle.setText(article.getTitle());
        if (!TextUtils.isEmpty(article.getUrlToImage()))
            Picasso.get().load(article.getUrlToImage()).placeholder(R.drawable.ic_newspaper).into(viewHolder.mNewsImage);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.tv_news_tile)
        TextView mTitle;
        @BindView(R.id.iv_news)
        ImageView mNewsImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
