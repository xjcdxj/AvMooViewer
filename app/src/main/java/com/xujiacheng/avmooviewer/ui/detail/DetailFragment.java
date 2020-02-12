package com.xujiacheng.avmooviewer.ui.detail;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.R;
import com.xujiacheng.avmooviewer.databinding.DetailFragmentBinding;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.itembean.Info;
import com.xujiacheng.avmooviewer.ui.actress.actressvideos.ActressesVideosFragment;

import java.util.ArrayList;
import java.util.Objects;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    private String url;


    private DetailViewModel mViewModel;
    private DetailFragmentBinding binding;


    public DetailFragment() {
    }

    public DetailFragment(String url) {
        this.url = url;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        binding = DetailFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        binding.setData(mViewModel);
        mViewModel.checkCollections(this.url);

        final Toolbar toolbar = view.findViewById(R.id.detail_toolbar);
        final FloatingActionButton collectionButton = view.findViewById(R.id.collection_button);
        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(mViewModel.av.getValue()).url == null) {
                    Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show();
                } else {

                    mViewModel.addToFavorite();
                }
            }
        });
        toolbar.inflateMenu(R.menu.favorite);
        final ImageView cover = view.findViewById(R.id.item_detail_cover);
        final ProgressBar coverLoading = view.findViewById(R.id.item_detail_cover_loading);
        final TextView name = view.findViewById(R.id.item_detail_name);
        final TextView id = view.findViewById(R.id.item_detail_id);
        final TextView date = view.findViewById(R.id.item_detail_date);
        final NestedScrollView detailScroll = view.findViewById(R.id.detail_scroll_view);
        final GridView actressGridView = view.findViewById(R.id.item_actresses_grid_view);
        final LinearLayout detailContainer = view.findViewById(R.id.detail_container);
        final MenuItem addToCollectionMenu = toolbar.getMenu().findItem(R.id.menu_favorite);
        addToCollectionMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (Objects.requireNonNull(mViewModel.av.getValue()).url == null) {
                    Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show();
                } else {
                    mViewModel.addToFavorite();

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
        mViewModel.isInCollection.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean aBoolean) {
                final AnimationSet scale = new AnimationSet(true);
                scale.addAnimation(new ScaleAnimation(1F, 0F, 1F, 0F,
                        Animation.ABSOLUTE, collectionButton.getWidth() / 2,
                        Animation.ABSOLUTE, collectionButton.getHeight() / 2));
                scale.setDuration(150);
//                scale.setFillBefore(false);
                scale.setInterpolator(new AccelerateDecelerateInterpolator());
                scale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (aBoolean) {
                            addToCollectionMenu.setIcon(R.drawable.ic_star_black_24dp);
                            collectionButton.setImageResource(R.drawable.ic_star_black_24dp);
                        } else {
                            addToCollectionMenu.setIcon(R.drawable.ic_star_border_black_24dp);
                            collectionButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                        }
                        AnimationSet expand = new AnimationSet(true);
                        expand.addAnimation(new ScaleAnimation(0F, 1F, 0F, 1F,
                                Animation.ABSOLUTE, collectionButton.getWidth() / 2,
                                Animation.ABSOLUTE, collectionButton.getHeight() / 2));
                        expand.setDuration(150);
//                        expand.setFillBefore(false);
                        expand.setInterpolator(new AccelerateDecelerateInterpolator());
                        expand.cancel();
                        expand.reset();
                        collectionButton.startAnimation(expand);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                scale.cancel();
                scale.reset();
                collectionButton.startAnimation(scale);


            }
        });
        mViewModel.loadAvInfo(DetailFragment.this.url);
        mViewModel.av.observe(getViewLifecycleOwner(), new Observer<Av>() {
            @Override
            public void onChanged(Av av) {
                if (mViewModel.isDataReady) {
                    detailScroll.setVisibility(View.VISIBLE);
                    final int height = cover.getHeight();
                    Log.d(TAG, "onChanged: height = " + height);
                    final int width = cover.getWidth();
                    Log.d(TAG, "onChanged: width = " + width);
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
                                public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    coverLoading.setVisibility(View.GONE);
                                    return false;

                                }
                            })
                            .into(cover);

                    name.setText(av.name);
//                    toolbar.setTitle(av.name);
                    id.setText(av.id);
                    date.setText(av.releaseDate);
                    if (av.actors != null && av.actors.size() > 0) {
                        actressGridView.setAdapter(new ActressAdapter(av.actors));
                    } else {
                        actressGridView.setVisibility(View.GONE);
                    }
                    if (av.previewURL != null && av.previewURL.size() > 0) {
                        for (String url : av.previewURL) {
                            final ImageView imageView = new ImageView(requireContext());
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            imageView.setPadding(4, 4, 4, 4);
                            imageView.setAdjustViewBounds(true);
                            Glide.with(DetailFragment.this)
                                    .asBitmap()
                                    .load(url)
                                    .placeholder(R.drawable.avmooviewer)
                                    .into(imageView);
                            detailContainer.addView(imageView);
                        }
                    }
                }
            }
        });
        return view;
    }


    //用于加载演员头像到gridView中
    class ActressAdapter extends BaseAdapter {

        private ArrayList<Info> mActresses;
        private final LayoutInflater inflater;

        ActressAdapter(ArrayList<Info> mActresses) {
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
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_actress, parent, false);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Info item = getItem(position);
                        MainActivity.changeFragment(new ActressesVideosFragment(item.url, item.name), false);
                    }
                });

                final ImageView header = convertView.findViewById(R.id.item_actress_header);
                Glide.with(DetailFragment.this)
                        .asBitmap()
                        .load(getItem(position).imageURL)
                        .placeholder(R.drawable.woman)
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .into(header);
                TextView name = convertView.findViewById(R.id.item_actress_name);
                name.setText(getItem(position).name);
            }
            return convertView;
        }


    }


}
