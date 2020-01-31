package com.xujiacheng.avmooviewer.ui.allvideos;

import androidx.lifecycle.MutableLiveData;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class AllVideoViewModel extends BaseViewModel {
    private static final String ALL_VID_URL = "https://avmask.com/cn/page/%s";

    @Override
    public String requestAvDataURL() {
        this.currentPage = 1;
        return String.format(ALL_VID_URL, currentPage);
    }

    @Override
    public String loadMoreAvDataURL() {
        this.currentPage++;
        return String.format(ALL_VID_URL, currentPage);
    }

}
