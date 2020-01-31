package com.xujiacheng.avmooviewer.ui.collections;

import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;

public class CollectionsFragment extends ShowAvsBaseFragment {
    private static final String TAG = "CollectionsFragment";

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
        dataStatusChange(LOAD_SUCCESS);
        mToolbar.setTitle(getString(R.string.collections));
        CollectionsViewModel mViewModel = new ViewModelProvider(requireActivity()).get(CollectionsViewModel.class);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        final CollectionsAdapter collectionsAdapter = new CollectionsAdapter();
        final CollectionItemAdapter collectionItemAdapter = new CollectionItemAdapter(mViewModel.showCollections);
        mRecyclerView.setAdapter(collectionItemAdapter);
        mViewModel.collections.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                Log.d(TAG, "onChanged: collections size = " + avs.size());
                collectionItemAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.getCollections();
    }


    @Override
    public BaseViewModel setViewModel() {
        return null;
    }

}
