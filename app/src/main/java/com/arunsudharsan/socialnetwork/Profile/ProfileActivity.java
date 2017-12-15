package com.arunsudharsan.socialnetwork.Profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.ViewCommentsFragment;
import com.arunsudharsan.socialnetwork.utils.ViewPostFragment;
import com.arunsudharsan.socialnetwork.models.Photo;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.GridImageAdapter;

import java.util.ArrayList;

/**
 * Created by root on 13/12/17.
 */

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.onImagegridSelectedListener,ViewPostFragment.OnCommentThreadSelectedListener {
    private Context mContext = ProfileActivity.this;
    private static final int Activity_num = 4;
    private ProgressBar progressBar;
    private ImageView profilephoto;
    private static final int NUM_GRID_COLUMS = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //  progressBar = findViewById(R.id.profileProgressBar);
        //progressBar.setVisibility(View.GONE);
        //    settingupNavigationView();
        //  setuptoolbar();
        //setupactivitywidgets();
        //  setprofileimage();
        //tempgridsetup();
        init();
    }

    private void init() {
        ProfileFragment fragment = new ProfileFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(getString(R.string.profilefragment));
        fragmentTransaction.commit();
    }

    private void tempgridsetup() {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            urls.add("https://pbs.twimg.com/profile_images/839721704163155970/LI_TRk1z_400x400.jpg");
        }
        setupimagegrid(urls);
    }

    private void setupimagegrid(ArrayList<String> urls) {
        GridView gridView = findViewById(R.id.gridviewprofilefeed);
        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", urls);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imagewidth = gridWidth / NUM_GRID_COLUMS;
        gridView.setColumnWidth(imagewidth);
        gridView.setAdapter(adapter);
    }

    /*
     * Setting Up Bottom Navigation View
     * */
    private void settingupNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }


    @Override
    public void onGridImageselected(Photo photo, int ActivityNumber) {

        ViewPostFragment viewPostFragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), (Parcelable) photo);
        args.putInt("activity", ActivityNumber);
        viewPostFragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, viewPostFragment);
        fragmentTransaction.addToBackStack(getString(R.string.view_post_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onCommentSelectedThreadListener(Photo photo) {
        ViewCommentsFragment fragment =  new ViewCommentsFragment();
        Bundle args = new Bundle();
         args.putParcelable(getString(R.string.photo),photo);
         fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(getString(R.string.view_comment_fragment));
        fragmentTransaction.commit();
    }
}