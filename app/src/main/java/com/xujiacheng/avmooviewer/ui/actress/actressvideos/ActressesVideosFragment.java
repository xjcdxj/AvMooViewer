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

public class ActressesVideosFragment extends ShowAvsBaseFragment {
    private static final String TAG = "ActressesVideosFragment";
    private String actressURL;
    private ActressVideoViewModel actressVideoViewModel;
    public ActressesVideosFragment(String actressURL) {
        //https://avmask.com/cn/star/26532bc87b4ce1d1/page/2
        this.actressURL = actressURL;
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
                switch (msg.what) {
                    case BaseAdapter.LOAD_MORE:
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
                baseAdapter.setLoadFinish(actressVideoViewModel.isLoadingFinished());
                Log.d(TAG, "onChanged: size = " + avs.size());
                if (actressVideoViewModel.isDataReady()) {

                    baseAdapter.submitList(new ArrayList<Av>(avs));
                    dataStatusChange(LOAD_SUCCESS);
                }
                actressVideoViewModel.setDataReady(false);
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


//        if (ActressVideoViewModel.getBaseUrl() == null || !ActressVideoViewModel.getBaseUrl().equals(this.actressURL)) {
//            actressVideoViewModel.actressName.setValue("");
//            actressVideoViewModel.actressAv.setValue(new ArrayList<Av>());
//            actressVideoViewModel.initActressVideos();
//
//        } else {
//            actressVideoViewModel.setDataReady(true);
//        }

        if (ActressVideoViewModel.actressURL == null) {
            ActressVideoViewModel.actressURL = this.actressURL;
            actressVideoViewModel.initActressVideos();
        } else {
            if (!ActressVideoViewModel.actressURL.equals(this.actressURL)) {
                ActressVideoViewModel.actressURL = this.actressURL;
                actressVideoViewModel.initActressVideos();
                actressVideoViewModel.actressName.setValue("");
            } else {
                actressVideoViewModel.setDataReady(true);
            }
        }


    }
}
