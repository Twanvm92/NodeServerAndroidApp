package twanvm.movieapp.Presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twanvm.movieapp.R;

public class MainActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();
    private ViewPager mViewPager;
    private FilmListFragment filmListFragment = new FilmListFragment();
    private RentedFilmFragment rentedFilmFragment = new RentedFilmFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        boolean loggedIn = false;

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("saved_token", "");
        if (token != null && !token.equals("")) {
            loggedIn = true;
        }

        MenuItem loginItem = menu.findItem(R.id.activityMain_item_login);
        MenuItem registerItem = menu.findItem(R.id.activityMain_item_register);
        MenuItem logoutItem = menu.findItem(R.id.activityMain_item_logout);
        if (loggedIn){
            loginItem.setVisible(false);
            registerItem.setVisible(false);
            logoutItem.setVisible(true);
        } else {
            loginItem.setVisible(true);
            registerItem.setVisible(true);
            logoutItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.activityMain_item_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.activityMain_item_register) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.activityMain_item_logout) {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getApplicationContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("saved_token");
            editor.remove("saved_userID");
            editor.apply();

            Toast.makeText(getApplicationContext(), "Succesfully logged out", Toast.LENGTH_SHORT).show();
            Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(filmListFragment, "Films");
        adapter.addFragment(rentedFilmFragment, "My rentals");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
