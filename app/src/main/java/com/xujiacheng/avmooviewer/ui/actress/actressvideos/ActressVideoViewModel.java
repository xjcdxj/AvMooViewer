package com.xujiacheng.avmooviewer.ui.actress.actressvideos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class ActressVideoViewModel extends BaseViewModel {
    private static final String TAG = "ActressVideoViewModel";
    static String mActressURL;
    MutableLiveData<String> actressName;

    public ActressVideoViewModel() {
        actressName = new MutableLiveData<>("");
    }
    @Override
    public String requestAvDataURL() {
        currentPage = 1;
        return String.format(mActressURL, currentPage);
    }

    @Override
    public String loadMoreAvDataURL() {
        currentPage++;
        return String.format(mActressURL, currentPage);
    }
}
