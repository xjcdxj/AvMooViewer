package com.xujiacheng.avmooviewer.ui.category;

import androidx.lifecycle.ViewModelProvider;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryFragment extends ShowAvsBaseFragment {
    private String categoryURL;
    private String categoryName;


//    private static final String TAG = "CategoryFragment";
    private CategoryViewModel mViewModel;

    public CategoryFragment(String categoryURL, String categoryName) {
        this.categoryURL = categoryURL + "/page/%s";
        this.categoryName = categoryName;
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
        if (categoryName != null) {
            mToolbar.setTitle(categoryName);
        }
        if (mViewModel.getUrl()==null
                ||!mViewModel.getUrl().equals(this.categoryURL)
                || Objects.requireNonNull(mViewModel.mAvListData.getValue()).size()==0){
            mViewModel.setUrl(this.categoryURL);
            mViewModel.mAvListData.setValue(new ArrayList<Av>());
            refreshData();
        }else {
            mViewModel.setLoadSuccess(true);
            mViewModel.setDataReady(true);
        }

//        if (mViewModel.getUrl() == null) {
//            mViewModel.setUrl(this.categoryURL);
//            mViewModel.refreshAvData();
//        }
//        if (!mViewModel.getUrl().equals(this.categoryURL)) {
//            mViewModel.setUrl(this.categoryURL);
//            refreshData();
//        } else {
//            if (mViewModel.mAvListData.getValue().size() == 0) {
//                refreshData();
//            } else {
//                mViewModel.setDataReady(true);
//                mViewModel.setLoadSuccess(true);
//            }
//        }
    }

    @Override
    public BaseViewModel setViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        return mViewModel;
    }
}
