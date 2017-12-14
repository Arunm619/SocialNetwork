package com.arunsudharsan.socialnetwork.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.Share.ShareActivity;
import com.arunsudharsan.socialnetwork.dialogs.ConfirmPasswordDialog;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.arunsudharsan.socialnetwork.models.UserSettings;
import com.arunsudharsan.socialnetwork.utils.FirebaseMethods;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 13/12/17.
 */

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.onConfirmPasswordListener {
    private CircleImageView mprofilepic;
    //firebase

    private UserSettings mUsersettings;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private Context mContext;
    private EditText mDisplayname, mUsername, mwebsite, mdesc, memail, mphonenumber;
    private TextView mchangeprofilephoto;
    private String UserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editprofile, container, false);
        initwidgets(v);
        firebaseMethods = new FirebaseMethods(mContext);

        setfirebaseauth();

        //back button to profile acivityt
        ImageView backarrow = v.findViewById(R.id.ivbackarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        ImageView savedata = v.findViewById(R.id.saveChanges);
        savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveprofilesettings();
            }
        });

        return v;
    }

    private void initwidgets(View v) {
        mContext = getActivity();
        mprofilepic = v.findViewById(R.id.profilephoto);
        mDisplayname = v.findViewById(R.id.displayname);
        mUsername = v.findViewById(R.id.username);
        mwebsite = v.findViewById(R.id.website);
        mdesc = v.findViewById(R.id.description);
        memail = v.findViewById(R.id.email);
        mphonenumber = v.findViewById(R.id.phonenumber);

        mchangeprofilephoto = v.findViewById(R.id.changeProfilePhoto);
        mchangeprofilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
        getActivity().finish();
            }
        });

    }

    private void saveprofilesettings() {
        final String displayname = mDisplayname.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mwebsite.getText().toString();
        final Long phonenumber = Long.parseLong(mphonenumber.getText().toString());
        final String email = memail.getText().toString();
        final String description = mdesc.getText().toString();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!mUsersettings.getUser().getUsername().equals(username)) {
                    checkifnameusernameexists(username);
                }
                if (!mUsersettings.getSettings().getDisplayname().equals(displayname)) {

                    firebaseMethods.updateuseraccountsettings(displayname, null, null, 0L);

                }
                if (!mUsersettings.getSettings().getWebsite().equals(website)) {
                    firebaseMethods.updateuseraccountsettings(null, website, null, 0L);

                }
                if (!mUsersettings.getSettings().getDescription().equals(description)) {
                    firebaseMethods.updateuseraccountsettings(null, null, description, 0L);

                }


                if (!mUsersettings.getUser().getEmail().equals(email)) {
                    ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                    dialog.show(getFragmentManager(), getString(R.string.confirmpassworddialog));
                    dialog.setTargetFragment(EditProfileFragment.this, 2);
                }
                if (!Long.valueOf(mUsersettings.getUser().getPhonenumber()).equals(phonenumber)) {
                    firebaseMethods.updateuseraccountsettings(null, null, null, phonenumber);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkifnameusernameexists(final String username) {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child(getString(R.string.dbusers))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
//add the username
                    firebaseMethods.updateUsername(username);
                    Toast.makeText(mContext, "Username saved!", Toast.LENGTH_SHORT).show();
                }

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Toast.makeText(mContext, "That username Already Exists!", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        UserId = auth.getCurrentUser().getUid();
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
        mUsersettings = usersettings;
//        User user = usersettings.getUser();
        UserAccountSettings userAccountSettings = usersettings.getSettings();


        UniversalImageLoader.setImage(userAccountSettings.getProfilephoto(), mprofilepic, null, "");

        mDisplayname.setText(userAccountSettings.getDisplayname());
        mUsername.setText(userAccountSettings.getUsername());
        mwebsite.setText(userAccountSettings.getWebsite());
        mdesc.setText(userAccountSettings.getDescription());
        memail.setText(String.valueOf(usersettings.getUser().getEmail()));

        mphonenumber.setText(String.valueOf(usersettings.getUser().getPhonenumber()));

    }

    @Override
    public void onConfirmPassword(String Password) {
        final FirebaseUser user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), Password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(mContext, "Re-Authentication Failed!", Toast.LENGTH_SHORT).show();
                } else {
                    final String email = memail.getText().toString();
                    auth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getProviders().size() == 1) {
                                    Toast.makeText(getActivity(), "Email Already in Use!", Toast.LENGTH_SHORT).show();
                                } else

                                {
                                    Toast.makeText(getActivity(), "Email is Available", Toast.LENGTH_SHORT).show();
                                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                firebaseMethods.updateEmail(email);
                                                Toast.makeText(getActivity(), "Email is Updated...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });

                }
            }
        });

    }
}
