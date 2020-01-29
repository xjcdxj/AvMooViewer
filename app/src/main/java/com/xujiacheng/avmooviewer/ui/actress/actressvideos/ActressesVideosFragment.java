package com.xujiacheng.avmooviewer.ui.actress.actressvideos;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseAdapter;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class ActressesVideosFragment extends ShowAvsBaseFragment {
    private static final String TAG = "ActressesVideosFragment";
    private String actressURL;
    private ActressVideoViewModel actressVideoViewModel;

    public ActressesVideosFragment(String actressURL) {
        //https://avmask.com/cn/star/26532bc87b4ce1d1/page/2
        this.actressURL = actressURL + "/page/%s";
    }

    @Override
    public boolean isHomeFragment() {
        return false;
    }

    @Override
    public void uiOperation() {

        actressVideoViewModel = new ViewModelProvider(requireActivity()).get(ActressVideoViewModel.class);
        toolbar.setTitle(this.actressURL);
        final BaseAdapter baseAdapter = new BaseAdapter(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                //处理加载更多
                if (msg.what == BaseAdapter.LOAD_MORE) {
                    actressVideoViewModel.loadMore();
                }
                return false;
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(baseAdapter);
        actressVideoViewModel.actressAv.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                if (actressVideoViewModel.isDataReady()) {
                    baseAdapter.setLoadFinished(actressVideoViewModel.isLoadFinished());
                    baseAdapter.submitList(new ArrayList<>(avs));
                    dataStatusChange(LOAD_SUCCESS);
                }
                actressVideoViewModel.initStatus();
            }
        });
        actressVideoViewModel.actressName.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.length() > 0) {
                    toolbar.setTitle(s);
                }
            }
        });


        //如果ActressVideoViewModel里的静态变量actressURL为空，表示第一次创建演员fragment，
        // 这时候，要讲此次的演员url赋值给静态变量
        // 并且要加载数据
        Log.d(TAG, "uiOperation: 静态变量 " + ActressVideoViewModel.actressURL);
        Log.d(TAG, "uiOperation: 传入的 " + this.actressURL);
        if (ActressVideoViewModel.actressURL == null) {
            ActressVideoViewModel.actressURL = this.actressURL;
            actressVideoViewModel.initActressVideos();
            Log.d(TAG, "uiOperation: 值为空 加载数据");
        } else {
            //静态变量不为空，并且已有的静态变量和fragment构造方法传进来的不一致，就要重新加载数据
            if (!ActressVideoViewModel.actressURL.equals(this.actressURL)) {
                ActressVideoViewModel.actressURL = this.actressURL;
                actressVideoViewModel.initActressVideos();
                actressVideoViewModel.actressName.setValue("");
                Log.d(TAG, "uiOperation: 值不为空 且二者不想等 加载");
            } else {
                //静态变量与传进来的一致，fragment数据存在，不用重复获取
                if (Objects.requireNonNull(actressVideoViewModel.actressAv.getValue()).size() > 0) {
                    actressVideoViewModel.setDataReady(true);
                    Log.d(TAG, "uiOperation: 值相同 且已有数据长度大于0 不加载");
                } else {
                    actressVideoViewModel.initActressVideos();
                    Log.d(TAG, "uiOperation: 值相同， 但已有数据长度为0 ，加载");
                }
                Log.d(TAG, "uiOperation: already loaded");
            }
        }

    }
}
