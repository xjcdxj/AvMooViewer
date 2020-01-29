package com.xujiacheng.avmooviewer.ui.search;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseAdapter;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends ShowAvsBaseFragment {
    private static String queryString;
    private SearchViewModel searchViewModel;


    public SearchFragment(String queryString) {
        SearchFragment.queryString = queryString;
    }

    @Override
    public boolean isHomeFragment() {
        return false;
    }

    @Override
    public void uiOperation() {

        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        toolbar.setTitle(getString(R.string.search_for) + queryString);
        if (searchViewModel.queryString == null || Objects.requireNonNull(searchViewModel.searchResult.getValue()).size() == 0) {
            searchViewModel.search(queryString);
        } else {
            searchViewModel.setDataReady(true);
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == BaseAdapter.LOAD_MORE) {
                    searchViewModel.loadMore();
                }
                return false;
            }
        });
        final BaseAdapter searchAdapter = new BaseAdapter(handler);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchViewModel.searchResult.observe(this, new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                if (avs.size() > 0) {
                    dataStatusChange(LOAD_SUCCESS);
                }
                if (searchViewModel.isDataReady()) {
                    searchAdapter.setLoadFinished(searchViewModel.isLoadFinished());
                    if (!searchViewModel.isLoadSuccess()) {
                        if (avs.size() == 0) {
                            dataStatusChange(LOAD_FAILED);
                        }
                    }
                }

                searchAdapter.submitList(new ArrayList<>(avs));
                searchViewModel.initStatus();
            }
        });
    }

}
