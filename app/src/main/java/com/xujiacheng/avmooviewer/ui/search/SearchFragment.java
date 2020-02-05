package com.xujiacheng.avmooviewer.ui.search;


import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends ShowAvsBaseFragment {
    private static String queryString;
    private SearchViewModel mViewModel;


    public SearchFragment(String queryString) {
        SearchFragment.queryString = queryString;
    }

    private static final String TAG = "SearchFragment";
    @Override
    public boolean isHomeFragment() {
        return false;
    }

    @Override
    public boolean useDefaultConfig() {
        return true;
    }

    @Override
    public void uiOperation() {
        mToolbar.setTitle(getString(R.string.search_for) + queryString);
        if (mViewModel.queryString == null || !mViewModel.queryString.equals(queryString) || Objects.requireNonNull(mViewModel.mAvListData.getValue()).size() == 0) {
            Log.d(TAG, "uiOperation: request data");
            mViewModel.queryString = queryString;
            mViewModel.mAvListData.setValue(new ArrayList<Av>());
            refreshData();
        } else {
            Log.d(TAG, "uiOperation: no request");
            mViewModel.setDataReady(true);
            mViewModel.setLoadSuccess(true);
//            if (!mViewModel.queryString.equals(queryString)) {
//                mViewModel.queryString = queryString;
//                mViewModel.showAvItems.clear();
//                if (mViewModel.mAvListData.getValue().size() > 0) {
//                    mViewModel.setDataReady(true);
//                    mViewModel.setLoadSuccess(true);
//                    mViewModel.mAvListData.setValue(new ArrayList<Av>());
//                }
//                mViewModel.refreshAvData();
//            } else {
//                if (Objects.requireNonNull(mViewModel.mAvListData.getValue()).size() > 0) {
//                    mViewModel.setDataReady(true);
//                    mViewModel.setLoadSuccess(true);
//                } else {
//                    refreshData();
//                }
//            }
        }
    }

    @Override
    public BaseViewModel setViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        return mViewModel;
    }

}
