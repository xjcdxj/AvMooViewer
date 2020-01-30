package com.xujiacheng.avmooviewer.ui.collections;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Av;

import java.util.ArrayList;

public class CollectionsFragment extends Fragment {
    private static final String TAG = "CollectionsFragment";


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
        CollectionsViewModel mViewModel = new ViewModelProvider(this).get(CollectionsViewModel.class);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        final CollectionsAdapter collectionsAdapter = new CollectionsAdapter();
        recyclerView.setAdapter(collectionsAdapter);
        mViewModel.collections.observe(getViewLifecycleOwner(), new Observer<ArrayList<Av>>() {
            @Override
            public void onChanged(ArrayList<Av> avs) {
                Log.d(TAG, "onChanged: collections size = " + avs.size());
                collectionsAdapter.submitList(new ArrayList<>(avs));
            }
        });
        mViewModel.getCollections();
        return view;
    }


}
