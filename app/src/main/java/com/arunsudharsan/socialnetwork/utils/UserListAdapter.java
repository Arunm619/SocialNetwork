package com.arunsudharsan.socialnetwork.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.User;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 16/12/17.
 */

public class UserListAdapter extends ArrayAdapter<User> {

    private LayoutInflater inflater;
    private List<User> musers;
    private int layoutresource;
    private Context mContext;

    public UserListAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {

        super(context, resource, objects);
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutresource = resource;
        this.musers = objects;
    }

    private static class Viewholder {
        TextView username, email;
        CircleImageView profile;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Viewholder viewholder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutresource, parent, false);
            viewholder = new Viewholder();
            viewholder.username = convertView.findViewById(R.id.username);
            viewholder.email = convertView.findViewById(R.id.email);
            viewholder.profile = convertView.findViewById(R.id.profileimage);
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();

        }

        viewholder.username.setText(getItem(position).getUsername());
        viewholder.email.setText(getItem(position).getEmail());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query
                = reference.child(mContext.getString(R.string.dbuseraccountsettings))
                .orderByChild(mContext.getString(R.string.userid)).equalTo(getItem(position).getUserid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(ds.getValue(UserAccountSettings.class)
                            .getProfilephoto(), viewholder.profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return convertView;
    }
}
