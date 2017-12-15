package com.arunsudharsan.socialnetwork.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.Permissions;
import com.arunsudharsan.socialnetwork.utils.SectionsPagerAdapter;
import com.arunsudharsan.socialnetwork.utils.SectionsStatePagerAdapter;

import java.util.Random;

/**
 * Created by root on 13/12/17.
 */

public class ShareActivity extends AppCompatActivity {

    private Context mContext;
    private static final int Activity_num = 2;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
       // settingupNavigationView();
        mContext = ShareActivity.this;
        setViewPager();
        if (checkpermissionsArray(Permissions.PERMISSIONS)) {


        } else {
            verifyPermissions(Permissions.PERMISSIONS);

        }
    }

    public int getcurrentabnumber(){
        return viewPager.getCurrentItem();
    }

    private void setViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addfragment(new GalleryFragment());
        adapter.addfragment(new PhotoFragment());
        viewPager = findViewById(R.id.mycontainer);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.bottomtabs);
        tabLayout.setupWithViewPager(viewPager);
        try {
            tabLayout.getTabAt(0).setText(getString(R.string.Gallery));
            tabLayout.getTabAt(1).setText(getString(R.string.Photo));
        } catch (NullPointerException e) {
            Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(ShareActivity.this, permissions, 1);
    }

    public boolean checkpermissionsArray(String[] Permissions) {

        for (int i = 0; i <= Permissions.length; i++) {
            String check = Permissions[i];
            if (!checkPermission(check)) {
                return false;
            }
        }
        return true;
    }

    public int gettask(){
        return getIntent().getFlags();
    }

    public boolean checkPermission(String Permission) {
        int permissionrequest = ActivityCompat.checkSelfPermission(mContext, Permission);
        if (permissionrequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else return true;
    }

    /*
    * Setting Up Bottom Navigation View
    * */
    private void settingupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }
}
