package com.arunsudharsan.socialnetwork.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.Home.HomeActivity;
import com.arunsudharsan.socialnetwork.Login.LoginActivity;
import com.arunsudharsan.socialnetwork.Profile.AccountSettingsActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.Photo;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.arunsudharsan.socialnetwork.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private StorageReference storageReference;
    private Context mContext;
    private double photouploadprogress = 0;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;
        storageReference = FirebaseStorage.getInstance().getReference();
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    /**
     * Register a new email and password to Firebase Authentication
     *
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        } else if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            //send Verification EMail
                            sendVerificationemail();

                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }


    public void sendVerificationemail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(mContext, "Couldnt Send Verification EMail..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void addNewUser(String email, String username, String description, String website, String profile_photo) {

        User user = new User(userID, 1, email, StringManipulation.condenseUsername(username));

        myRef.child(mContext.getString(R.string.dbusers))
                .child(userID)
                .setValue(user);


        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        myRef.child(mContext.getString(R.string.dbuseraccountsettings))
                .child(userID)
                .setValue(settings);

    }

    public UserSettings getUserSettings(DataSnapshot ds) {
        UserAccountSettings settings = new UserAccountSettings();


        for (DataSnapshot d : ds.getChildren()) {
            if (d.getKey().equals(mContext.getString(R.string.dbuseraccountsettings))) {
                if (d.child(userID).exists()) {
                    settings.setDescription(d.child(userID).getValue(UserAccountSettings.class).getDescription());
                    settings.setDisplayname(d.child(userID).getValue(UserAccountSettings.class).getDisplayname());
                    settings.setFollowers(d.child(userID).getValue(UserAccountSettings.class).getFollowers());
                    settings.setFollowing(d.child(userID).getValue(UserAccountSettings.class).getFollowing());
                    settings.setPosts(d.child(userID).getValue(UserAccountSettings.class).getPosts());
                    settings.setProfilephoto(d.child(userID).getValue(UserAccountSettings.class).getProfilephoto());
                    settings.setUsername(d.child(userID).getValue(UserAccountSettings.class).getUsername());
                    settings.setWebsite(d.child(userID).getValue(UserAccountSettings.class).getWebsite());

                }
            }

        }


        User user = new User();

        for (DataSnapshot d : ds.getChildren()) {
            if (d.getKey().equals(mContext.getString(R.string.dbusers))) {
                if (d.child(userID).exists()) {
                    user.setUsername(d.child(userID).getValue(User.class).getUsername());
                    user.setEmail(d.child(userID).getValue(User.class).getEmail());
                    user.setPhonenumber(d.child(userID).getValue(User.class).getPhonenumber());
                    user.setUserid(d.child(userID).getValue(User.class).getUserid());
                }
            }

        }


        return new UserSettings(user, settings);
    }

    public void updateUsername(String username) {

        myRef.child(mContext.getString(R.string.dbusers)).child(userID).child(mContext.getString(R.string.field_username)).setValue(username);
        myRef.child(mContext.getString(R.string.dbuseraccountsettings)).child(userID).child(mContext.getString(R.string.field_username)).setValue(username);

    }


    public void updateEmail(String email) {

        myRef.child(mContext.getString(R.string.dbusers)).child(userID).child(mContext.getString(R.string.field_email)).setValue(email);
    }

    public void updateuseraccountsettings(String displayname, String website, String description, Long phonenumber) {

        if (displayname != null)
            myRef.child(mContext.getString(R.string.dbuseraccountsettings)).child(userID).child(mContext.getString(R.string.field_display_name)).setValue(displayname);


        if (description != null)
            myRef.child(mContext.getString(R.string.dbuseraccountsettings)).child(userID).child(mContext.getString(R.string.field_description)).setValue(description);


        if (website != null)
            myRef.child(mContext.getString(R.string.dbuseraccountsettings)).child(userID).child(mContext.getString(R.string.field_website)).setValue(website);

        if (phonenumber != 0L)
            myRef.child(mContext.getString(R.string.dbusers)).child(userID).child(mContext.getString(R.string.field_phonenumber)).setValue(phonenumber);

    }

    public int getimagecount(DataSnapshot dataSnapshot) {
        int c = 0;
        for (DataSnapshot ds : dataSnapshot.child(mContext.getString(R.string.user_photos)).child(userID).getChildren()) {
            c++;
        }
        return c;
    }

    public void uploadnewphoto(final String phototype, final String caption, int imgcount, String imgUrl, Bitmap bm) {

        FilePaths filePaths = new FilePaths();
        if (phototype.equals(mContext.getString(R.string.new_photo))) {

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference reference = storageReference.child(filePaths.FirebaseImageStorage).child(user_id).child("photo" + (imgcount + 1));
            if (bm == null)
                bm = ImageManager.getBm(imgUrl);
            UploadTask task = null;
            byte[] bytes = ImageManager.getbytes(bm, 100);
            task = reference.putBytes(bytes);

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseuri = taskSnapshot.getDownloadUrl();
                    //  Toast.makeText(mContext, "Success"+firebaseuri.toString(), Toast.LENGTH_SHORT).show();
                    addimagetodb(caption, firebaseuri);
                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();

                }
            })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double Progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            if (Progress - 15 > photouploadprogress) {
                                Toast.makeText(mContext, "Uploading ... Progress:" + String.format("%.0f", Progress), Toast.LENGTH_SHORT).show();
                                photouploadprogress = Progress;
                            }
                        }
                    });


        } else if (phototype.equals(mContext.getString(R.string.profile_photo))) {

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference reference = storageReference.child(filePaths.FirebaseImageStorage).child(user_id).child("profile_photo");
            if (bm == null)
                bm = ImageManager.getBm(imgUrl);
            UploadTask task = null;
            byte[] bytes = ImageManager.getbytes(bm, 100);
            task = reference.putBytes(bytes);

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseuri = taskSnapshot.getDownloadUrl();

                    //insert into useraccountsettings

                    assert firebaseuri != null;
                    setprofilephoto(firebaseuri.toString());
                    ((AccountSettingsActivity) mContext).setViewPager(
                            ((AccountSettingsActivity) mContext).sectionsStatePagerAdapter
                                    .getFragmentNumber(mContext.getString(R.string.editprofile)));


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();

                }
            })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double Progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            if (Progress - 15 > photouploadprogress) {
                                Toast.makeText(mContext, "Uploading ... Progress:" + String.format("%.0f", Progress), Toast.LENGTH_SHORT).show();
                                photouploadprogress = Progress;
                            }
                        }
                    });


        }
    }

    private void setprofilephoto(String string) {
        myRef.child(mContext.getString(R.string.dbuseraccountsettings))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mContext.getString(R.string.profile_photo))
                .setValue(string);
    }

    private void addimagetodb(String caption, Uri firebaseuri) {
        String newphotokey = myRef.child(mContext.getString(R.string.dbphotos)).push().getKey();
        Photo photo = new Photo();
        String tags = " ";
        tags = StringManipulation.extracttags(caption);

        photo.setCaption(caption);
        photo.setImgpath(firebaseuri.toString());
        photo.setTags(tags);
        photo.setDatecreated(gettimestamp());
        photo.setUserid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setPhotoid(newphotokey);

        //insert into db
        myRef.child(mContext.getString(R.string.dbphotos)).child(newphotokey).setValue(photo);
        myRef.child(mContext.getString(R.string.dbuser_photos)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(newphotokey).setValue(photo);
    }

    private String gettimestamp() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        return sdf.format(new Date());
    }

}






