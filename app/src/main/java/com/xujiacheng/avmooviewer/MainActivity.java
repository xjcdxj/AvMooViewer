package com.xujiacheng.avmooviewer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.xujiacheng.avmooviewer.ui.actress.ActressFragment;
import com.xujiacheng.avmooviewer.ui.allvideos.AllAvFragment;
import com.xujiacheng.avmooviewer.ui.collections.CollectionsFragment;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //收藏视频的文件保存地址
    public static File CollectionDir;
    private static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在外部存储沙盒内
        CollectionDir = getExternalFilesDir("Collections");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        mFragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //默认启动是主页，全部视频
        transaction.replace(R.id.container, new AllAvFragment());
        transaction.commit();
        //左侧菜单导航到各个功能的fragment
        //这几个fragment作为顶级界面，不加入返回栈
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.menu_all_vids:
                        transaction.replace(R.id.container, new AllAvFragment());
                        break;
                    case R.id.menu_actress:
                        transaction.replace(R.id.container, new ActressFragment());
                        break;
                    case R.id.menu_category:
                        break;
                    case R.id.menu_collections:
                        transaction.replace(R.id.container, new CollectionsFragment());
                }
                transaction.commit();
                return false;
            }
        });
    }

    public static void changeFragment(Fragment destination, boolean isHomeView) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!isHomeView) {
            transaction.addToBackStack(null);
        }
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container, destination);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RunningTask.cleanAlltask();
    }
}
