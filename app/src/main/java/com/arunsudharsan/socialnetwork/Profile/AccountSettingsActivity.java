package com.arunsudharsan.socialnetwork.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.arunsudharsan.socialnetwork.utils.FirebaseMethods;
import com.arunsudharsan.socialnetwork.utils.SectionsStatePagerAdapter;
import com.arunsudharsan.socialnetwork.utils.StringManipulation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by root on 13/12/17.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    private Context mContext = AccountSettingsActivity.this;
    private static final int Activity_num = 4;

    public SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);

        viewPager = findViewById(R.id.mycontainer);
        relativeLayout = findViewById(R.id.relLayout1);
        firebaseMethods = new FirebaseMethods(mContext);
        settingupNavigationView();
        setupsettingslist();
        setupfragments();
        setupBackButtonarrow();
        getIncomingIntent();
    }


    public void setViewPager(int fragnumber) {
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
        sectionsStatePagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.editprofile));//fragment 0
        sectionsStatePagerAdapter.addFragment(new SignoutFragment(), getString(R.string.signout));//fragment 1

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


    private void getIncomingIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.selectedimg)) || intent.hasExtra(getString(R.string.selected_bitmap))) {

            if (intent.getStringExtra(getString(R.string.returntofragment)).equals(getString(R.string.editprofile))) {

                if (intent.hasExtra(getString(R.string.selectedimg))) {
                    String imgurl = intent.getStringExtra(getString(R.string.selectedimg));
                    firebaseMethods.uploadnewphoto(getString(R.string.profile_photo), null, 0, imgurl, null);

                } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {


                    Bitmap bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    firebaseMethods.uploadnewphoto(getString(R.string.profile_photo), null, 0, null, bitmap);


                }
            }

            if (intent.hasExtra(getString(R.string.callingactivity))) {
                setViewPager(sectionsStatePagerAdapter.getFragmentNumber(getString(R.string.editprofile)));
            }
        }

    }
}
