package com.arunsudharsan.socialnetwork.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;

/**
 * Created by root on 13/12/17.
 */

public class SearchActivity extends AppCompatActivity {
    private static final int Activity_num=1;

    private Context mContext=SearchActivity.this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        settingupNavigationView();
    }

    /*
   * Setting Up Bottom Navigation View
   * */
    private void settingupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }
}
