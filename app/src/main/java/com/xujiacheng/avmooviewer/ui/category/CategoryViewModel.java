package com.xujiacheng.avmooviewer.ui.category;

import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;

/*
https://avmask.com/cn/genre/ce53dfd4157de35c/page/2
 */
public class CategoryViewModel extends BaseViewModel {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String requestAvDataURL() {
        currentPage = 1;
        return String.format(url, currentPage);
    }

    @Override
    public String loadMoreAvDataURL() {
        currentPage++;
        return String.format(url, currentPage);
    }
    // TODO: Implement the ViewModel
}
