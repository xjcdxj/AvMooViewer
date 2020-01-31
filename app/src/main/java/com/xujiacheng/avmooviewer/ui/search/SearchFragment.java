package com.xujiacheng.avmooviewer.ui.search;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.AvItemAdapter;
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
        if (mViewModel.queryString == null) {
            mViewModel.queryString = queryString;
            refreshData();
        } else {
            if (!mViewModel.queryString.equals(queryString)) {
                mViewModel.queryString = queryString;
                mViewModel.showAvItems.clear();
                if (mViewModel.mAvLiveData.getValue().size()>0){
                    mViewModel.setDataReady(true);
                    mViewModel.setLoadSuccess(true);
                    mViewModel.mAvLiveData.setValue(new ArrayList<Av>());
                }
                mViewModel.requestAvData();
            } else {
                if (Objects.requireNonNull(mViewModel.mAvLiveData.getValue()).size() > 0) {
                    mViewModel.setDataReady(true);
                    mViewModel.setLoadSuccess(true);
                } else {
                    refreshData();
                }
            }
        }
    }

    @Override
    public BaseViewModel setViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        return mViewModel;
    }

}
