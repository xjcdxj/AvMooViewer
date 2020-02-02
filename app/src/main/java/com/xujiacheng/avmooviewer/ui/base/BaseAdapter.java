package com.xujiacheng.avmooviewer.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

public abstract class BaseAdapter extends ListAdapter<Av, BaseAdapter.AvItemViewHolder> {
//    private static final String TAG = "BaseAdapter";
    private static final int LOADING = 0;
    private static final int NORMAL = 1;
//    public static final int LOAD_MORE = 3;


    BaseAdapter() {
        super(new DiffUtil.ItemCallback<Av>() {
            @Override
            public boolean areItemsTheSame(@NonNull Av oldItem, @NonNull Av newItem) {
                return oldItem.url.equals(newItem.url);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Av oldItem, @NonNull Av newItem) {
                return oldItem.coverURL.equals(newItem.coverURL)
                        && oldItem.name.equals(newItem.name);
            }
        });
    }


    @NonNull
    @Override
    public AvItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final AvItemViewHolder avItemViewHolder;
        //最后一个是加载中的视图
        if (viewType == NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_av, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
            avItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //导航到点击的视频的详情页
//                    AnimationSet animationSet = new AnimationSet(true);
//                    animationSet.addAnimation(new ScaleAnimation(1F, 1F, 1F, 2F));
//                    animationSet.setDuration(500);
//                    animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
//                    avItemViewHolder.itemView.startAnimation(animationSet);
                    MainActivity.changeFragment(new DetailFragment(getItem(avItemViewHolder.getAdapterPosition()).url), false);

                }
            });

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
        }
        return avItemViewHolder;
    }

    public abstract void loadingProcess(AvItemViewHolder holder);

    @Override
    public void onBindViewHolder(@NonNull final AvItemViewHolder holder, final int position) {
        if (position == getItemCount() - 1) {
            loadingProcess(holder);
        } else {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(new TranslateAnimation(0F, 0F, 50F, 0F));
//            animationSet.addAnimation(new ScaleAnimation(0.5F,1F,1F,1F));
            animationSet.setDuration(500);
            animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            holder.itemView.startAnimation(animationSet);
            Av item = getItem(position);
            holder.id.setText(item.id);
            holder.name.setText(item.name);
            holder.releaseDate.setText(item.releaseDate);
            Glide.with(holder.cover)
                    .load(item.coverURL)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.cover);

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LOADING;
        }
        return NORMAL;
    }

    @Override
    public int getItemCount() {
        if (super.getItemCount() == 0) {
            return 0;
        }
        return super.getItemCount() + 1;
    }

    class AvItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover;
        private final TextView name;
        private final TextView id;
        private final TextView releaseDate;
        final ProgressBar loading;
        final TextView loadFinish;
        static final int LOADING = 0;
        static final int FINISH = 1;
        static final int FAILED = 2;


        void setLoadStatus(int status) {
            switch (status) {
                case LOADING:
                    loading.setVisibility(View.VISIBLE);
                    loadFinish.setVisibility(View.GONE);
                    break;
                case FINISH:
                    loadFinish.setText(R.string.load_finished);
                    loadFinish.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    break;
                case FAILED:
                    loading.setVisibility(View.GONE);
                    loadFinish.setText(R.string.load_failed_click_to_retry);
                    loadFinish.setVisibility(View.VISIBLE);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + status);
            }
        }

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
}
