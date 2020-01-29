package com.xujiacheng.avmooviewer.ui.collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

import java.util.ArrayList;
import java.util.Objects;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ItemViewHolder> {

    private MutableLiveData<ArrayList<Av>> mData;


    CollectionsAdapter(MutableLiveData<ArrayList<Av>> collections) {
        this.mData = collections;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Objects.requireNonNull(mData.getValue()).get(itemViewHolder.getAdapterPosition()).url;
                MainActivity.changeFragment(new DetailFragment(url), false);

            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Av av = Objects.requireNonNull(mData.getValue()).get(position);
        holder.name.setText(av.name);
        Glide.with(holder.cover)
                .asBitmap()
                .load(av.bigCoverURL)
                .into(holder.cover);


    }

    @Override
    public int getItemCount() {

        return Objects.requireNonNull(mData.getValue()).size();
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
