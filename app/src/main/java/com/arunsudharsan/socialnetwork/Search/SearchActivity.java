package com.arunsudharsan.socialnetwork.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.Profile.ProfileActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.UserListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 13/12/17.
 */

public class SearchActivity extends AppCompatActivity {
    private static final int Activity_num = 1;
    private EditText etsearch;
    private ListView listView;
    private List<User> userList;
    private UserListAdapter mAdapter;
    private Context mContext = SearchActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etsearch = findViewById(R.id.etsearch);
        etsearch.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        listView = findViewById(R.id.lvforsearchresults);

        settingupNavigationView();
        initTextListener();


    }

    private void searchformatch(String keyword) {


        userList.clear();

        if (keyword.length() == 0) {
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference
                    .child(getString(R.string.dbusers))
                    .orderByChild(getString(R.string.field_username))
                    .equalTo(keyword);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        userList.add(singleSnapshot.getValue(User.class));

                        updateuserslist();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void initTextListener() {
        userList = new ArrayList<>();
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = etsearch.getText().toString().toLowerCase(Locale.getDefault());
                searchformatch(text);
            }
        });

    }

    private void updateuserslist() {
        mAdapter = new UserListAdapter(SearchActivity.this, R.layout.layout_userlistitem, userList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                intent.putExtra(getString(R.string.callingactivity), getString(R.string.searchactivity));
                intent.putExtra(getString(R.string.intent_user), userList.get(i));
               // Toast.makeText(mContext, ""+userList.get(i).getUsername(), Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
    }

    private void hidekeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // assert manager != null;
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /*
   * Setting Up Bottom Navigation View
   * */
    private void settingupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }
}
