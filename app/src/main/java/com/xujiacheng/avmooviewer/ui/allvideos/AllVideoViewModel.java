package com.xujiacheng.avmooviewer.ui.allvideos;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class AllVideoViewModel extends BaseViewModel {
    MutableLiveData<ArrayList<Av>> allAvData;
    private static final String ALL_VID_URL = "https://avmask.com/cn/page/%s";


    public AllVideoViewModel() {
        allAvData = new MutableLiveData<>(new ArrayList<Av>());
    }

    void initAllData() {
        this.currentPage = 1;
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> avList = new ArrayList<>();
                String html = internetRequest.getHTML(String.format(ALL_VID_URL, currentPage));
                if (html != null) {
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
                allAvData.postValue(avList);
            }
        });

    }

    void loadMore() {
        this.currentPage++;
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> data = allAvData.getValue();
                String html = internetRequest.getHTML(String.format(ALL_VID_URL, currentPage));
                if (html != null) {
                    ArrayList<Av> avList = Crawler.getAvList(html);
                    if (avList.size() > 0) {
                        setLoadSuccess(true);
                        assert data != null;
                        data.addAll(avList);
                    }
                    if (avList.size() < 30) {
                        setLoadFinished(true);
                    }
                } else {
                    setLoadSuccess(false);
                }
                setDataReady(true);
                allAvData.postValue(data);
            }
        });
    }

}
