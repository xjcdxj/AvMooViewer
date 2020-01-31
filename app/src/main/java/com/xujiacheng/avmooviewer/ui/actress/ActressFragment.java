package com.xujiacheng.avmooviewer.ui.actress;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class ActressFragment extends ShowAvsBaseFragment {
    private ActressViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(requireActivity()).get(ActressViewModel.class);
        mToolbar.setTitle(getString(R.string.actress));


        //adapter用handler通知加载更多数据
        final ActressAdapter mActressAdapter = new ActressAdapter(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == ActressAdapter.LOAD_MORE) {
                    mViewModel.loadMoreActress();
                }
                return false;
            }
        }));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.initData();
            }
        });
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(requireContext(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mActressAdapter);

        if (Objects.requireNonNull(mViewModel.actresses.getValue()).size() == 0) {
            mViewModel.initData();
        }
        mViewModel.actresses.observe(getViewLifecycleOwner(), new Observer<ArrayList<Actor>>() {
            @Override
            public void onChanged(ArrayList<Actor> actors) {
                if (actors.size() > 0) {
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    dataStatusChange(LOAD_SUCCESS);
                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }
                if (mViewModel.isDataReady()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mViewModel.isLoadSuccess()) {
                        mActressAdapter.submitList(new ArrayList<>(actors));
                    } else {
                        if (actors.size() == 0) {
                            dataStatusChange(LOAD_FAILED);
                        }
                    }
                    mActressAdapter.setLoadFinished(mViewModel.isLoadFinished());

                }
            }
        });
    }


    @Override
    public BaseViewModel setViewModel() {
        return null;
    }
}
