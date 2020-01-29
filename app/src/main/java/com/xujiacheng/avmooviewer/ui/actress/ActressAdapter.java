package com.xujiacheng.avmooviewer.ui.actress;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.search.SearchViewModel;

import java.security.MessageDigest;

public class ActressAdapter extends ListAdapter<Actor, ActressAdapter.ViewHolder> {
    private ActressViewModel actressViewModel;
    private boolean isLoadFinished = false;

    public boolean isLoadFinished() {
        return isLoadFinished;
    }

    public void setLoadFinished(boolean loadFinished) {
        isLoadFinished = loadFinished;
    }

    public ActressAdapter(ActressViewModel actressViewModel) {
        super(new DiffUtil.ItemCallback<Actor>() {
            @Override
            public boolean areItemsTheSame(@NonNull Actor oldItem, @NonNull Actor newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Actor oldItem, @NonNull Actor newItem) {
                return oldItem.url.equals(newItem.url);
            }
        });
        this.actressViewModel = actressViewModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actress, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = ActressFragment.ITEM_CLICK;
                message.obj = getItem(viewHolder.getAdapterPosition()).url;
                ActressFragment.handler.sendMessage(message);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Actor item = getItem(position);
        holder.name.setText(item.name);
        Glide.with(holder.itemView)
                .load(item.imageURL)
                .placeholder(R.drawable.woman)
                .into(holder.header);
        if (position == getItemCount() - 1) {
            if (!isLoadFinished) {
                actressViewModel.loadMoreActress();
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView header;
        private final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.item_actress_header);
            name = itemView.findViewById(R.id.item_actress_name);
        }
    }
}
