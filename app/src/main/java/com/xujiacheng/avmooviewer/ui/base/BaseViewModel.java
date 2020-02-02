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
//    public ArrayList<Av> showAvItems;
    public MutableLiveData<ArrayList<Av>> mAvListData;//获取到的数据存放


    protected BaseViewModel() {
        internetRequest = InternetRequest.getInstance();
//        showAvItems = new ArrayList<>();
        mAvListData = new MutableLiveData<>(new ArrayList<Av>());
    }

    void refreshAvData() {
        final String url = requestAvDataURL();
        Log.d(TAG, "loadMoreAvData: request url = " + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> value = requestAvData(url);
                mAvListData.postValue(value);
            }
        });
    }

    private ArrayList<Av> requestAvData(String url) {
        initStatus();
        setLoadFinished(false);
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
            //获取网页失败，要将页数回调
            currentPage--;
            setLoadSuccess(false);
        }
        setDataReady(true);
        return avList;
    }

    void loadMoreAvData() {
        initStatus();
        final String url = loadMoreAvDataURL();
        Log.d(TAG, "loadMoreAvData: load url = " + url);
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> value = mAvListData.getValue();
                assert value != null;
                value.addAll(requestAvData(url));

//                ArrayList<Av> data = mAvListData.getValue();
//                String html = internetRequest.getHTML(url);
//                if (html != null) {
//                    ArrayList<Av> avList = Crawler.getAvList(html);
//                    if (avList.size() > 0) {
//                        setLoadSuccess(true);
//                        assert data != null;
//                        data.addAll(avList);
//                    }
//                    if (avList.size() < 30) {
//                        setLoadFinished(true);
//                    }
//                } else {
//                    setLoadSuccess(false);
//                }
//                setDataReady(true);
                mAvListData.postValue(value);
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

    private void initStatus() {
        setDataReady(false);
        setLoadSuccess(false);
    }

    public boolean isLoadFinished() {
        return isLoadFinished;
    }

    public void setLoadFinished(boolean loadFinished) {
        this.isLoadFinished = loadFinished;
    }

}
