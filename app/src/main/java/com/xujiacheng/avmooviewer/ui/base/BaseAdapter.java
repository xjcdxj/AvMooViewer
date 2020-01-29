package com.xujiacheng.avmooviewer.ui.base;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class BaseAdapter extends ListAdapter<Av, BaseAdapter.AvItemViewHolder> {
    private static final String TAG = "BaseAdapter";
    private static final int LOADING = 0;
    private static final int NORMAL = 1;
    public static final int LOAD_MORE = 3;
    private boolean isLoadFinished = false;
    private boolean isLoadSuccess = true;

    private boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        isLoadSuccess = loadSuccess;
    }

    private boolean isLoadFinished() {
        return isLoadFinished;
    }

    public void setLoadFinished(boolean loadFinished) {
        this.isLoadFinished = loadFinished;
    }


    private Handler mHandler;

    public BaseAdapter(Handler handler) {
        super(new DiffUtil.ItemCallback<Av>() {
            @Override
            public boolean areItemsTheSame(@NonNull Av oldItem, @NonNull Av newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Av oldItem, @NonNull Av newItem) {
                return oldItem.url.equals(newItem.url);
            }
        });
        this.mHandler = handler;
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
                    MainActivity.changeFragment(new DetailFragment(getItem(avItemViewHolder.getAdapterPosition()).url), false);

                }
            });

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
        }
        return avItemViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AvItemViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            if (isLoadSuccess()) {
                if (isLoadFinished()) {
                    holder.loading.setVisibility(View.GONE);
                    holder.loadFinish.setText(R.string.load_finished);
                    holder.loadFinish.setVisibility(View.VISIBLE);
                } else {
                    mHandler.sendEmptyMessage(LOAD_MORE);
                }
            } else {
                holder.loading.setVisibility(View.GONE);
                holder.loadFinish.setText(R.string.load_failed);
                holder.loadFinish.setVisibility(View.VISIBLE);
            }
            Log.d(TAG, "onBindViewHolder: last " + isLoadSuccess());
            return;
        }
        Av item = getItem(position);
        holder.id.setText(item.id);
        holder.name.setText(item.name);
        holder.releaseDate.setText(item.releaseDate);
        try {
            Glide.with(holder.cover)
                    .load(item.coverURL)
                    .into(holder.cover);
        } catch (Exception ignored) {
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
}
