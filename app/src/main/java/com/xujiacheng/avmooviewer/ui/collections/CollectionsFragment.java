package com.xujiacheng.avmooviewer.ui.collections;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;
import com.xujiacheng.avmooviewer.ui.detail.DetailFragment;

import java.util.ArrayList;

public class CollectionsFragment extends Fragment {
    private static final String TAG = "CollectionsFragment";

    private CollectionsViewModel mViewModel;
    private CollectionsViewModel collectionsViewModel;

    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collections_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.collection_toolbar);
        toolbar.setTitle(getString(R.string.collections));
        RecyclerView recyclerView = view.findViewById(R.id.collection_recycler_view);
        toolbar.setNavigationIcon(R.drawable.white_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        collectionsViewModel = new ViewModelProvider(this).get(CollectionsViewModel.class);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        final CollectionsAdapter collectionsAdapter = new CollectionsAdapter(collectionsViewModel.collections, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == CollectionsAdapter.ITEM_CLICK) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.container, new DetailFragment((String) msg.obj));
                    transaction.commit();
                }
                return false;
            }
        }));
        recyclerView.setAdapter(collectionsAdapter);
        collectionsViewModel.collections.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                Log.d(TAG, "onChanged: collections size = " + avs.size());
                collectionsAdapter.notifyDataSetChanged();
            }
        });
        collectionsViewModel.getCollections();

        return view;
    }




}
