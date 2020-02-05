package com.xujiacheng.avmooviewer.ui.category.categorylist;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Category;
import com.xujiacheng.avmooviewer.utils.Crawler;
import com.xujiacheng.avmooviewer.utils.InternetRequest;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class ViewPagerCategoryViewModel extends ViewModel {
    MutableLiveData<ArrayList<ArrayList<Category>>> categoriesLiveData;
    private static final String url = "https://avmask.com/cn/genre";
    private static final String TAG = "ViewPagerCategoryViewMo";
    //      boolean isCategoryLoad;
    boolean isLoadSuccess = false;
    ArrayList<String> categoryTabs;


    public ViewPagerCategoryViewModel() {
        categoriesLiveData = new MutableLiveData<>(new ArrayList<ArrayList<Category>>());
//        isCategoryLoad = false;
//        categoryTabs=new ArrayList<>();
    }

    void getCategoryData() {
        RunningTask.addTask(new Runnable() {

            private ArrayList<ArrayList<Category>> categories = new ArrayList<>();

            @Override
            public void run() {
                InternetRequest internetRequest = InternetRequest.getInstance();
                String html = internetRequest.getHTML(url);
                if (html != null) {
                    categoryTabs = Crawler.getCategoryTabs(html);
                    Log.d(TAG, "run: tabs = " + categoryTabs);
                    categories = Crawler.getCategories(html);
                    Log.d(TAG, "run: size = " + categories.size());
                    for (ArrayList<Category> arrayList : categories) {
                        Log.d(TAG, "run: first = " + arrayList.get(0).name);
                    }
//                    isCategoryLoad = true;
                    isLoadSuccess = true;

                } else {
                    isLoadSuccess = false;
                }
                categoriesLiveData.postValue(categories);
            }
        });
    }
    // TODO: Implement the ViewModel
}
