package com.xujiacheng.avmooviewer.ui.actress.actressvideos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class ActressVideoViewModel extends BaseViewModel {
    private static final String TAG = "ActressVideoViewModel";
    MutableLiveData<ArrayList<Av>> actressAv;
    private String BASE_URL = "";
    public static String actressURL;
    MutableLiveData<String> actressName;

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String url) {
        this.BASE_URL = url + "/page/%s";
    }

    public boolean isDataReady() {
        return isDataReady;
    }

    public void setDataReady(boolean dataReady) {
        isDataReady = dataReady;
    }

    private boolean isDataReady = false;


    public ActressVideoViewModel() {
        actressAv = new MutableLiveData<>(new ArrayList<Av>());
        actressName = new MutableLiveData<>("");
    }

    public void initActressVideos() {
        currentPage = 1;
        final String url = String.format(actressURL, currentPage);

        Log.d(TAG, "initActressVideos: init actresses"+url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    actressName.postValue(Crawler.getActressName(html));
                    ArrayList<Av> avList = Crawler.getAvList(html);

                    if (avList.size() > 0) {
                        if (avList.size() < 30) {
                            loadingFinished = true;
                        }
                        setDataReady(true);
                        actressAv.postValue(avList);
                    }
                }
            }
        });
    }

    public void loadMore() {
        currentPage++;
        final String url = String.format(actressURL, currentPage);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> value = actressAv.getValue();
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    ArrayList<Av> avList = Crawler.getAvList(html);
                    if (avList.size() < 30) {
                        loadingFinished = true;
                    }
                    setDataReady(true);
                    value.addAll(avList);
                    actressAv.postValue(avList);
                }
                actressAv.postValue(value);
            }
        });
    }
}
