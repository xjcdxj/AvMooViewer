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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;

import java.util.ArrayList;

//显示视频列表的fragment抽象类，已经提取出了一些控件供使用
public abstract class ShowAvsBaseFragment extends Fragment {
    private static final String TAG = "ShowAvsBaseFragment";

    private static final int REFRESH_DATA = 0;
    protected static final int LOAD_SUCCESS = 1;
    public static final int LOAD_FAILED = 2;
    protected static final int NO_RESULT = 3;
    protected Toolbar mToolbar;
    protected RecyclerView mRecyclerView;
    private ProgressBar loadingData;
    private TextView loadFailed;
    private DrawerLayout drawerLayout;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseViewModel mBaseViewModel;
    private BaseAdapter baseAdapter;


    private FloatingActionButton scrollTop;

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
                dataStatusChange(REFRESH_DATA);
            }
        });


        //使用默认adapter layoutmanager


        if (useDefaultConfig()) {
            defaultConfiguration();
        }
        uiOperation();
        return view;
    }

    private void defaultConfiguration() {
//        requireActivity().setActionBar(mToolbar);
//        getActivity().setSupportActionBar();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        baseAdapter = new BaseAdapter() {
            @Override
            public void loadingProcess(final AvItemViewHolder holder) {
                holder.itemView.setClickable(false);
                if (mBaseViewModel.isLoadFinished()) {
                    holder.setLoadStatus(AvItemViewHolder.FINISH);
                } else {
                    if (mBaseViewModel.isLoadSuccess()) {

                        holder.setLoadStatus(AvItemViewHolder.LOADING);
                        mBaseViewModel.loadMoreAvData();
                    } else {
                        holder.itemView.setClickable(true);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBaseViewModel.loadMoreAvData();
                                holder.loading.setVisibility(View.VISIBLE);
                                holder.loadFinish.setVisibility(View.GONE);
                            }
                        });
                        holder.setLoadStatus(AvItemViewHolder.FAILED);
                    }
                }
            }
        };
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    scrollTop.setVisibility(View.VISIBLE);
                } else {
                    scrollTop.setVisibility(View.GONE);
                }
            }
        });

        mRecyclerView.setAdapter(baseAdapter);
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mBaseViewModel.mAvListData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                if (avs.size() == 0) {
                    if (mBaseViewModel.isLoadFinished()) {
                        dataStatusChange(NO_RESULT);
                    } else {
//                    mSwipeRefreshLayout.setVisibility(View.GONE);
                        dataStatusChange(REFRESH_DATA);
                    }
                } else {
//                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    dataStatusChange(LOAD_SUCCESS);
                }
                if (mBaseViewModel.isDataReady()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mBaseViewModel.isLoadSuccess()) {
                        baseAdapter.submitList(new ArrayList<>(avs));
                    } else {
                        if (avs.size() == 0) {
                            dataStatusChange(LOAD_FAILED);
                        } else {
                            baseAdapter.notifyItemChanged(baseAdapter.getItemCount() - 1);
                        }
                    }
                }
            }
        });
    }


    public abstract BaseViewModel setViewModel();    //返回true表示作为顶级界面，false表示是子界面，对左上角导航菜单做修改

    public abstract boolean isHomeFragment();

    public abstract boolean useDefaultConfig();

    //刷新界面数据
    protected void refreshData() {
        if (mBaseViewModel != null && baseAdapter != null) {
            mBaseViewModel.refreshAvData();
        }
    }


    //子类的自定义操作在这完成
    public abstract void uiOperation();


    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        mToolbar = view.findViewById(R.id.detail_toolbar);
        loadFailed = view.findViewById(R.id.load_failed_text);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        scrollTop = view.findViewById(R.id.scroll_top);
        scrollTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        loadingData = view.findViewById(R.id.all_vid_loading);
        ConstraintLayout rootLayout = view.findViewById(R.id.base_root_view);
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
                loadFailed.setText(R.string.no_result);
                loadingData.setVisibility(View.GONE);
                loadFailed.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
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
        }
    }


}
