package com.xujiacheng.avmooviewer.ui.collections;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.utils.Collections;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.util.ArrayList;

public class CollectionsViewModel extends ViewModel {
     MutableLiveData<ArrayList<Av>> collections;
    private static final String TAG = "CollectionsViewModel";

    public CollectionsViewModel() {
        collections = new MutableLiveData<>(new ArrayList<Av>());
    }

    void getCollections() {
        RunningTask.addTask(new Runnable() {
            @Override
            public void run() {
                ArrayList<Av> existingCollections = Collections.getExistingCollections();
                Log.d(TAG, "run: collections size = "+existingCollections.size());
                collections.postValue(existingCollections);
            }
        });
    }
// TODO: Implement the ViewModel
}
