package com.xujiacheng.avmooviewer.ui.allvideos;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.ui.base.BaseViewModel;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;
import com.xujiacheng.avmooviewer.ui.search.SearchFragment;

import java.util.Objects;

public class AllAvFragment extends ShowAvsBaseFragment {
    private static final String TAG = "AllAvFragment";
    private AllVideoViewModel mViewModel;

    public AllAvFragment() {

    }

    @Override
    public boolean isHomeFragment() {
        return true;
    }

    @Override
    public boolean useDefaultConfig() {
        return true;
    }

    @Override
    public void uiOperation() {
        mToolbar.inflateMenu(R.menu.search);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                closeKeyBoard();
                return false;
            }
        });
        mToolbar.setTitle(getString(R.string.home));
        mToolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
                return false;
            }
        });
        final SearchView mSearchView = (SearchView) mToolbar.getMenu()
                .findItem(R.id.menu_search)
                .getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                SearchFragment fragment = new SearchFragment(query);
                MainActivity.changeFragment(fragment, false);
                mSearchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        if (Objects.requireNonNull(this.mViewModel.mAvListData.getValue()).size() == 0) {
            refreshData();
        } else {
            mViewModel.setDataReady(true);
            mViewModel.setLoadSuccess(true);
        }
    }

    @Override
    public BaseViewModel setViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(AllVideoViewModel.class);
        return mViewModel;
    }

    @Override
    public void onStop() {

        super.onStop();
        closeKeyBoard();
    }
}
