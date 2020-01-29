package com.xujiacheng.avmooviewer.ui.search;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseAdapter;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;

import java.util.ArrayList;

public class SearchFragment extends ShowAvsBaseFragment {
    private static final String TAG = "SearchFragment";
    public static String queryString;
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
        if (searchViewModel.queryString == null || searchViewModel.searchResult.getValue().size() == 0) {
            searchViewModel.search(queryString);
        }else {
            searchViewModel.setResponseStatus(SearchViewModel.DATA_READY);
        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case BaseAdapter.LOAD_MORE:
                        searchViewModel.loadMore();
                        break;
                }
                return false;
            }
        });
        final SearchAdapter searchAdapter = new SearchAdapter(handler);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchViewModel.searchResult.observe(this, new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {

                switch (searchViewModel.getResponseStatus()) {
                    case SearchViewModel.DATA_READY:
                        loadFailed.setVisibility(View.GONE);
                        loadingData.setVisibility(View.GONE);
                        break;
                    case SearchViewModel.RESPONSE_ERROR:
                        Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        loadingData.setVisibility(View.GONE);
                        loadFailed.setVisibility(View.VISIBLE);
                        break;
                    case SearchViewModel.NO_MORE:
                        searchAdapter.setLoadFinish(true);
                }
                if (avs.size() > 0) {
                    loadingData.setVisibility(View.GONE);
                }
                searchViewModel.setResponseStatus(0);
                searchAdapter.submitList(new ArrayList<Av>(avs));
            }
        });
    }

}
