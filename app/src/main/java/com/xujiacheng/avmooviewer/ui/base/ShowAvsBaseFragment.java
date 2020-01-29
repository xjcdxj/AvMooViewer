package com.xujiacheng.avmooviewer.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.xujiacheng.avmooviewer.R;

//显示视频列表的fragment抽象类，已经提取出了一些控件供使用
public abstract class ShowAvsBaseFragment extends Fragment {

    public static final int REFRESH_DATA = 0;
    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_FAILED = 2;
    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    private ProgressBar loadingData;
    private TextView loadFailed;
    private DrawerLayout drawerLayout;




    public ShowAvsBaseFragment() {

    }


    @SuppressLint("UseSparseArrays")


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_avs_base_fragment, container, false);
        initWidget(view);//提取控件
        if (isHomeFragment()) {
            toolbar.setNavigationIcon(R.drawable.white_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        uiOperation();
        return view;
    }

    //返回true表示作为顶级界面，false表示是子界面，对左上角导航菜单做修改
    public abstract boolean isHomeFragment();

    //子类的自定义操作在这完成
    public abstract void uiOperation();

    private void initWidget(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        loadFailed = view.findViewById(R.id.load_failed_text);
        recyclerView = view.findViewById(R.id.recycler_view);
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        loadingData = view.findViewById(R.id.all_vid_loading);
    }

    //提供方法供子类使用，来改变视图中progressBar和显示加载失败的显示与隐藏
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

    }
}
