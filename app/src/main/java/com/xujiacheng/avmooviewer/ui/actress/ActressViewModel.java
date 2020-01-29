package com.xujiacheng.avmooviewer.ui.actress;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class ActressViewModel extends BaseViewModel {
    MutableLiveData<ArrayList<Actor>> actresses;
    private static final String ACTRESS_URL = "https://avmask.com/cn/actresses/page/%s";

    public ActressViewModel() {
        actresses = new MutableLiveData<>(new ArrayList<Actor>());

    }

    void loadMoreActress() {
        this.currentPage++;
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(String.format(ACTRESS_URL, currentPage));
                ArrayList<Actor> value = actresses.getValue();
                if (html != null) {
                    ArrayList<Actor> data = Crawler.getActresses(html);
                    if (data.size() > 0) {
                        setLoadSuccess(true);
                    }
                    if (data.size() < 50) {
                        setLoadFinished(true);
                    }
                    assert value != null;
                    value.addAll(data);
                } else {
                    setLoadSuccess(false);
                }
                setDataReady(true);
                actresses.postValue(value);
            }
        });

    }

    void initData() {
        this.currentPage = 1;

        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(String.format(ACTRESS_URL, currentPage));
                ArrayList<Actor> data = new ArrayList<>();
                if (html != null) {
                    data = Crawler.getActresses(html);
                    if (data.size() > 0) {
                        setLoadSuccess(true);
                    }
                    if (data.size() < 50) {
                        setLoadFinished(true);
                    }
                } else {
                    setLoadSuccess(false);
                }
                setDataReady(true);
                actresses.postValue(data);
            }
        });


    }
    // TODO: Implement the ViewModel
}
