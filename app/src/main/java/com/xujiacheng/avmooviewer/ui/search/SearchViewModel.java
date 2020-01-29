package com.xujiacheng.avmooviewer.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private int responseStatus;
    public final static int DATA_READY = 1;
    public final static int RESPONSE_ERROR = 2;
    public final static int NO_MORE = 3;

    public SearchViewModel() {
        this.searchResult = new MutableLiveData<>(new ArrayList<Av>());
        internetRequest = new InternetRequest();
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    private int currentPage = 1;
    private static final String TAG = "SearchViewModel";
    private final InternetRequest internetRequest;

    MutableLiveData<ArrayList<Av>> searchResult;
    private static final String SEARCH_URL = "https://avmask.com/cn/search/%s/page/%s";
     String queryString;

    public void search(final String queryString) {
        currentPage = 1;
        this.queryString = queryString;
        RunningTask.addTask(new Runnable() {


            @Override
            public void run() {
                ArrayList<Av> avList = new ArrayList<>();
                String html = internetRequest.getHTML(String.format(SEARCH_URL, queryString, currentPage));
                if (html != null) {
                    avList = Crawler.getAvList(html);
                    setResponseStatus(DATA_READY);
                    if (avList.size() < 30) {
                        setResponseStatus(NO_MORE);
                    }
                } else {
                    setResponseStatus(RESPONSE_ERROR);
                }
                searchResult.postValue(avList);
            }
        });
    }

    public void loadMore() {
        currentPage++;
        RunningTask.addTask(new Runnable() {


            @Override
            public void run() {
                ArrayList<Av> value = searchResult.getValue();
                ArrayList<Av> avList = new ArrayList<>();
                String html = internetRequest.getHTML(String.format(SEARCH_URL, queryString, currentPage));
                if (html != null) {
                    avList = Crawler.getAvList(html);
                    if (avList.size() > 0) {
                        value.addAll(avList);
                        setResponseStatus(DATA_READY);
                        if (avList.size() < 30) {
                            setResponseStatus(NO_MORE);
                        }
                    }
                } else {
                    setResponseStatus(RESPONSE_ERROR);
                }
                searchResult.postValue(value);
            }
        });

    }
    // TODO: Implement the ViewModel
}
