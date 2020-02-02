package com.xujiacheng.avmooviewer.ui.detail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Collections;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.Objects;

public class DetailViewModel extends BaseViewModel {
    private static final String TAG = "DetailViewModel";
    public MutableLiveData<Av> av;
    boolean isDataReady = false;
    MutableLiveData<Boolean> isInCollection;


    public DetailViewModel() {
        av = new MutableLiveData<>(new Av());
        isInCollection = new MutableLiveData<>(false);
    }

    @Override
    public String requestAvDataURL() {
        return null;
    }

    @Override
    public String loadMoreAvDataURL() {
        return null;
    }

    void loadAvInfo(final String url) {
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

    void addToFavorite() {

        if (!Objects.requireNonNull(isInCollection.getValue())) {
            RunningTask.addTask(new Runnable() {
                @Override
                public void run() {
                    Av value = av.getValue();
                    if (value != null) {
                        Collections.addToCollection(value);
                        checkCollections(value.url);
                    }
                }
            });
        } else {
            RunningTask.addTask(new Runnable() {
                @Override
                public void run() {
                    Av value = av.getValue();
                    if (value != null) {
                        boolean b = Collections.removeCollection(value.url);
                        checkCollections(value.url);
                    }
                }
            });
        }
    }

    void checkCollections(final String url) {
        Log.d(TAG, "checkCollections: url = " + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                isInCollection.postValue(Collections.checkCollections(url));
            }
        });
    }
}
