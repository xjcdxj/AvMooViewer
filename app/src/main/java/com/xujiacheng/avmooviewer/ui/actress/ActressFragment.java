package com.xujiacheng.avmooviewer.ui.actress;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class ActressFragment extends ShowAvsBaseFragment {
    private ActressViewModel actressViewModel;

    @Override
    public boolean isHomeFragment() {
        return true;
    }

    @Override
    public void uiOperation() {
        toolbar.setTitle(getString(R.string.actress));
        actressViewModel = new ViewModelProvider(requireActivity()).get(ActressViewModel.class);
        //adapter用handler通知加载更多数据
        final ActressAdapter actressAdapter = new ActressAdapter(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == ActressAdapter.LOAD_MORE) {
                    actressViewModel.loadMoreActress();
                }
                return false;
            }
        }));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(actressAdapter);
        if (Objects.requireNonNull(actressViewModel.actresses.getValue()).size() == 0) {
            actressViewModel.initData();
        }
        actressViewModel.actresses.observe(getViewLifecycleOwner(), new Observer<ArrayList<Actor>>() {
            @Override
            public void onChanged(ArrayList<Actor> actors) {
                if (actressViewModel.isDataReady()) {
                    dataStatusChange(LOAD_SUCCESS);
                    actressAdapter.setLoadFinished(actressViewModel.isLoadFinished());
                    if (actressViewModel.isLoadSuccess()) {
                        actressAdapter.submitList(new ArrayList<>(actors));
                    }
                }
            }
        });
    }
}
