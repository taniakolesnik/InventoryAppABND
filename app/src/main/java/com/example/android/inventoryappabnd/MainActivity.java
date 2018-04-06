package com.example.android.inventoryappabnd;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new SlideFragmentPagerAdapter(getSupportFragmentManager()));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNew_menu:
                insertItem();
                break;
            case R.id.deleteAll_menu:
               // deleteAllRecords();
                break;
        }
        return true;
    }

    private void insertItem() {
        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
        startActivity(intent);
    }

    private class SlideFragmentPagerAdapter extends FragmentPagerAdapter {


        public SlideFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AvailableItemsFragment();
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    args.putBoolean("inStock", true);
                    break;
                case 1:
                    args.putBoolean("inStock", false);
                    break;
                default:
                    return null;
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabName = "";
            switch (position) {
                case 0:
                    tabName = "Available";
                    break;
                case 1:
                    tabName = "Sold out";
                    break;
            }
            return tabName;
        }
    }
//
//    private void deleteAllRecords() {
//        getActivity().getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
//    }
}
