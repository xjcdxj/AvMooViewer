package com.xujiacheng.avmooviewer.ui.detail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.utils.Collections;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

public class DetailViewModel extends ViewModel {
    private static final String TAG = "DetailViewModel";
    public MutableLiveData<Av> av;
    boolean isDataReady = false;
    MutableLiveData<Boolean> isInCollection;
    private final InternetRequest internetRequest;

    public DetailViewModel() {
        av = new MutableLiveData<>(new Av());
        internetRequest = new InternetRequest();
        isInCollection = new MutableLiveData<>(false);
    }

    public void loadAvInfo(final String url) {
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    Av avInformation = Crawler.getAvInformation(html);
                    isDataReady = true;
                    avInformation.url = url;
                    av.postValue(avInformation);
                }
            }
        });
    }

    public void addToFavorite() {
        if (!isInCollection.getValue()) {
            RunningTask.addTask(new Runnable() {
                @Override
                public void run() {
                    boolean b = Collections.addToCollection(av.getValue());
                    checkCollections(av.getValue().url);
                }
            });
        } else {
            RunningTask.addTask(new Runnable() {
                @Override
                public void run() {
                    boolean b = Collections.removeCollection(av.getValue().url);
                    checkCollections(av.getValue().url);
                }
            });
        }
    }

    public void checkCollections(final String url) {
        Log.d(TAG, "checkCollections: url = " + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                Av av = Collections.checkCollections(url);
//                isDataReady = true;
//                DetailViewModel.this.av.postValue(av);
                if (av != null) {
                    isInCollection.postValue(true);
                } else {
                    isInCollection.postValue(false);
                }
            }
        });
    }
}
