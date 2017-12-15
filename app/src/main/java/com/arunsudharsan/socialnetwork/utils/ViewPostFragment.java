package com.arunsudharsan.socialnetwork.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.Like;
import com.arunsudharsan.socialnetwork.models.Photo;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.arunsudharsan.socialnetwork.utils.BottomNavigationViewHelper;
import com.arunsudharsan.socialnetwork.utils.GridImageAdapter;
import com.arunsudharsan.socialnetwork.utils.SquareImageView;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 15/12/17.
 */

public class ViewPostFragment extends Fragment {

    public interface
    OnCommentThreadSelectedListener {
        void onCommentSelectedThreadListener(Photo photo);
    }

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    OnCommentThreadSelectedListener onCommentThreadSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            onCommentThreadSelectedListener = (OnCommentThreadSelectedListener) getActivity();
        } catch (ClassCastException ignored) {

        }
    }

    private StringBuilder musers;
    private Photo mphoto;

    private boolean mLikedbycurrentuser;
    private String mlikedstring;
    private int Activitynumber = 0;
    private SquareImageView mImage;
    private BottomNavigationView bottomNavigationView;
    private TextView mBacklabel, mUsername, mCaption, mTimestamp, mlikes;
    private ImageView mBackarrow, mmoremenu, mheartliked, mheartunliked;
    private ImageView mspeechbubble;
    private CircleImageView mprofile;
    private Heart heart;

    //firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String profileurl, photousername;
    private UserAccountSettings settings;
    private GestureDetector gestureDetector;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_post, container, false);

        mImage = v.findViewById(R.id.post_image);
        mprofile = v.findViewById(R.id.profilephoto);
        mlikes = v.findViewById(R.id.imglikes);
        bottomNavigationView = v.findViewById(R.id.bottomNavViewBar);
        mBackarrow = v.findViewById(R.id.backarrowviewphoto);
        mmoremenu = v.findViewById(R.id.viewphotomenuicon);
        mheartliked = v.findViewById(R.id.heartred);
        mheartunliked = v.findViewById(R.id.heartwhite);
        mUsername = v.findViewById(R.id.username);
        mspeechbubble = v.findViewById(R.id.speechbubble);
        mCaption = v.findViewById(R.id.img_caption);
        mTimestamp = v.findViewById(R.id.img_time_posted);
        gestureDetector = new GestureDetector(getActivity(), new gesturelistener());
        heart = new Heart(mheartunliked, mheartliked);
        //  testtoggle();
        try {
            mphoto = getphotofromBundle();
            Activitynumber = getActivitynumberfromBundle();
            setfirebaseauth();

            UniversalImageLoader.setImage(mphoto.getImgpath(), mImage, null, "");
            getprofiledetails();
            getlikesstring();
            settingupNavigationView();


        } catch (NullPointerException ignored) {


        }

        return v;
    }


    private void getprofiledetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbuseraccountsettings))
                .orderByChild("userid")
                .equalTo(mphoto.getUserid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    settings = ds.getValue(UserAccountSettings.class);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getlikesstring() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbphotos))
                .child(mphoto.getPhotoid())
                .child(getString(R.string.likes));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                musers = new StringBuilder();

                for (DataSnapshot ds : dataSnapshot.getChildren())

                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference.child(getString(R.string.dbusers))
                            .orderByChild("userid")
                            .equalTo(ds.getValue(Like.class).getUserid());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            musers = new StringBuilder();

                            for (DataSnapshot single : dataSnapshot.getChildren())

                            {
                                musers.append(single.getValue(User.class).getUsername());
                                musers.append(",");
                            }

                            String[] splitusers = musers.toString().split(",");
                            if (musers.toString().contains(settings.getUsername() + ","))

                            {
                                mLikedbycurrentuser = true;
                            } else {
                                mLikedbycurrentuser = false;
                            }

                            int length = splitusers.length;
                            switch (length) {
                                case 1:
                                    mlikedstring = "Liked By " + splitusers[0];
                                    break;
                                case 2:
                                    mlikedstring = "Liked By " + splitusers[0] + " and " + splitusers[1];
                                    break;
                                case 3:
                                    mlikedstring = "Liked By " + splitusers[0] + " , " + splitusers[1] + " and " + splitusers[2];


                                    break;

                                default:
                                    mlikedstring = "Liked By " + splitusers[0] + " , " + splitusers[1] + " and " + (length - 2) + "others";


                            }
                            setupwidgets();
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (!dataSnapshot.exists()) {
                    mlikedstring = "";
                    mLikedbycurrentuser = false;
                    setupwidgets();

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private int getActivitynumberfromBundle() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getInt("activity");
        } else
            return 0;

    }


    private class gesturelistener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // heart.toggleLike();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child(getString(R.string.dbphotos))
                    .child(mphoto.getPhotoid())
                    .child(getString(R.string.likes));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren())

                    {
                        String keyid = ds.getKey();
                        //user already liked the pic
                        if (mLikedbycurrentuser && ds.getValue(Like.class).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            myRef.child(getString(R.string.dbphotos))
                                    .child(mphoto.getPhotoid())

                                    .child(getString(R.string.likes))
                                    .child(keyid)
                                    .removeValue();

                            myRef.child(getString(R.string.dbuser_photos))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(mphoto.getPhotoid())

                                    .child(getString(R.string.likes))
                                    .child(keyid)
                                    .removeValue();
                            heart.toggleLike();
                            getlikesstring();
                        } else if (!mLikedbycurrentuser) {
                            //add new like
                            addnewlike();
                            break;
                        }
                    }
                    if (!dataSnapshot.exists()) {
                        //add new like
                        addnewlike();
                    }

                    //user not liked the pic yet


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return true;
        }

    }


    @SuppressLint("SetTextI18n")
    private void setupwidgets() {
        String timestamp = getTimestampdiffernce();
        if (!timestamp.equals("0")) {
            mTimestamp.setText(timestamp + getString(R.string.daysago));
        } else {
            mTimestamp.setText(R.string.today);
        }
        profileurl = settings.getProfilephoto();
        //Toast.makeText(getActivity(), ""+profileurl ,Toast.LENGTH_SHORT).show();
        UniversalImageLoader.setImage(profileurl, mprofile, null, "");
        photousername = settings.getUsername();
        if (photousername != null)
            mUsername.setText(photousername);

        mlikes.setText(mlikedstring);
        mCaption.setText(mphoto.getCaption());

        if (mLikedbycurrentuser) {
            mheartunliked.setVisibility(View.GONE);
            mheartliked.setVisibility(View.VISIBLE);

            mheartliked.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });


        } else {
            mheartunliked.setVisibility(View.VISIBLE);
            mheartliked.setVisibility(View.GONE);

            mheartunliked.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });

        }

        mBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mspeechbubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommentThreadSelectedListener.onCommentSelectedThreadListener(mphoto);
            }
        });
    }

    private String getTimestampdiffernce() {
        String difference = "";

//simple date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        //setting today
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        sdf.format(today);
        //getting timnestampfrom photo
        final String mphototimestamp = mphoto.getDatecreated();
        Date timestamp;

        try {
            timestamp = sdf.parse(mphototimestamp);

            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
        } catch (ParseException ignored) {
            difference = "0";
        }
        return difference;
    }

    private Photo getphotofromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        } else
            return null;
    }

    /*
         * Setting Up Bottom Navigation View
         * */
    private void settingupNavigationView() {
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(getActivity(), getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(Activitynumber);
        menuItem.setChecked(true);
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

    private void addnewlike() {
        String key = myRef.push().getKey();
        Like like = new Like();
        like.setUserid(FirebaseAuth.getInstance().getCurrentUser().getUid());


        myRef.child(getString(R.string.dbphotos))
                .child(mphoto.getPhotoid())
                .child(getString(R.string.likes))
                .child(key)
                .setValue(like);

        myRef.child(getString(R.string.dbuser_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mphoto.getPhotoid())

                .child(getString(R.string.likes))
                .child(key)
                .setValue(like);
        heart.toggleLike();
        getlikesstring();
    }
}
