package com.xujiacheng.avmooviewer.ui.actress.actressvideos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class ActressVideoViewModel extends BaseViewModel {
    private static final String TAG = "ActressVideoViewModel";
    MutableLiveData<ArrayList<Av>> actressAv;
    static String actressURL;
    MutableLiveData<String> actressName;


    public ActressVideoViewModel() {
        actressAv = new MutableLiveData<>(new ArrayList<Av>());
        actressName = new MutableLiveData<>("");
    }

    void initActressVideos() {
        currentPage = 1;
        final String url = String.format(actressURL, currentPage);
        Log.d(TAG, "initActressVideos: init actresses" + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(url);
                ArrayList<Av> avList = new ArrayList<>();
                if (html != null) {
                    actressName.postValue(Crawler.getActressName(html));
                    avList = Crawler.getAvList(html);
                    if (avList.size() > 0) {
                        setLoadSuccess(true);
                    }
                    if (avList.size() < 30) {
                        setLoadFinished(true);
                    }
                } else {
                    setLoadSuccess(false);
                }
                setDataReady(true);
                actressAv.postValue(avList);
            }
        });
    }

    void loadMore() {
        currentPage++;
        final String url = String.format(actressURL, currentPage);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> value = actressAv.getValue();
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    ArrayList<Av> avList = Crawler.getAvList(html);
                    if (avList.size() > 0) {
                        setLoadSuccess(true);
                    }
                    if (avList.size() < 30) {
                        setLoadFinished(true);
                    }
                    assert value != null;
                    value.addAll(avList);
                }
                setDataReady(true);
                actressAv.postValue(value);
            }
        });
    }
}
