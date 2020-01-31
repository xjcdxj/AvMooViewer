package com.xujiacheng.avmooviewer.ui.base;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

import java.util.ArrayList;

public abstract class AvItemAdapter extends RecyclerView.Adapter<AvItemAdapter.AvItemViewHolder> {
    private static final int NORMAL = 1;
    public static final int LOAD_MORE = 3;
    private static final int LOADING = 0;
    private ArrayList<Av> mAvData;
    private Handler mHandler;

    public AvItemAdapter(ArrayList<Av> mAvData, Handler mHandler) {
        this.mAvData = mAvData;
        this.mHandler = mHandler;
    }

    public AvItemAdapter(ArrayList<Av> mAvData) {
        this.mAvData = mAvData;
    }

    @NonNull
    @Override
    public AvItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final AvItemAdapter.AvItemViewHolder avItemViewHolder;
        //最后一个是加载中的视图
        if (viewType == NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_av, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
            avItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //导航到点击的视频的详情页
                    DetailFragment detailFragment = new DetailFragment(mAvData.get(avItemViewHolder.getAdapterPosition()).url);
                    MainActivity.changeFragment(detailFragment, false);
                }
            });

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
        }
        return avItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AvItemViewHolder holder, final int position) {
        if (position == getItemCount() - 1) {
            boolean isLoadFinished = loadMoreData();
            if (isLoadFinished) {
                holder.loading.setVisibility(View.GONE);
                holder.loadFinish.setVisibility(View.VISIBLE);
            }
        } else {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new AlphaAnimation(0F, 1F));
            animationSet.setDuration(150);
            holder.itemView.startAnimation(animationSet);
            animationSet.setAnimationListener(
                    new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Av item = mAvData.get(position);
                            holder.id.setText(item.id);
                            holder.name.setText(item.name);
                            holder.releaseDate.setText(item.releaseDate);
                            Glide.with(holder.cover)
                                    .load(item.coverURL)
                                    .into(holder.cover);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    }
            );
        }

    }

    @Override
    public int getItemCount() {
        if (mAvData.size() == 0) {
            return 0;
        } else {
            return mAvData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LOADING;
        }
        return NORMAL;
    }

    public class AvItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cover;
        private final TextView name;
        private final TextView id;
        private final TextView releaseDate;
        private final ProgressBar loading;
        private final TextView loadFinish;

        AvItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_av_cover);
            name = itemView.findViewById(R.id.item_av_name);
            id = itemView.findViewById(R.id.item_av_id);
            releaseDate = itemView.findViewById(R.id.item_av_date);
            loading = itemView.findViewById(R.id.item_loading_loading);
            loadFinish = itemView.findViewById(R.id.item_loading_no_more);
        }
    }

    public abstract boolean loadMoreData();
}
