package com.arunsudharsan.socialnetwork.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by root on 13/12/17.
 */

public class EditProfileFragment extends Fragment {
    ImageView mprofilepic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editprofile, container, false);

        mprofilepic = v.findViewById(R.id.profilephoto);
       //back button to profile acivityt
        ImageView backarrow = v.findViewById(R.id.ivbackarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setimage();
        return v;
    }



    private void setimage() {
        String url = "scontent.fmaa3-1.fna.fbcdn.net/v/t1.0-9/15356598_1802393030089473_486476897344407993_n.jpg?oh=17c4a456b48dce097691941f7f90faef&oe=5AD74B84";
        UniversalImageLoader.setImage(url, mprofilepic, null, "https://");
    }
}
