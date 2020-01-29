package com.xujiacheng.avmooviewer.ui.allvideos;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.BaseAdapter;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;
import com.xujiacheng.avmooviewer.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.Objects;

public class AllAvFragment extends ShowAvsBaseFragment {
    private static final String TAG = "AllAvFragment";
    private AllVideoViewModel allVideoViewModel;

    @Override
    public boolean isHomeFragment() {
        return true;
    }

    @Override
    public void uiOperation() {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case BaseAdapter.LOAD_MORE:
                        AllAvFragment.this.allVideoViewModel.loadMore();
                        break;
                    case BaseAdapter.ITEM_CLICK:
                        break;
//                        String url = (String) msg.obj;
//                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                        transaction.addToBackStack(null);
//                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
//                        transaction.replace(R.id.container, new DetailFragment(url));
//                        transaction.commit();
                }
                return false;
            }
        });
        final AllVideosAdapter adapter = new AllVideosAdapter(handler);
        this.allVideoViewModel = new ViewModelProvider(requireActivity()).get(AllVideoViewModel.class);
//        this.allVideoViewModel = ViewModelProviders.of(requireActivity()).get(AllVideoViewModel.class);
        toolbar.setTitle("All Videos");
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
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        dataStatusChange(REFRESH_DATA);
                        allVideoViewModel.initAllData();
                        recyclerView.scrollToPosition(0);
                        break;
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
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment fragment = new SearchFragment(query);
//                ObjectAnimator.ofFloat(fragment,"translationX",0,200)
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
        this.allVideoViewModel.allAvData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                switch (AllAvFragment.this.allVideoViewModel.getResponseStatus()) {
                    case AllVideoViewModel.DATA_READY:
                        dataStatusChange(LOAD_SUCCESS);
//                        recyclerView.setVisibility(View.VISIBLE);
                        break;
                    case AllVideoViewModel.RESPONSE_ERROR:
                        Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        dataStatusChange(LOAD_FAILED);
                        break;
                }
                if (avs.size() > 0) {
                    loadingData.setVisibility(View.GONE);
                }
                adapter.submitList(new ArrayList<>(avs));

                AllAvFragment.this.allVideoViewModel.setResponseStatus(0);
            }
        });
        if (Objects.requireNonNull(this.allVideoViewModel.allAvData.getValue()).size() == 0) {
            Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show();
            dataStatusChange(REFRESH_DATA);
            this.allVideoViewModel.initAllData();
        }


    }


}
