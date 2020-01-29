package com.xujiacheng.avmooviewer.ui.base;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

public  class BaseAdapter extends ListAdapter<Av, BaseAdapter.AvItemViewHolder> {
    private static final int LOADING = 0;
    private static final int NORMAL = 1;
    public static FragmentManager fragmentManager;
    protected BaseViewModel baseViewModel;
    public static final int ITEM_CLICK = 2;
    public static final int LOAD_MORE = 3;
    private boolean loadFinish = false;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }



    public boolean isLoadFinish() {
        return loadFinish;
    }

    public void setLoadFinish(boolean loadFinish) {
        this.loadFinish = loadFinish;
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
        if (viewType == NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_av, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
            avItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentManager != null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction.replace(R.id.container, new DetailFragment(getItem(avItemViewHolder.getAdapterPosition()).url));
                        transaction.commit();
                    } else {
                        Message message = new Message();
                        message.obj = getItem(avItemViewHolder.getAdapterPosition()).url;
                        message.what = ITEM_CLICK;
                        mHandler.sendMessage(message);
                    }
                }
            });

//            setItemViewClickListener(avItemViewHolder, fragmentManager);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            avItemViewHolder = new AvItemViewHolder(view);
        }
        return avItemViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AvItemViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            if (isLoadFinish()) {
                holder.loading.setVisibility(View.GONE);
                holder.loadFinish.setVisibility(View.VISIBLE);

            } else {
                Message message = new Message();
                message.what = LOAD_MORE;
                mHandler.sendMessage(message);
            }
            return;
        }
        Av item = getItem(position);
        Glide.with(holder.cover)
                .load(item.coverURL)
                .into(holder.cover);
        holder.id.setText(item.id);
        holder.name.setText(item.name);
        holder.releaseDate.setText(item.releaseDate);


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

    public class AvItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover;
        private final TextView name;
        private final TextView id;
        private final TextView releaseDate;
        private final ProgressBar loading;
        private final TextView loadFinish;

        public AvItemViewHolder(@NonNull View itemView) {
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
