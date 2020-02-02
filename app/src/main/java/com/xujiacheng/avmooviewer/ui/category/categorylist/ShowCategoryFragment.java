package com.xujiacheng.avmooviewer.ui.category.categorylist;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Category;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;
import com.xujiacheng.avmooviewer.ui.category.CategoryFragment;

import java.util.ArrayList;
import java.util.Objects;


public class ShowCategoryFragment extends ShowAvsBaseFragment {
    private static final String TAG = "ShowCategoryFragment";
    private CategoryListViewModel mViewModel;

    @Override
    public boolean isHomeFragment() {
        return true;
    }

    @Override
    public boolean useDefaultConfig() {
        return false;
    }

    @Override
    public void uiOperation() {
        mToolbar.setTitle("Category");
        mViewModel = new ViewModelProvider(requireActivity()).get(CategoryListViewModel.class);
        final CategoryAdapter categoryAdapter = new CategoryAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        mRecyclerView.setAdapter(categoryAdapter);
        mViewModel.mCategoryList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                if (mViewModel.isDataReady) {
                    Log.d(TAG, "onChanged: size = " + categories.size());
                    dataStatusChange(LOAD_SUCCESS);
                    if (categories.size() > 0) {
                        dataStatusChange(LOAD_SUCCESS);
                    }
                    categoryAdapter.submitList(categories);

                }
            }
        });
        if (Objects.requireNonNull(mViewModel.mCategoryList.getValue()).size() == 0) {
            mViewModel.getCategoryData();
        }
    }

    @Override
    public BaseViewModel setViewModel() {
        return null;
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
            Category category = Objects.requireNonNull(mViewModel.mCategoryList.getValue()).get(position);
            holder.categoryName.setText(category.name);
        }

        @Override
        public int getItemCount() {
            return Objects.requireNonNull(mViewModel.mCategoryList.getValue()).size();
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
