package com.randomname.vlad.weathertest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.randomname.vlad.weathertest.MainActivity;
import com.randomname.vlad.weathertest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerBaseActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    private Drawer drawer;

    protected void initDrawer(Toolbar toolbar, final String className) {
        setSupportActionBar(toolbar);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.main_drawer_item),
                        new PrimaryDrawerItem().withName(R.string.map_drawer_item)
                )
                .build();

        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                Intent intent;

                switch (i) {
                    case 0:
                        if (!className.equals("MainActivity")) {
                            intent = new Intent(DrawerBaseActivity.this, MainActivity.class);
                            startActivity(intent);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 300);
                        } else {
                            drawer.closeDrawer();
                        }
                        break;
                    case 1:
                        if (!className.equals("MapActivity")) {
                            intent = new Intent(DrawerBaseActivity.this, MapActivity.class);
                            startActivity(intent);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 300);
                        } else {
                            drawer.closeDrawer();
                        }
                        break;
                    default:
                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), R.string.press_back_again_toast, Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();
        }
    }
}
