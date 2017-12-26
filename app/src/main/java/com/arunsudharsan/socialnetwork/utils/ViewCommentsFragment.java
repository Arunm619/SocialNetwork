package com.arunsudharsan.socialnetwork.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.Comment;
import com.arunsudharsan.socialnetwork.models.Like;
import com.arunsudharsan.socialnetwork.models.Photo;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 15/12/17.
 */

public class ViewCommentsFragment extends Fragment {
    public ViewCommentsFragment() {
        super();
        setArguments(new Bundle());

    }

    private Photo mphoto;
    private ImageView mbackarrow, mcheckmark;
    private EditText mcommentet;
    private Context  context;

    private ArrayList<Comment> mcomments;
    private ListView listViewcomments;
    //firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String profileurl, photousername;
    private UserAccountSettings settings;
    private GestureDetector gestureDetector;
    private TextView mcommentstextview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_comments, container, false);
        mbackarrow = v.findViewById(R.id.ivbackarrow);
        mcheckmark = v.findViewById(R.id.ivpostcomment);
        mcommentet = v.findViewById(R.id.etcomment);
        listViewcomments = v.findViewById(R.id.lvcomments);
context= getActivity();
        try {
            mphoto = getphotofromBundle();


        } catch (NullPointerException ignored) {


        }
        mcomments = new ArrayList<>();

        setfirebaseauth();

        return v;
    }

    private void setupwidgets() {


        CommentListAdapter commentListAdapter = new CommentListAdapter(context,
                R.layout.layout_comment, mcomments);
        listViewcomments.setAdapter(commentListAdapter);


        mbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mcheckmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = mcommentet.getText().toString();

                if (!TextUtils.isEmpty(comment)) {
                    addnewcomment(comment);
                    mcommentet.setText("");
                    hidekeyboard();
                }

            }
        });
    }

    private void hidekeyboard() {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert manager != null;
            manager.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
        }
    }

    private Photo getphotofromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        } else
            return null;
    }

    private void addnewcomment(String newcomment) {
        String CommentId = myRef.push().getKey();
        Comment comment = new Comment();
        comment.setComment(newcomment);
        comment.setDatecreated(gettimestamp());
        comment.setUserid(FirebaseAuth.getInstance().getCurrentUser().getUid());


        myRef.child(context.getString(R.string.dbphotos)).child(mphoto.getPhotoid())
                .child(getContext().getString(R.string.comment))
                .child(CommentId).setValue(comment);

        myRef.child(context.getString(R.string.dbuser_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mphoto.getPhotoid())
                .child(context.getString(R.string.comment))
                .child(CommentId).setValue(comment);
    }

    private String gettimestamp() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        return sdf.format(new Date());
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

        if (mphoto.getComments().size() == 0) {

            mcomments.clear();
            Comment firstComment = new Comment();
            firstComment.setComment(mphoto.getCaption());
            firstComment.setUserid(mphoto.getUserid());
            firstComment.setDatecreated(mphoto.getDatecreated());
            mcomments.add(firstComment);
            mphoto.setComments(mcomments);
            setupwidgets();
        }

        myRef.child(context.getString(R.string.dbphotos))
                .child(mphoto.getPhotoid())
                .child(getContext().getString(R.string.comment)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Query query = myRef.child(context.getString(R.string.dbphotos)).orderByChild(context.getString(R.string.photoid))
                        .equalTo(mphoto.getPhotoid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singlesnapshot : dataSnapshot.getChildren()) {
                            //  photos.add(singlesnapshot.getValue(Photo.class));

                            Photo photo = new Photo();
                            Map<String, Object> objectMap = (HashMap<String, Object>) singlesnapshot.getValue();
                            photo.setPhotoid(objectMap.get(context.getString(R.string.photoid)).toString());
                            photo.setDatecreated(objectMap.get(context.getString(R.string.datecreated)).toString());
                            photo.setUserid(objectMap.get(context.getString(R.string.userid)).toString());
                            photo.setTags(objectMap.get(context.getString(R.string.tags)).toString());
                            photo.setImgpath(objectMap.get(context.getString(R.string.imgpath)).toString());
                            photo.setCaption(objectMap.get(context.getString(R.string.caption)).toString());


                            mcomments.clear();
                            Comment firstComment = new Comment();
                            firstComment.setComment(mphoto.getCaption());
                            firstComment.setUserid(mphoto.getUserid());
                            firstComment.setDatecreated(mphoto.getDatecreated());
                            mcomments.add(firstComment);

                            for (DataSnapshot dataSnapshot1 : singlesnapshot
                                    .child(context.getString(R.string.comment))
                                    .getChildren()) {
                                Comment comment = new Comment();
                                comment.setUserid(dataSnapshot1.getValue(Comment.class).getUserid());
                                comment.setDatecreated((dataSnapshot1.getValue(Comment.class).getDatecreated()));
                                comment.setComment((dataSnapshot1.getValue(Comment.class).getComment()));
                                mcomments.add(comment);
                            }
                            photo.setComments(mcomments);
                            mphoto = photo;
                            setupwidgets();


                /*    List<Like> likes = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : singlesnapshot
                            .child(getString(R.string.likes))
                            .getChildren()) {
                        Like like = new Like();
                        like.setUserid(dataSnapshot1.getValue(Like.class).getUserid());
*/

                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
}