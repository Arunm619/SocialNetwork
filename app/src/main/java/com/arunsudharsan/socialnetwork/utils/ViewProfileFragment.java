package com.arunsudharsan.socialnetwork.utils;

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
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.Profile.AccountSettingsActivity;
import com.arunsudharsan.socialnetwork.Profile.ProfileActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.Comment;
import com.arunsudharsan.socialnetwork.models.Like;
import com.arunsudharsan.socialnetwork.models.Photo;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.arunsudharsan.socialnetwork.models.UserSettings;
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

public class ViewProfileFragment extends Fragment {
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

    private TextView mposts, mfollowers, mfollowing, mDisplayname, mUsername, mwebsite, mdesc, editprofile,
            mfollow, munfollow;
    private ProgressBar progressBar;
    private CircleImageView profileimgview;
    private GridView gridView;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView profilemenu;
    private BottomNavigationView bottomNavigationView;
    private int Activity_num = 1;
    private Context mContext;
    private FirebaseMethods firebaseMethods;
    private int NUMGRIDCOLUMNS = 3;

    private User muser;
    private int mfollowersc=0, mfollowingc =0, mpostsc=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_profile, container, false);
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
        mfollow = v.findViewById(R.id.follow);
        munfollow = v.findViewById(R.id.unfollow);

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


        try {
            muser = getuserfrombundle();
            init();
            isfollowing();
            getFollowers();
            getFollowing();
            getpostcount();

        } catch (NullPointerException e) {
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

        settingupNavigationView();
        setfirebaseauth();

        //setuptoolbar();


        // setuupgridview();

        mfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().
                        child(getString(R.string.dbfollowing))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())

                        .child(muser.getUserid())
                        .child(getString(R.string.userid))
                        .setValue(muser.getUserid());


                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.dbfollowers))
                        .child(muser.getUserid())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.userid))

                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                setfollowing();
            }
        });

        munfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase.getInstance().getReference().
                        child(getString(R.string.dbfollowing))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())

                        .child(muser.getUserid())
                        .child(getString(R.string.userid))
                        .removeValue()
                ;
                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.dbfollowers))
                        .child(muser.getUserid())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.userid))

                        .removeValue();
                setunfollowing();
            }
        });
        return v;
    }

    private void getFollowers()
    {
        mfollowersc=0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbfollowers))
                .child(muser.getUserid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    mfollowersc++;


                }
                mfollowers.setText(String.valueOf(mfollowersc));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void getFollowing()
    {
        mfollowingc=0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbfollowing))
                .child(muser.getUserid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    mfollowingc++;


                }
                mfollowing.setText(String.valueOf(mfollowingc));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void getpostcount()
    {
        mpostsc=0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbuser_photos))
                .child(muser.getUserid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    mpostsc++;


                }
                mposts.setText(String.valueOf(mpostsc));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isfollowing() {
        setunfollowing();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbfollowing))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild(getString(R.string.userid))
                .equalTo(muser.getUserid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    setfollowing();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setfollowing() {

        mfollow.setVisibility(View.GONE);
        munfollow.setVisibility(View.VISIBLE);
        editprofile.setVisibility(View.GONE);
    }

    private void setunfollowing() {

        mfollow.setVisibility(View.VISIBLE);
        munfollow.setVisibility(View.GONE);
        editprofile.setVisibility(View.GONE);
    }

    private void setcurrentusersprofile() {

        mfollow.setVisibility(View.GONE);
        munfollow.setVisibility(View.GONE);
        editprofile.setVisibility(View.VISIBLE);
    }


    private void init() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbuseraccountsettings))
                .orderByChild(getString(R.string.userid))
                .equalTo(muser.getUserid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    UserSettings settings = new UserSettings();
                    settings.setUser(muser);
                    settings.setSettings(singleSnapshot.getValue(UserAccountSettings.class));
                    setprofilewidgets(settings);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference1.child(getString(R.string.dbuser_photos))
                .child(muser.getUserid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Photo> photos = new ArrayList<>();

                for (DataSnapshot singlesnapshot : dataSnapshot.getChildren()) {
                    //  photos.add(singlesnapshot.getValue(Photo.class));
                    try {


                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singlesnapshot.getValue();
                        photo.setPhotoid(objectMap.get(getString(R.string.photoid)).toString());
                        photo.setDatecreated(objectMap.get(getString(R.string.datecreated)).toString());
                        photo.setUserid(objectMap.get(getString(R.string.userid)).toString());
                        photo.setTags(objectMap.get(getString(R.string.tags)).toString());
                        photo.setImgpath(objectMap.get(getString(R.string.imgpath)).toString());
                        photo.setCaption(objectMap.get(getString(R.string.caption)).toString());


                        ArrayList<Comment> mcomments = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : singlesnapshot
                                .child(getString(R.string.comment))
                                .getChildren()) {
                            Comment comment = new Comment();
                            comment.setUserid(dataSnapshot1.getValue(Comment.class).getUserid());
                            comment.setDatecreated((dataSnapshot1.getValue(Comment.class).getDatecreated()));
                            comment.setComment((dataSnapshot1.getValue(Comment.class).getComment()));
                            mcomments.add(comment);
                        }
                        photo.setComments(mcomments);


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
                    catch (Exception e)
                    {}
                    }
                    setimagegrid(photos);
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setimagegrid(final ArrayList<Photo> photos) {
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

                mOnImagegridSelectedListener.onGridImageselected(photos.get(i), 2);
            }
        });

    }

    private void initwidgets(View v) {
    }

    private User getuserfrombundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            Toast.makeText(mContext, "pochu pochu", Toast.LENGTH_SHORT).show();

            return bundle.getParcelable(getString(R.string.intent_user));
        } else
            return null;
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


    /*
    *
    * */
    private void setuptoolbar() {

      /*  ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        profilemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(i);

            }
        });*/
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
