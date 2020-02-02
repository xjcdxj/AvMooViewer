package com.xujiacheng.avmooviewer.ui.actress.actressvideos;

import androidx.lifecycle.ViewModelProvider;

import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.Objects;

public class ActressesVideosFragment extends ShowAvsBaseFragment {
//    private static final String TAG = "ActressesVideosFragment";
    private String avDataURL;
    private ActressVideoViewModel mViewModel;
    private String actressName;

    public ActressesVideosFragment(String avDataURL, String actressName) {
        this.avDataURL = avDataURL + "/page/%s";
        this.actressName = actressName;
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
        if (actressName != null) {
            mToolbar.setTitle(actressName);
        } else {
            mToolbar.setTitle(this.avDataURL);
        }
        if (mViewModel.mAvDataURL == null
                || !mViewModel.mAvDataURL.equals(this.avDataURL)
                || Objects.requireNonNull(mViewModel.mAvListData.getValue()).size() == 0) {
            mViewModel.mAvDataURL = avDataURL;
            refreshData();
        } else {
            mViewModel.setDataReady(true);
            mViewModel.setLoadSuccess(true);
        }
    }

    @Override
    public BaseViewModel setViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(ActressVideoViewModel.class);
        return mViewModel;
    }
}
