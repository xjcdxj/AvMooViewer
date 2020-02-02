package com.xujiacheng.avmooviewer.ui.collections;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.utils.Collections;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class CollectionsViewModel extends ViewModel {
    MutableLiveData<ArrayList<Av>> collections;
//    private static final String TAG = "CollectionsViewModel";
    ArrayList<Av> showCollections;

    public CollectionsViewModel() {
        collections = new MutableLiveData<>(new ArrayList<Av>());
        showCollections = new ArrayList<>();
    }

    void getCollections() {
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> existingCollections = Collections.getCollections();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showCollections.clear();
                showCollections.addAll(existingCollections);
                collections.postValue(existingCollections);

            }
        });
    }
// TODO: Implement the ViewModel
}
