package com.xujiacheng.avmooviewer.ui.collections;

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
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

public class CollectionsAdapter extends ListAdapter<Av, CollectionsAdapter.ItemViewHolder> {


    CollectionsAdapter() {
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
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeFragment(new DetailFragment(getItem(itemViewHolder.getAdapterPosition()).url), false);
            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Av av = getItem(position);
        holder.name.setText(av.name);
        Glide.with(holder.cover)
                .asBitmap()
                .load(av.bigCoverURL)
                .into(holder.cover);


    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover;
        private final TextView name;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_collection_cover);
            name = itemView.findViewById(R.id.item_collection_name);
        }
    }
}
