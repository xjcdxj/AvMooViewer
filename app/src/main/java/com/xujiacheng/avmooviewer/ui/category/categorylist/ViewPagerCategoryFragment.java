package com.xujiacheng.avmooviewer.ui.category.categorylist;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.databinding.ViewPagerCategoryFragmentBinding;
import com.xujiacheng.avmooviewer.itembean.Category;

import java.util.ArrayList;

public class ViewPagerCategoryFragment extends Fragment {

    private ViewPagerCategoryViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(ViewPagerCategoryViewModel.class);
//        ViewPagerCategoryFragmentBinding binding = ViewPagerCategoryFragmentBinding.inflate(inflater, container, false);

        ViewPagerCategoryFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_category_fragment, container, false);
//        binding.setIsvisibale(false);
        binding.setLifecycleOwner(this);
        if (mViewModel.categoryTabs == null || mViewModel.categoryTabs.size() == 0) {
            mViewModel.getCategoryData();
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar mToolbar = view.findViewById(R.id.category_toolbar);
        mToolbar.setTitle(R.string.category);
        mToolbar.setNavigationIcon(R.drawable.white_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
//                closeKeyBoard();
            }
        });
        final TextView loadFailed = view.findViewById(R.id.category_load_failed);
        final TabLayout tabLayout = view.findViewById(R.id.category_tabs);
        final ProgressBar progressBar = view.findViewById(R.id.category_loading);
        final ViewPager2 viewPager2 = view.findViewById(R.id.category_view_pager);
        loadFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                mViewModel.getCategoryData();
            }
        });
        tabLayout.setVisibility(View.GONE);
        viewPager2.setVisibility(View.GONE);
        mViewModel.categoriesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<ArrayList<Category>>>() {
            @Override
            public void onChanged(final ArrayList<ArrayList<Category>> arrayLists) {

                if (arrayLists.size() > 0 && arrayLists.size() == mViewModel.categoryTabs.size()) {
                    if (mViewModel.isLoadSuccess) {
                        progressBar.setVisibility(View.GONE);
                        loadFailed.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        loadFailed.setVisibility(View.VISIBLE);
                    }
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager2.setVisibility(View.VISIBLE);
                    viewPager2.setAdapter(new FragmentStateAdapter(ViewPagerCategoryFragment.this) {
                        @NonNull
                        @Override
                        public Fragment createFragment(int position) {
                            return new ShowCategoryFragment(arrayLists.get(position));
                        }

                        @Override
                        public int getItemCount() {
                            return arrayLists.size();
                        }
                    });
                    new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            tab.setText(mViewModel.categoryTabs.get(position));

                        }
                    }).attach();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);


    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        // TODO: Use the ViewModel
//    }

}
