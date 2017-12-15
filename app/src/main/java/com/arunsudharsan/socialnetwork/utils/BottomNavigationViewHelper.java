package com.arunsudharsan.socialnetwork.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.arunsudharsan.socialnetwork.Home.HomeActivity;
import com.arunsudharsan.socialnetwork.Likes.LikesActivity;
import com.arunsudharsan.socialnetwork.Profile.ProfileActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.Search.SearchActivity;
import com.arunsudharsan.socialnetwork.Share.ShareActivity;

import java.lang.reflect.Field;

/**
 * Created by root on 13/12/17.
 */

public class BottomNavigationViewHelper {

    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    public static void enableNavigation(final Context context, final Activity activity,BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_house:
                        Intent i1 = new Intent(context, HomeActivity.class);//Activity_num=0
                        context.startActivity(i1);
                        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.ic_search:
                        Intent i2 = new Intent(context, SearchActivity.class); //Activity_num=1
                        context.startActivity(i2);
                        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;

                    case R.id.ic_circle:
                        Intent i3 = new Intent(context, ShareActivity.class);//Activity_num=2
                        context.startActivity(i3);
                        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;

                    case R.id.ic_alert:
                        Intent i4 = new Intent(context, LikesActivity.class);//Activity_num=3
                        context.startActivity(i4);
                        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;
                    case R.id.ic_android:
                        Intent i5 = new Intent(context, ProfileActivity.class);//Activity_num=4
                        context.startActivity(i5);

                        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;

                }

                return false;
            }
        });
    }
}
