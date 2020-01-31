package com.xujiacheng.avmooviewer.ui.search;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class SearchViewModel extends BaseViewModel {
    private static final String TAG = "SearchViewModel";


    private static final String SEARCH_URL = "https://avmask.com/cn/search/%s/page/%s";
    String queryString;

//    void search(final String queryString) {
//        currentPage = 1;
//        this.queryString = queryString;
//        RunningTask.addTask(new Runnable() {
//
//
//            @Override
//            public void run() {
//                ArrayList<Av> avList = new ArrayList<>();
//                String html = internetRequest.getHTML(String.format(SEARCH_URL, queryString, currentPage));
//                if (html != null) {
//                    avList = Crawler.getAvList(html);
//                    Log.d(TAG, "run: size = " + avList.size());
//                    if (avList.size() > 0) {
//                        setLoadSuccess(true);
//                    }
//                    if (avList.size() < 30) {
//                        setLoadFinished(true);
//                    }
//                } else {
//                    setLoadSuccess(false);
//                }
//                setDataReady(true);
//                mAvLiveData.postValue(avList);
//            }
//        });
//    }
//
//    void loadMore() {
//        currentPage++;
//        RunningTask.addTask(new Runnable() {
//
//
//            @Override
//            public void run() {
//                ArrayList<Av> value = mAvLiveData.getValue();
//
//                String html = internetRequest.getHTML(String.format(SEARCH_URL, queryString, currentPage));
//                if (html != null) {
//                    ArrayList<Av> avList = Crawler.getAvList(html);
//                    if (avList.size() > 0) {
//                        setLoadSuccess(true);
//                        assert value != null;
//                        value.addAll(avList);
//                    }
//                    if (avList.size() < 30) {
//                        setLoadFinished(true);
//                    }
//                } else {
//                    setLoadSuccess(false);
//                }
//                setDataReady(true);
//                mAvLiveData.postValue(value);
//            }
//        });
//
//    }

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
