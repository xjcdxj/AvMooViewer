package com.xujiacheng.avmooviewer.ui.base;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xujiacheng.avmooviewer.R;


public abstract class ShowAvsBaseFragment extends Fragment {

    private static final String TAG = "ShowAvsBaseFragment";
    public static final int REFRESH_DATA = 0;
    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_FAILED = 2;
    protected FragmentManager mFragmentManager;


    public ShowAvsBaseFragment() {

    }


    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    protected ProgressBar loadingData;
    protected TextView toolbarTitle;
    protected TextView loadFailed;
    protected DrawerLayout drawerLayout;

    @SuppressLint("UseSparseArrays")


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_avs_base_fragment, container, false);
        mFragmentManager = requireActivity().getSupportFragmentManager();
        BaseAdapter.fragmentManager = mFragmentManager;
        Log.d(TAG, "onCreateView: creat");
        initWidget(view);
        if (isHomeFragment()) {
            toolbar.setNavigationIcon(R.drawable.white_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        } else {
            toolbar.setNavigationIcon(R.drawable.white_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().onBackPressed();
                }
            });
        }
        return view;
    }

    public abstract boolean isHomeFragment();

    public abstract void uiOperation();

    private void initWidget(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        loadFailed = view.findViewById(R.id.load_failed_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        loadingData = view.findViewById(R.id.all_vid_loading);
    }

    protected void dataStatusChange(int status) {
        switch (status) {
            case REFRESH_DATA:
                loadingData.setVisibility(View.VISIBLE);
                loadFailed.setVisibility(View.GONE);
                break;
            case LOAD_FAILED:
                loadingData.setVisibility(View.GONE);
                loadFailed.setVisibility(View.VISIBLE);
                break;
            case LOAD_SUCCESS:
                loadingData.setVisibility(View.GONE);
                loadFailed.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        uiOperation();
    }
}
