package com.arunsudharsan.socialnetwork.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.Login.LoginActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.SectionsPagerAdapter;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {
    private static final int Activity_num = 0;
    private Context mContext = HomeActivity.this;
    private static final String TAG = "HomeActivity";

    //firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initimageloader();
        setfirebaseauth();
        settingupNavigationView();
        settingupViewPager();
    }

    private void checkcurrentuser(FirebaseUser user) {
        if (user == null)
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    private void setfirebaseauth() {

        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkcurrentuser(user );

            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
        checkcurrentuser(auth.getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null)
            auth.removeAuthStateListener(listener);
    }

    /*
            * Setting Up  View Pager: for adding Camera,Home and Messages Fragment
            * */
    private void settingupViewPager() {
        SectionsPagerAdapter Adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Adapter.addfragment(new CameraFragment());//index:0
        Adapter.addfragment(new HomeFragment());//index:1
        Adapter.addfragment(new MessagesFragment());//index:2
        ViewPager viewPager = findViewById(R.id.mycontainer);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setAdapter(Adapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.d(TAG, "settingupViewPager: set");
        // TabLayout.Tab tab = tabLayout.getTabAt(0);
        // tab.setIcon(R.drawable.ic_android);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_android);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);

    }

    private void initimageloader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(HomeActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getconfig());
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
}
