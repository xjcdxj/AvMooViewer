package com.xujiacheng.avmooviewer.ui.base;

import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.utils.InternetRequest;

public abstract class BaseViewModel extends ViewModel {

    protected final InternetRequest internetRequest;
    private boolean isLoadFinished = false;
    private boolean isDataReady = false;
    private boolean isLoadSuccess = true;
    public int currentPage = 1;


    protected BaseViewModel() {
        internetRequest = new InternetRequest();
    }


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
