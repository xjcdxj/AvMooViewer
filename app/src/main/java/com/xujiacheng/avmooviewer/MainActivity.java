package com.xujiacheng.avmooviewer;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.xujiacheng.avmooviewer.ui.actress.ActressFragment;
import com.xujiacheng.avmooviewer.ui.allvideos.AllAvFragment;
import com.xujiacheng.avmooviewer.ui.category.categorylist.ShowCategoryFragment;
import com.xujiacheng.avmooviewer.ui.category.categorylist.ViewPagerCategoryFragment;
import com.xujiacheng.avmooviewer.ui.collections.CollectionsFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //收藏视频的文件保存地址
    public static File CollectionDir;
    private static FragmentManager mFragmentManager;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        linearLayout = findViewById(R.id.container);

        //在外部存储沙盒内
        CollectionDir = getExternalFilesDir("Collections");
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        mFragmentManager = getSupportFragmentManager();
        //默认启动是主页，全部视频
        if (!mainActivityViewModel.isShown) {
            changeFragment(new AllAvFragment(), true);
            mainActivityViewModel.isShown = true;
            navigationView.setCheckedItem(R.id.menu_home);
        }


        //左侧菜单导航到各个功能的fragment
        //这几个fragment作为顶级界面，不加入返回栈
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        changeFragment(new AllAvFragment(), true);
                        break;
                    case R.id.menu_actress:
                        changeFragment(new ActressFragment(), true);
                        break;
                    case R.id.menu_category:
                        changeFragment(new ViewPagerCategoryFragment(), true);
                        break;
                    case R.id.menu_collections:
                        changeFragment(new CollectionsFragment(), true);
                        break;
//                    case R.id.menu_test:
////                        changeFragment(new ViewPagerCategoryFragment(), true);
//                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }



    public static void changeFragment(Fragment destination, boolean isHomeView) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (!isHomeView) {
            transaction.addToBackStack(null);
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.replace(R.id.container, destination);
        transaction.commit();
    }
}
