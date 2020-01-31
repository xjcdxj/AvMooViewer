package com.xujiacheng.avmooviewer.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;

import java.util.ArrayList;

//显示视频列表的fragment抽象类，已经提取出了一些控件供使用
public abstract class ShowAvsBaseFragment extends Fragment {
    private static final String TAG = "ShowAvsBaseFragment";

    public static final int REFRESH_DATA = 0;
    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_FAILED = 2;
    public static final int NO_RESULT = 3;
    protected Toolbar mToolbar;
    protected RecyclerView mRecyclerView;
    private ProgressBar loadingData;
    protected TextView loadFailed;
    private DrawerLayout drawerLayout;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected AvItemAdapter mAvAdapter;
    private BaseViewModel mBaseViewModel;


    public ShowAvsBaseFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_avs_base_fragment, container, false);
        initView(view);//提取控件
        mBaseViewModel = setViewModel();

        mSwipeRefreshLayout.setEnabled(true);
        loadFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });


        //使用默认adapter layoutmanager


        if (useDefaultConfig()) {
            defaulConfiguration();
        }
        uiOperation();
        return view;
    }

    private void defaulConfiguration() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAvAdapter = new AvItemAdapter(mBaseViewModel.showAvItems) {
            @Override
            public boolean loadMoreData() {
                if (mBaseViewModel.isLoadFinished()) {
                    return true;
                } else {
                    mBaseViewModel.loadMoreAvData();
                    return false;
                }
            }
        };
        mRecyclerView.setAdapter(mAvAdapter);
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mBaseViewModel.mAvLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                if (mBaseViewModel.isDataReady()) {
                    //只要数据大于0，就表明加载成功了
                    if (avs.size() > 0) {
                        dataStatusChange(LOAD_SUCCESS);
                        if (mBaseViewModel.isDataReady()) {
                            if (mBaseViewModel.isLoadSuccess()) {//数据大于0，数据准备好了，而且也没有获取失败，就提交数据
                                int positionStart = mBaseViewModel.showAvItems.size() - 1;
                                if (positionStart < 0) {
                                    positionStart = 0;
                                }
                                int itemCount = avs.size() - mBaseViewModel.showAvItems.size();
//                                mBaseViewModel.showAvItems.clear();
                                for (int i = positionStart; i < avs.size(); i++) {
                                    mBaseViewModel.showAvItems.add(avs.get(i));
                                }
//                                mBaseViewModel.showAvItems.addAll(avs);
                                mAvAdapter.notifyItemRangeInserted(positionStart, itemCount);

                            }
                        }
                    } else {
                        //数据等于0，判断获取成功与否，分别改变界面
                        if (mBaseViewModel.isLoadSuccess()) {
                            dataStatusChange(NO_RESULT);
                        } else {
                            dataStatusChange(LOAD_FAILED);
                        }
                    }
                }
                mBaseViewModel.initStatus();
            }
        });
    }

    public abstract BaseViewModel setViewModel();    //返回true表示作为顶级界面，false表示是子界面，对左上角导航菜单做修改

    public abstract boolean isHomeFragment();

    public abstract boolean useDefaultConfig();

    //刷新界面数据
    protected void refreshData() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        dataStatusChange(REFRESH_DATA);
        if (mBaseViewModel != null && mAvAdapter != null) {
            mBaseViewModel.showAvItems.clear();
            mAvAdapter.notifyDataSetChanged();
            mBaseViewModel.requestAvData();
        }
    }


    //子类的自定义操作在这完成
    public abstract void uiOperation();


    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        mToolbar = view.findViewById(R.id.detail_toolbar);
        loadFailed = view.findViewById(R.id.load_failed_text);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        loadingData = view.findViewById(R.id.all_vid_loading);
        CoordinatorLayout rootLayout = view.findViewById(R.id.coordinatorLayout2);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyBoard();

                return false;
            }
        });
        if (isHomeFragment()) {
            mToolbar.setNavigationIcon(R.drawable.white_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    closeKeyBoard();
                }
            });
        } else {
            mToolbar.setNavigationIcon(R.drawable.white_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().onBackPressed();
                    closeKeyBoard();
                }
            });
        }

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
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                break;
            case LOAD_SUCCESS:
                loadingData.setVisibility(View.GONE);
                loadFailed.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                break;
            case NO_RESULT:
                loadFailed.setText("No Result");
                loadingData.setVisibility(View.GONE);
                loadFailed.setVisibility(View.VISIBLE);
        }
    }

    protected void closeKeyBoard() {
        MenuItem menuItem = mToolbar.getMenu().findItem(R.id.menu_search);
        if (menuItem != null) {
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.onActionViewCollapsed();
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
            }
//                    }
//                    Toast.makeText(requireContext(), "Cancel search", Toast.LENGTH_SHORT).show();

        }
    }


}
