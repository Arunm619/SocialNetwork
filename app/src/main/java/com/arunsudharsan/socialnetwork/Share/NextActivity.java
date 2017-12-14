package com.arunsudharsan.socialnetwork.Share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.FirebaseMethods;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by root on 14/12/17.
 */

public class NextActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private int imgcount = 0;
    private Context mContext;
    private FirebaseMethods firebaseMethods;
    private Intent intent;
    private String caption, imgUrl;
    //widgets
    private EditText mCaption;
    private static final String TAG = "NextActivity";
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Log.d(TAG, "onCreate: started");
        mContext = NextActivity.this;
        setfirebaseauth();

        setImage();
        initwidgets();


    }

    private void initwidgets() {

        mCaption = findViewById(R.id.postCaption);

        ImageView backarrow = findViewById(R.id.ivclosenext);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TextView share = findViewById(R.id.tvshare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caption = mCaption.getText().toString();


                if (intent.hasExtra(getString(R.string.selectedimg))) {

                    imgUrl = intent.getStringExtra(getString(R.string.selectedimg));

                    firebaseMethods.uploadnewphoto(getString(R.string.new_photo), caption, imgcount, imgUrl, null);

                } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                    bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));


                    firebaseMethods.uploadnewphoto(getString(R.string.new_photo), caption, imgcount, null, bitmap);
                }
            }
        });
    }

    private void setImage() {
        intent = getIntent();
        ImageView iv = findViewById(R.id.imgthumbnail);

        if (intent.hasExtra(getString(R.string.selectedimg))) {

            imgUrl = intent.getStringExtra(getString(R.string.selectedimg));
            UniversalImageLoader.setImage(imgUrl, iv, null, "file://");

        } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
            bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));

            iv.setImageBitmap(bitmap);
        }
    }

    private void setfirebaseauth() {
        firebaseMethods = new FirebaseMethods(mContext);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // checkcurrentuser(user );

            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                imgcount = firebaseMethods.getimagecount(dataSnapshot);
                // Toast.makeText(mContext, "" + imgcount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // auth.addAuthStateListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            auth.removeAuthStateListener(listener);
        }
    }

}