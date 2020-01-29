package com.xujiacheng.avmooviewer.ui.allvideos;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class AllVideoViewModel extends ViewModel {
    private static final String TAG = "AllVideoViewModel";
    MutableLiveData<ArrayList<Av>> allAvData;
    private int currentPage = 1;
    private InternetRequest internetRequest;
    private static final String ALL_VID_URL = "https://avmask.com/cn/page/%s";
    private int responseStatus;
    public final static int DATA_READY = 1;
    public final static int RESPONSE_ERROR = 2;
    public final static int NO_MORE = 3;

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public AllVideoViewModel() {
        allAvData = new MutableLiveData<>(new ArrayList<Av>());
        internetRequest = new InternetRequest();
    }

    public void initAllData() {
        this.currentPage = 1;
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> avList = new ArrayList<>();
                String html = internetRequest.getHTML(String.format(ALL_VID_URL, currentPage));
                if (html != null) {
                    avList = Crawler.getAvList(html);
                    if (avList.size() > 0) {
                        setResponseStatus(DATA_READY);
                        if (avList.size() < 30) {
                            setResponseStatus(NO_MORE);
                        }
                    }
                } else {
                    setResponseStatus(RESPONSE_ERROR);
                }
                allAvData.postValue(avList);
            }
        });

    }

    public void loadMore() {
        this.currentPage++;
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> data = allAvData.getValue();
                String html = internetRequest.getHTML(String.format(ALL_VID_URL, currentPage));
                if (html != null) {
                    ArrayList<Av> avList = Crawler.getAvList(html);
                    if (avList.size() > 0) {
                        data.addAll(avList);

                        if (avList.size() < 30) {
                            setResponseStatus(NO_MORE);
                        }
                    }
                } else {
                    setResponseStatus(RESPONSE_ERROR);
                }
                setResponseStatus(DATA_READY);
                allAvData.postValue(data);

            }
        });
    }

}
