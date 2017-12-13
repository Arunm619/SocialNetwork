package com.arunsudharsan.socialnetwork.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.GridImageAdapter;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;

import java.util.ArrayList;

/**
 * Created by root on 13/12/17.
 */

public class ProfileActivity extends AppCompatActivity {
    private Context mContext = ProfileActivity.this;
    private static final int Activity_num = 4;
    private ProgressBar progressBar;
    private ImageView profilephoto;
    private static final int NUM_GRID_COLUMS = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressBar = findViewById(R.id.profileProgressBar);
        progressBar.setVisibility(View.GONE);
        settingupNavigationView();
        setuptoolbar();
        setupactivitywidgets();
        setprofileimage();
        tempgridsetup();
    }

    private void tempgridsetup() {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            urls.add("https://pbs.twimg.com/profile_images/839721704163155970/LI_TRk1z_400x400.jpg");
        }
        setupimagegrid(urls);
    }

    private void setupimagegrid(ArrayList<String> urls) {
        GridView gridView = findViewById(R.id.gridview);
        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", urls);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imagewidth = gridWidth / NUM_GRID_COLUMS;
gridView.setColumnWidth(imagewidth);
        gridView.setAdapter(adapter);
    }

    private void setupactivitywidgets() {
        progressBar = findViewById(R.id.profileProgressBar);
        progressBar.setVisibility(View.GONE);
        profilephoto = findViewById(R.id.profile_image);
    }

    private void setprofileimage() {
        String url = "scontent.fmaa3-1.fna.fbcdn.net/v/t1.0-9/15356598_1802393030089473_486476897344407993_n.jpg?oh=17c4a456b48dce097691941f7f90faef&oe=5AD74B84";
        UniversalImageLoader.setImage(url, profilephoto, progressBar, "https://");
    }

    /*
     * Setting Up Bottom Navigation View
     * */
    private void settingupNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }

    /*
    *
    * */
    private void setuptoolbar() {
        Toolbar toolbar = findViewById(R.id.ProfileToobar);
        setSupportActionBar(toolbar);
        ImageView ivprofilemenu = findViewById(R.id.profilemenuicon);
        ivprofilemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(i);
            }
        });
    }
}