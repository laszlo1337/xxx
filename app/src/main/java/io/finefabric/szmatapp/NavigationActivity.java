package io.finefabric.szmatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NavigationActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view_pager)
    AHBottomNavigationViewPager viewPager;

    @BindView(R.id.navigation_bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private int[] tabColors;
    private AHBottomNavigationAdapter navigationAdapter;
    DemoFragment currentFragment;
    DemoViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ButterKnife.bind(this);

        setUpNavigation();
    }

    private void setUpNavigation() {

        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu);

        bottomNavigation.setColored(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                viewPager.setCurrentItem(position, false);
                currentFragment = adapter.getCurrentFragment();

                return true;
            }
        });

        adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
    }



}
