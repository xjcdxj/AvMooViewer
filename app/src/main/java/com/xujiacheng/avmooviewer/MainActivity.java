package com.xujiacheng.avmooviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.xujiacheng.avmooviewer.ui.actress.ActressFragment;
import com.xujiacheng.avmooviewer.ui.allvideos.AllAvFragment;
import com.xujiacheng.avmooviewer.ui.base.ShowAvsBaseFragment;
import com.xujiacheng.avmooviewer.ui.collections.CollectionsFragment;
import com.xujiacheng.avmooviewer.utils.Collections;
import com.xujiacheng.avmooviewer.utils.RunningTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int SEARCH = 1;
    public static final String fragment_type = "FRAGMENT_TYPE";
    private NavController navController;
    public static File CollectionDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CollectionDir = getExternalFilesDir("Collections");
        try {
            File file = new File(CollectionDir, "collections.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("test");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
//        navController = Navigation.findNavController(this, R.id.fragment);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, new AllAvFragment());

        transaction.commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.menu_all_vids:
                        transaction.replace(R.id.container, new AllAvFragment());
                        Toast.makeText(MainActivity.this, "all", Toast.LENGTH_SHORT).show();
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
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }
}
