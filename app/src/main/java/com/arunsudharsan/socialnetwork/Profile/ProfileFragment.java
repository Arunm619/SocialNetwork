package com.arunsudharsan.socialnetwork.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.Like;
import com.arunsudharsan.socialnetwork.models.Photo;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.arunsudharsan.socialnetwork.models.UserSettings;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.FirebaseMethods;
import com.arunsudharsan.socialnetwork.utils.GridImageAdapter;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 14/12/17.
 */

public class ProfileFragment extends Fragment {
    public interface onImagegridSelectedListener {
        void onGridImageselected(Photo photo, int ActivityNumber);
    }

    private onImagegridSelectedListener mOnImagegridSelectedListener;

    @Override
    public void onAttach(Context context) {
        try {
            mOnImagegridSelectedListener = (onImagegridSelectedListener) getActivity();

        } catch (ClassCastException ignored) {

        }
        super.onAttach(context);
    }

    //firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private TextView mposts, mfollowers, mfollowing, mDisplayname, mUsername, mwebsite, mdesc, editprofile;
    private ProgressBar progressBar;
    private CircleImageView profileimgview;
    private GridView gridView;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView profilemenu;
    private BottomNavigationView bottomNavigationView;
    private int Activity_num = 4;
    private Context mContext;
    private FirebaseMethods firebaseMethods;
    private int NUMGRIDCOLUMNS = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initwidgets(v);
        settingupNavigationView();
        setuptoolbar();
        setfirebaseauth();
        setuupgridview();
        return v;
    }

    private void initwidgets(View v) {
        mContext = getActivity();
        firebaseMethods = new FirebaseMethods(mContext);
        mDisplayname = v.findViewById(R.id.displayname);

        mUsername = v.findViewById(R.id.username);
        mdesc = v.findViewById(R.id.description);
        mwebsite = v.findViewById(R.id.website);
        mposts = v.findViewById(R.id.tvPosts);
        mfollowers = v.findViewById(R.id.tvFollowers);
        mfollowing = v.findViewById(R.id.tvFollowing);
        progressBar = v.findViewById(R.id.profileProgressBar);
        profileimgview = v.findViewById(R.id.profile_image);
        gridView = v.findViewById(R.id.gridviewprofilefeed);
        profilemenu = v.findViewById(R.id.profilemenuicon);
        bottomNavigationView = v.findViewById(R.id.bottomNavViewBar);
        editprofile = v.findViewById(R.id.textEditProfile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AccountSettingsActivity.class);
                i.putExtra(getString(R.string.callingactivity), getString(R.string.profileactivity));
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }


    /*
    * Setting Up Bottom Navigation View
    * */
    private void settingupNavigationView() {
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activity_num);
        menuItem.setChecked(true);
    }


    private void setuupgridview() {
        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbuser_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singlesnapshot : dataSnapshot.getChildren()) {
                    //  photos.add(singlesnapshot.getValue(Photo.class));

                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singlesnapshot.getValue();
                    photo.setPhotoid(objectMap.get(getString(R.string.photoid)).toString());
                    photo.setDatecreated(objectMap.get(getString(R.string.datecreated)).toString());
                    photo.setUserid(objectMap.get(getString(R.string.userid)).toString());
                    photo.setTags(objectMap.get(getString(R.string.tags)).toString());
                    photo.setImgpath(objectMap.get(getString(R.string.imgpath)).toString());
                    photo.setCaption(objectMap.get(getString(R.string.caption)).toString());
                    List<Like> likes = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : singlesnapshot
                            .child(getString(R.string.likes))
                            .getChildren()) {
                        Like like = new Like();
                        like.setUserid(dataSnapshot1.getValue(Like.class).getUserid());


                    }

                    photo.setLikes(likes);
                    photos.add(photo);
                }
                //setup Image Grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imagewidth = gridWidth / NUMGRIDCOLUMNS;
                gridView.setColumnWidth(imagewidth);
                ArrayList<String> imgurls = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    imgurls.add(photos.get(i).getImgpath());
                }

                GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, "", imgurls);
                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        mOnImagegridSelectedListener.onGridImageselected(photos.get(i), 4);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    *
    * */
    private void setuptoolbar() {

        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        profilemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(i);

            }
        });
    }

    private void setfirebaseauth() {

        auth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // checkcurrentuser(user );

            }
        };
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                setprofilewidgets(firebaseMethods.getUserSettings(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
        // checkcurrentuser(auth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null)
            auth.removeAuthStateListener(listener);
    }


    private void setprofilewidgets(UserSettings usersettings) {

        //User user = usersettings.getUser();
        UserAccountSettings userAccountSettings = usersettings.getSettings();
        UniversalImageLoader.setImage(userAccountSettings.getProfilephoto(), profileimgview, null, "");

        mDisplayname.setText(userAccountSettings.getDisplayname());
        mUsername.setText(userAccountSettings.getUsername());
        mwebsite.setText(userAccountSettings.getWebsite());
        mdesc.setText(userAccountSettings.getDescription());

        mfollowing.setText(String.valueOf(userAccountSettings.getFollowing()));
        mfollowers.setText(String.valueOf(userAccountSettings.getFollowers()));
        mposts.setText(String.valueOf(userAccountSettings.getPosts()));
        progressBar.setVisibility(View.GONE);
    }


}
