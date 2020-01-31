package com.xujiacheng.avmooviewer.ui.collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

import java.util.ArrayList;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.CollectionItemViewHolder> {
    private ArrayList<Av> mCollections;

    public CollectionItemAdapter(ArrayList<Av> mCollections) {
        this.mCollections = mCollections;
    }

    @NonNull
    @Override
    public CollectionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        final CollectionItemViewHolder itemViewHolder = new CollectionItemViewHolder(view);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeFragment(new DetailFragment(mCollections.get(itemViewHolder.getAdapterPosition()).url), false);
            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionItemViewHolder holder, int position) {
        Av av = mCollections.get(position);
        holder.name.setText(av.name);
        Glide.with(holder.cover)
//                .asBitmap()
                .load(av.bigCoverURL)
                .placeholder(R.drawable.avmooviewer)
//                .override(210, 300)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return mCollections.size();
    }

    public class CollectionItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cover;
        private final TextView name;

        CollectionItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_collection_cover);
            name = itemView.findViewById(R.id.item_collection_name);
        }
    }
}
