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

public class ViewCommentsFragment extends Fragment {
    public ViewCommentsFragment() {
        super();
        setArguments(new Bundle());

    }

    private Photo mphoto;
    private ImageView mbackarrow, mcheckmark;
    private EditText mcommentet;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_comments, container, false);
        mbackarrow = v.findViewById(R.id.ivbackarrow);
        mcheckmark = v.findViewById(R.id.ivpostcomment);
        mcommentet = v.findViewById(R.id.etcomment);
        listViewcomments = v.findViewById(R.id.lvcomments);


        try {
            mphoto = getphotofromBundle();


        } catch (NullPointerException ignored) {


        }
        setfirebaseauth();
        mcomments = new ArrayList<>();
        Comment firstComment = new Comment();
        firstComment.setComment(mphoto.getCaption());
        firstComment.setUserid(mphoto.getUserid());
        firstComment.setDatecreated(mphoto.getDatecreated());
        mcomments.add(firstComment);
        CommentListAdapter commentListAdapter = new CommentListAdapter(getActivity(),
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
        return v;
    }

    private void hidekeyboard() {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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


        myRef.child(getString(R.string.dbphotos)).child(mphoto.getPhotoid())
                .child(getString(R.string.comment))
                .child(CommentId).setValue(comment);

        myRef.child(getString(R.string.dbuser_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mphoto.getPhotoid())
                .child(getString(R.string.comment))
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
}