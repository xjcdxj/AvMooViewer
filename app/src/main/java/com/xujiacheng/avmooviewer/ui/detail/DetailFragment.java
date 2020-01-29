package com.xujiacheng.avmooviewer.ui.detail;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.FrameLayoutBindingAdapter;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.itembean.Info;
import com.xujiacheng.avmooviewer.ui.actress.actressvideos.ActressesVideosFragment;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    private String url;


    private DetailViewModel mViewModel;
    private DetailViewModel detailViewModel;


    public DetailFragment() {
    }

    public DetailFragment(String url) {
        this.url = url;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        detailViewModel.checkCollections(this.url);

        final Toolbar toolbar = view.findViewById(R.id.detail_toolbar);
        toolbar.inflateMenu(R.menu.favorite);
        final ImageView cover = view.findViewById(R.id.item_detail_cover);
        final ProgressBar coverLoading = view.findViewById(R.id.item_detail_cover_loading);
        final TextView name = view.findViewById(R.id.item_detail_name);
        final TextView id = view.findViewById(R.id.item_detail_id);
        final TextView date = view.findViewById(R.id.item_detail_date);
        final ProgressBar detailLoading = view.findViewById(R.id.detail_loading);
        final ScrollView detailScroll = view.findViewById(R.id.detail_scroll_view);
        final GridView actressGridView = view.findViewById(R.id.item_actresses_grid_view);
        final LinearLayout detailContainer = view.findViewById(R.id.detail_container);
        final MenuItem addToCollectionMenu = toolbar.getMenu().findItem(R.id.menu_favorite);
        toolbar.setTitle(this.url);
        addToCollectionMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (detailViewModel.av.getValue().url == null) {
                    Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show();
                } else {
                    detailViewModel.addToFavorite();
                }
                return false;
            }
        });


        toolbar.setNavigationIcon(R.drawable.white_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        detailViewModel.isInCollection.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    addToCollectionMenu.setIcon(R.drawable.ic_star_black_24dp);
                } else {
                    addToCollectionMenu.setIcon(R.drawable.ic_star_border_black_24dp);


                }
            }
        });
        detailViewModel.loadAvInfo(DetailFragment.this.url);
        detailViewModel.av.observe(getViewLifecycleOwner(), new Observer<Av>() {
            @Override
            public void onChanged(Av av) {
                if (detailViewModel.isDataReady) {
                    Log.d(TAG, "onChanged: big cover" + av.bigCoverURL);
                    detailLoading.setVisibility(View.GONE);
                    detailScroll.setVisibility(View.VISIBLE);
                    if (av.bigCoverImage != null) {
                        cover.setImageBitmap(av.bigCoverImage);
                        Log.d(TAG, "onChanged: load from file");
                    } else {
                        Glide.with(DetailFragment.this)
                                .asBitmap()
                                .load(av.bigCoverURL)
                                .placeholder(R.drawable.avmooviewer)

                                .listener(new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        coverLoading.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                        detailViewModel.av.getValue().bigCoverImage = resource;
                                        coverLoading.setVisibility(View.GONE);
                                        return false;
                                    }
                                })

                                .into(cover);
                    }
                    name.setText(av.name);
                    toolbar.setTitle(av.name);
                    id.setText(av.id);
                    toolbar.setSubtitle(av.id);
                    date.setText(av.releaseDate);
                    if (av.actors != null && av.actors.size() > 0) {
                        actressGridView.setAdapter(new ActressAdapter(av.actors));
                    } else {
                        actressGridView.setVisibility(View.GONE);
                    }
                    if (av.previewURL != null && av.previewURL.size() > 0) {
                        for (String url : av.previewURL) {
                            ImageView imageView = new ImageView(requireContext());
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            imageView.setPadding(4, 4, 4, 4);
                            imageView.setAdjustViewBounds(true);
                            if (detailViewModel.av.getValue().previewImage != null && detailViewModel.av.getValue().previewImage.size() == av.previewURL.size()) {
                                Log.d(TAG, "onChanged: load from file");
                                for (Bitmap bitmap : detailViewModel.av.getValue().previewImage) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            } else {
                                Glide.with(DetailFragment.this)
                                        .asBitmap()
                                        .load(url)
                                        .listener(new RequestListener<Bitmap>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                                detailViewModel.av.getValue().previewImage.add(resource);
                                                return false;
                                            }
                                        })
                                        .into(imageView);
                            }
                            detailContainer.addView(imageView);
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    class ActressAdapter extends BaseAdapter {

        private ArrayList<Info> mActresses;
        private final LayoutInflater inflater;
        private ImageView header;
        private TextView name;

        public ActressAdapter(ArrayList<Info> mActresses) {
            this.mActresses = mActresses;
            inflater = LayoutInflater.from(requireContext());
        }

        @Override
        public int getCount() {
            return mActresses.size();
        }

        @Override
        public Info getItem(int position) {
            return mActresses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.item_actress, null, false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.container, new ActressesVideosFragment(getItem(position).url));
                    transaction.commit();
                }
            });
            ActressViewHolder actressViewHolder = new ActressViewHolder(convertView);
            header = convertView.findViewById(R.id.item_actress_header);
            Glide.with(DetailFragment.this)
                    .asBitmap()
                    .load(getItem(position).imageURL)
                    .transition(withCrossFade())
                    .placeholder(R.drawable.woman)
                    .fitCenter()
                    .into(header);
            name = convertView.findViewById(R.id.item_actress_name);
            name.setText(getItem(position).name);
            return convertView;
        }

        class ActressViewHolder {
            private View itemView;

            public ActressViewHolder(View itemView) {
                this.itemView = itemView;
            }
        }
    }

}
