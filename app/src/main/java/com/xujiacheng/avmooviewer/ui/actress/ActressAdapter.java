package com.xujiacheng.avmooviewer.ui.actress;

import android.os.Handler;
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
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.ui.actress.actressvideos.ActressesVideosFragment;

public class ActressAdapter extends ListAdapter<Actor, ActressAdapter.ViewHolder> {
    static int LOAD_MORE = 1;
    private boolean isLoadFinished = false;
    private Handler mHandler;

    ActressAdapter(Handler handler) {
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
        this.mHandler = handler;
    }

    private boolean isLoadFinished() {
        return isLoadFinished;
    }

    void setLoadFinished(boolean loadFinished) {
        isLoadFinished = loadFinished;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actress, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeFragment(new ActressesVideosFragment(getItem(viewHolder.getAdapterPosition()).url), false);
//                Message message = new Message();
//                message.what = ActressFragment.ITEM_CLICK;
//                message.obj = getItem(viewHolder.getAdapterPosition()).url;
//                ActressFragment.handler.sendMessage(message);

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
            if (!isLoadFinished()) {
                mHandler.sendEmptyMessage(LOAD_MORE);
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView header;
        private final TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.item_actress_header);
            name = itemView.findViewById(R.id.item_actress_name);
        }
    }
}
