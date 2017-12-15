package com.arunsudharsan.socialnetwork.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.models.Comment;
import com.arunsudharsan.socialnetwork.models.UserAccountSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 16/12/17.
 */

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private LayoutInflater mLayoutInflater;
    private int layoutres;
    private Context mcontext;

    public CommentListAdapter(@NonNull Context context, int resource,
                              @NonNull List<Comment> objects) {
        super(context, resource, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mcontext = context;
        layoutres = resource;
    }

    private static class ViewHolder {
        TextView comment, username, timestamp, reply, likescount;
        ImageView likeimageview;
        CircleImageView profile;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(layoutres, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.comment = convertView.findViewById(R.id.comment);
            viewHolder.username = convertView.findViewById(R.id.comment_username);
            viewHolder.timestamp = convertView.findViewById(R.id.comment_timeposted);
            viewHolder.reply = convertView.findViewById(R.id.comment_reply);
            viewHolder.likescount = convertView.findViewById(R.id.comment_likes);
            viewHolder.likeimageview = convertView.findViewById(R.id.comment_like);
            viewHolder.profile = convertView.findViewById(R.id.commentprofileimage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();


        }

        //set COMMENTS
        viewHolder.comment.setText(getItem(position).getComment());
        //set TIMESTAMP
        String timestamp = getTimestampdiffernce(getItem(position));
        if (!timestamp.equals("0"))
            viewHolder.timestamp.setText(timestamp + "d");

        else
            viewHolder.timestamp.setText("Today");

//set Username and profile
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(mcontext.getString(R.string.dbuseraccountsettings))
                .orderByChild("userid")
                .equalTo(getItem(position).getUserid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    viewHolder.username.setText(ds.getValue(UserAccountSettings.class)
                            .getUsername());
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(ds.getValue(UserAccountSettings.class)
                            .getProfilephoto(), viewHolder.profile);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        try {
            if (position == 0) {
                viewHolder.likeimageview.setVisibility(View.GONE);
                viewHolder.likescount.setVisibility(View.GONE);
                viewHolder.reply.setVisibility(View.GONE);

            }

        } catch (NullPointerException e) {

        }
        return convertView;
    }

    private String getTimestampdiffernce(Comment comment) {
        String difference = "";

//simple date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        //setting today
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        sdf.format(today);
        //getting timnestampfrom photo
        final String mphototimestamp = comment.getDatecreated();
        Date timestamp;

        try {
            timestamp = sdf.parse(mphototimestamp);

            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
        } catch (ParseException ignored) {
            difference = "0";
        }
        return difference;
    }

}
