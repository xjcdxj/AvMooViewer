package com.xujiacheng.avmooviewer.ui.collections;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;

import java.util.ArrayList;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ItemViewHolder> {
    public static final int ITEM_CLICK = 1;
    private MutableLiveData<ArrayList<Av>> mData;
    private Handler mHandler;


    public CollectionsAdapter(MutableLiveData<ArrayList<Av>> collections, Handler handler) {
        this.mData = collections;
        this.mHandler = handler;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = ITEM_CLICK;
                message.obj = mData.getValue().get(itemViewHolder.getAdapterPosition()).url;
                mHandler.sendMessage(message);

            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Av av = mData.getValue().get(position);
        holder.name.setText(av.name);
        if (av.bigCoverImage != null) {
            holder.cover.setImageBitmap(av.bigCoverImage);
        } else {
            Glide.with(holder.cover)
                    .asBitmap()
                    .load(av.bigCoverURL)
                    .into(holder.cover);
        }


    }

    @Override
    public int getItemCount() {
        return mData.getValue().size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover;
        private final TextView name;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_collection_cover);
            name = itemView.findViewById(R.id.item_collection_name);
        }
    }
}
