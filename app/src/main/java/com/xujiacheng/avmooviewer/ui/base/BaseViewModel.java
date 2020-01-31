package com.xujiacheng.avmooviewer.ui.base;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public abstract class BaseViewModel extends ViewModel {
    private static final String TAG = "BaseViewModel";
    protected final InternetRequest internetRequest;
    private boolean isLoadFinished = false;
    private boolean isDataReady = false;
    private boolean isLoadSuccess = true;
    public int currentPage = 1;
    public ArrayList<Av> showAvItems;
    public MutableLiveData<ArrayList<Av>> mAvLiveData;


    protected BaseViewModel() {
        internetRequest = InternetRequest.getInstance();
        showAvItems = new ArrayList<>();
        mAvLiveData = new MutableLiveData<>(new ArrayList<Av>());
    }

    public void requestAvData() {
        final String url = requestAvDataURL();
        Log.d(TAG, "loadMoreAvData: request url = " + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> avList = new ArrayList<>();
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    avList = Crawler.getAvList(html);
                    setLoadSuccess(true);
                    if (avList.size() < 30) {
                        setLoadFinished(true);
                    } else {
                        setLoadFinished(false);
                    }
                } else {
                    setLoadSuccess(false);
                }
                setDataReady(true);
                mAvLiveData.postValue(avList);
            }
        });
    }

    public void loadMoreAvData() {
        final String url = loadMoreAvDataURL();
        Log.d(TAG, "loadMoreAvData: load url = " + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {

                ArrayList<Av> data = mAvLiveData.getValue();
                String html = internetRequest.getHTML(url);
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
                mAvLiveData.postValue(data);
            }
        });
    }

    public abstract String requestAvDataURL();

    public abstract String loadMoreAvDataURL();

    public boolean isDataReady() {
        return isDataReady;
    }

    public void setDataReady(boolean dataReady) {
        isDataReady = dataReady;
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        isLoadSuccess = loadSuccess;
    }

    public void initStatus() {
        setDataReady(false);
        setLoadFinished(false);
        setLoadFinished(false);
    }

    public boolean isLoadFinished() {
        return isLoadFinished;
    }

    public void setLoadFinished(boolean loadFinished) {
        this.isLoadFinished = loadFinished;
    }

}
