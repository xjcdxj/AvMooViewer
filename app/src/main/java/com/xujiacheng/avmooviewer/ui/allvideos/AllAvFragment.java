package com.xujiacheng.avmooviewer.ui.allvideos;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseAdapter;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;
import com.xujiacheng.avmooviewer.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.Objects;

public class AllAvFragment extends ShowAvsBaseFragment {
    private static final String TAG = "AllAvFragment";
    private AllVideoViewModel mViewModel;

    @Override
    public boolean isHomeFragment() {
        return true;
    }

    @Override
    public void uiOperation() {
        this.mViewModel = new ViewModelProvider(requireActivity()).get(AllVideoViewModel.class);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == BaseAdapter.LOAD_MORE) {
                    AllAvFragment.this.mViewModel.loadMore();
                }
                return false;
            }
        });
        final BaseAdapter adapter = new BaseAdapter(handler);
//        this.mViewModel = ViewModelProviders.of(requireActivity()).get(AllVideoViewModel.class);
        toolbar.setTitle(getString(R.string.home));
        toolbar.inflateMenu(R.menu.search);
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                recyclerView.smoothScrollToPosition(0);
                return false;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_refresh) {
                    dataStatusChange(REFRESH_DATA);
                    mViewModel.initAllData();
                    recyclerView.scrollToPosition(0);
                }
                return false;
            }
        });
        Menu menu = toolbar.getMenu();
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                SearchFragment fragment = new SearchFragment(query);
                MainActivity.changeFragment(fragment, false);
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.mViewModel.allAvData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                if (avs.size()>0){
                    dataStatusChange(LOAD_SUCCESS);
                }
                if (mViewModel.isDataReady()) {
                    adapter.setLoadFinished(mViewModel.isLoadFinished());
                    adapter.setLoadSuccess(mViewModel.isLoadSuccess());
                    Log.d(TAG, "onChanged: response = " + mViewModel.isLoadSuccess());
                    if (!mViewModel.isLoadSuccess()) {
                        if (avs.size() == 0) {
                            dataStatusChange(LOAD_FAILED);
                        }
                    }
                }
                adapter.submitList(new ArrayList<>(avs));
                mViewModel.initStatus();
            }
        });
        if (Objects.requireNonNull(this.mViewModel.allAvData.getValue()).size() == 0) {
            dataStatusChange(REFRESH_DATA);
            this.mViewModel.initAllData();
        }
    }


}
