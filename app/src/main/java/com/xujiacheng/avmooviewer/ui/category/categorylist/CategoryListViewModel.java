package com.xujiacheng.avmooviewer.ui.category.categorylist;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Category;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class CategoryListViewModel extends ViewModel {
    MutableLiveData<ArrayList<Category>> mCategoryList;
    private static final String url = "https://avmask.com/cn/genre";
    private static final String TAG = "CategoryListViewModel";
    boolean isDataReady = false;

    public CategoryListViewModel() {
        mCategoryList = new MutableLiveData<>(new ArrayList<Category>());
    }

    void getCategoryData() {
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                InternetRequest internetRequest = InternetRequest.getInstance();
                Log.d(TAG, "run: url = " + url);
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    Log.d(TAG, "run: response");
                    ArrayList<Category> categoryList = Crawler.getCategoryList(html);
                    isDataReady = true;
                    mCategoryList.postValue(categoryList);
                }
            }
        });

    }
}
