package com.arunsudharsan.socialnetwork.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.SectionsStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by root on 13/12/17.
 */

public class AccountSettingsActivity extends AppCompatActivity {
    private Context mContext = AccountSettingsActivity.this;
    private static final int Activity_num = 4;

    private SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);

        viewPager = findViewById(R.id.mycontainer);
        relativeLayout = findViewById(R.id.relLayout1);

        settingupNavigationView();
        setupsettingslist();
        setupfragments();
        setupBackButtonarrow();

    }



    private void setViewPager(int fragnumber)
    {
relativeLayout.setVisibility(View.GONE);
viewPager.setAdapter(sectionsStatePagerAdapter);
viewPager.setCurrentItem(fragnumber);
    }

    /*
       * setting Up backbutton arrow to finish this activity
       * */

    private void setupBackButtonarrow() {
        ImageView ivbackarrow = findViewById(R.id.ivbackarrow);
        ivbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /*
    * setting up fragments..
    * */
    private void setupfragments() {
        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        sectionsStatePagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.editprofile)) ;//fragment 0
         sectionsStatePagerAdapter.addFragment(new SignoutFragment(), getString(R.string.signout));//fragment 1

    }

    /*
     * Setting Up Bottom Navigation View
     * */
    private void settingupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }

    /*Setting Up Options List in Account Settings*/

    private void setupsettingslist() {
        ListView lv = findViewById(R.id.listviewsettings);
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.editprofile));//fragment 0
        options.add(getString(R.string.signout));//fragment 1

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        lv.setAdapter(adapter);
lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        setViewPager(i);
    }
});
    }
}
