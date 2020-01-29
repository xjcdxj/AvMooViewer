package com.xujiacheng.avmooviewer.ui.actress;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Message;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.ui.actress.actressvideos.ActressesVideosFragment;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;

public class ActressFragment extends ShowAvsBaseFragment {
    static Handler handler;
    static final int ITEM_CLICK = 1;

    private static final String TAG = "ActressFragment";
    private ActressViewModel actressViewModel;

    @Override
    public boolean isHomeFragment() {
        return true;
    }

    @Override
    public void uiOperation() {
        toolbar.setTitle(getString(R.string.actress));
        ActressFragment.handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case ActressFragment.ITEM_CLICK:

                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.container, new ActressesVideosFragment((String) msg.obj));
                        transaction.commit();
                }
                return false;
            }
        });

        actressViewModel = new ViewModelProvider(requireActivity()).get(ActressViewModel.class);

        final ActressAdapter actressAdapter = new ActressAdapter(actressViewModel);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(actressAdapter);
        if (actressViewModel.actresses.getValue().size() == 0) {
            actressViewModel.initData();
        }
        actressViewModel.actresses.observe(getViewLifecycleOwner(), new Observer<ArrayList<Actor>>() {
            @Override
            public void onChanged(ArrayList<Actor> actors) {
                if (actressViewModel.dataReady) {
                    dataStatusChange(LOAD_SUCCESS);
                    actressAdapter.setLoadFinished(actressViewModel.isFinished);
                    actressAdapter.submitList(new ArrayList<Actor>(actors));
                }
            }
        });

    }


}
