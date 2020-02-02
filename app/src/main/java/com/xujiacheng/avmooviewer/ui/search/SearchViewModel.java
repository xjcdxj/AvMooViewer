package com.xujiacheng.avmooviewer.ui.search;

import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;

public class SearchViewModel extends BaseViewModel {
//    private static final String TAG = "SearchViewModel";


    private static final String SEARCH_URL = "https://avmask.com/cn/search/%s/page/%s";
    String queryString;

    @Override
    public String requestAvDataURL() {
        currentPage = 1;
        return String.format(SEARCH_URL, queryString, currentPage);
    }

    @Override
    public String loadMoreAvDataURL() {
        currentPage++;
        return String.format(SEARCH_URL, queryString, currentPage);
    }
    // TODO: Implement the ViewModel
}
