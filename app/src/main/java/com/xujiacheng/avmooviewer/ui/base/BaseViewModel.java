package com.xujiacheng.avmooviewer.ui.base;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.search.SearchFragment;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public abstract class BaseViewModel extends ViewModel {
    private static final String TAG = "BaseViewModel";
    protected int currentPage = 1;
    protected boolean loadingFinished=false;

    public boolean isLoadingFinished() {
        return loadingFinished;
    }

    public void setLoadingFinished(boolean loadingFinished) {
        this.loadingFinished = loadingFinished;
    }

    protected final InternetRequest internetRequest;

    public BaseViewModel() {
        internetRequest = new InternetRequest();
    }

}
