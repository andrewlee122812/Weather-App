package com.randomname.vlad.weathertest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.randomname.vlad.weathertest.Activities.AddCityActivity;
import com.randomname.vlad.weathertest.Activities.SettingsActivity;
import com.randomname.vlad.weathertest.Fragments.CitiesWeatherListFragment;
import com.randomname.vlad.weathertest.Model.Main;
import com.randomname.vlad.weathertest.Views.MaterialSearch.MaterialSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "Main Activity Tag";
    public final static int ADD_CITY_SUCC_CODE = 200;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.circle_reveal_view)
    View circleRevealView;

    private CitiesWeatherListFragment weatherListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherListFragment = (CitiesWeatherListFragment) getSupportFragmentManager().findFragmentByTag("cities_recycler_fragment");

        if (weatherListFragment != null) {
            weatherListFragment.setSearchQuery(searchView.getQuery());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CITY_SUCC_CODE && resultCode == Activity.RESULT_OK) {
            weatherListFragment.updateList();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                if (weatherListFragment != null) {
                    weatherListFragment.flushFilter();
                }
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (weatherListFragment != null) {
                    weatherListFragment.setSearchQuery(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (weatherListFragment != null) {
                    weatherListFragment.setSearchQuery(newText);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.add_fb)
    public void onAddClick() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            animatePreLollipop();
        } else {
            animateLollipop();
        }
    }

    private void animatePreLollipop() {
        int cx = (circleRevealView.getLeft() + circleRevealView.getRight());
        int cy = (circleRevealView.getTop() + circleRevealView.getBottom());

        // get the final radius for the clipping circle
        int dx = Math.max(cx, circleRevealView.getWidth() - cx);
        int dy = Math.max(cy, circleRevealView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator =
                (Animator) ViewAnimationUtils.createCircularReveal(circleRevealView, cx, cy, 0, finalRadius).get();

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#00897B"), Color.parseColor("#80CBC4"));
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleRevealView.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                circleRevealView.setVisibility(View.VISIBLE);
                circleRevealView.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
                startActivityForResult(intent, ADD_CITY_SUCC_CODE);
                overridePendingTransition(R.anim.fade_in, R.anim.stay_still);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circleRevealView.setVisibility(View.INVISIBLE);
                        circleRevealView.setClickable(false);
                    }
                }, 400);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                circleRevealView.setVisibility(View.INVISIBLE);
                circleRevealView.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animatorSet.playTogether(animator, colorAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    private void animateLollipop() {
        int cx = (circleRevealView.getLeft() + circleRevealView.getRight());
        int cy = (circleRevealView.getTop() + circleRevealView.getBottom());

        // get the final radius for the clipping circle
        int dx = Math.max(cx, circleRevealView.getWidth() - cx);
        int dy = Math.max(cy, circleRevealView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        android.animation.AnimatorSet animatorSet = new android.animation.AnimatorSet();

        android.animation.Animator anim = android.view.ViewAnimationUtils.createCircularReveal(circleRevealView, cx, cy, 0, finalRadius);
        android.animation.ValueAnimator colorAnimator = android.animation.ValueAnimator.ofObject(new android.animation.ArgbEvaluator(), Color.parseColor("#00897B"), Color.parseColor("#80CBC4"));

        colorAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                circleRevealView.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });

        animatorSet.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                circleRevealView.setVisibility(View.VISIBLE);
                circleRevealView.setClickable(true);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
                startActivityForResult(intent, ADD_CITY_SUCC_CODE);
                overridePendingTransition(R.anim.fade_in, R.anim.stay_still);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circleRevealView.setVisibility(View.INVISIBLE);
                        circleRevealView.setClickable(false);
                    }
                }, 400);
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                circleRevealView.setVisibility(View.INVISIBLE);
                circleRevealView.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {

            }
        });

        animatorSet.playTogether(anim, colorAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }
}
