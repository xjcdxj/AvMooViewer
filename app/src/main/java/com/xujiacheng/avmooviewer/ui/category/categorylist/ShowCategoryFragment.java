package com.xujiacheng.avmooviewer.ui.category.categorylist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Category;
import com.xujiacheng.avmooviewer.ui.category.CategoryFragment;

import java.util.ArrayList;


public class ShowCategoryFragment extends Fragment {
//    private static final String TAG = "ShowCategoryFragment";

    private ArrayList<Category> categories;

    ShowCategoryFragment(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ShowCategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.base_recycler_view);
        final CategoryAdapter categoryAdapter = new CategoryAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(categoryAdapter);
        if (categories != null) {
            categoryAdapter.submitList(categories);
        }
    }

    class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {


        CategoryAdapter() {
            super(new DiffUtil.ItemCallback<Category>() {
                @Override
                public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                    return oldItem.url.equals(newItem.url);
                }
            });
        }

        @NonNull
        @Override
        public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            final CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
            categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = getItem(categoryViewHolder.getAdapterPosition());
                    CategoryFragment categoryFragment = new CategoryFragment(category.url, category.name);
                    MainActivity.changeFragment(categoryFragment, false);
                }
            });
            return categoryViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
            Category category = categories.get(position);
            holder.categoryName.setText(category.name);
        }

        @Override
        public int getItemCount() {
            if (categories == null) {
                return 0;
            }
            return categories.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {

            private final TextView categoryName;

            CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.item_category_name);
            }
        }
    }
}
