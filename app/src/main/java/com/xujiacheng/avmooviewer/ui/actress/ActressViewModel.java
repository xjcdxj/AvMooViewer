package com.xujiacheng.avmooviewer.ui.actress;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class ActressViewModel extends ViewModel {
    MutableLiveData<ArrayList<Actor>> actresses;
    private final InternetRequest internetRequest;
    private int actressPage = 1;
    private static final String ACTRESS_URL = "https://avmask.com/cn/actresses/page/%s";
    boolean dataReady = false;
    boolean success = false;
    boolean isFinished=false;

    public ActressViewModel() {
        actresses = new MutableLiveData<>(new ArrayList<Actor>());
        internetRequest = new InternetRequest();
    }

    public void loadMoreActress() {
        this.actressPage++;
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(String.format(ACTRESS_URL, actressPage));
                if (html != null) {
                    ArrayList<Actor> data = Crawler.getActresses(html);
                    if (data.size()<50){
                        isFinished=true;
                    }
                    ArrayList<Actor> value = actresses.getValue();
                    value.addAll(data);

                    actresses.postValue(value);

                }
            }
        });

    }

    public void initData() {
        this.actressPage = 1;

        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(String.format(ACTRESS_URL, actressPage));
                ArrayList<Actor> data = new ArrayList<>();
                if (html != null) {
                    data = Crawler.getActresses(html);
                    if (data.size()<50){
                        isFinished=true;
                    }
                    dataReady = true;
                    success = true;

                } else {
                    dataReady = true;
                    success = false;
                }
                actresses.postValue(data);
            }
        });


    }
    // TODO: Implement the ViewModel
}
