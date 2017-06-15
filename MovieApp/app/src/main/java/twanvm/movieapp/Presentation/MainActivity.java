package twanvm.movieapp.Presentation;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import twanvm.movieapp.Constants;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activityMain_item_login) {
            Log.e("test", "login gedrukt");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, 1 );
            return true;
        } else if (id == R.id.activityMain_item_register) {
            Log.e("test", "register gedrukt");
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivityForResult(intent, 1 );
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

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case Constants.FOTO_MAKEN:
//                Uri selectedImage;
//                if (resultCode == RESULT_OK) {
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    selectedImage = getImageUri(getApplicationContext(), photo);
//
//                    File finalFile = new File(getRealPathFromURI(selectedImage));
////                  test of image_path correct gepakt wordt
//                    fotoImageView.setImageURI(selectedImage);
//                    fotoButton.setText(R.string.fotoWijzigen);
//                    imagePath = finalFile.toString();
//
//                }
//                break;
//            case Constants.FOTO_KIEZEN:
//                if (resultCode == RESULT_OK) {
//                    selectedImage = data.getData();
//                    String image_path = getRealPathFromURI(selectedImage);
//                    fotoImageView.setImageURI(selectedImage);
//                    fotoButton.setText(R.string.fotoWijzigen);
//                    imagePath = image_path;
//
//                }
//        }
//    }


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
