package com.xujiacheng.avmooviewer.ui.actress.actressvideos;

import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;

public class ActressVideoViewModel extends BaseViewModel {
     String mAvDataURL;

    public ActressVideoViewModel() {
    }
    @Override
    public String requestAvDataURL() {
        currentPage = 1;
        return String.format(mAvDataURL, currentPage);
    }

    @Override
    public String loadMoreAvDataURL() {
        currentPage++;
        return String.format(mAvDataURL, currentPage);
    }
}
