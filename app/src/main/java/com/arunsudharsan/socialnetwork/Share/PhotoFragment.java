package com.arunsudharsan.socialnetwork.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arunsudharsan.socialnetwork.Profile.AccountSettingsActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.Permissions;

/**
 * Created by root on 13/12/17.
 */

public class PhotoFragment extends Fragment {
    private int GALLERY_FRAGMENT = 0;
    private int PHOTO_FRAGMENT = 1;
    private int Camerarequestcode = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);

        Button btnlaunchcamera = v.findViewById(R.id.btn_opencamera);
        btnlaunchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((ShareActivity) getActivity()).getcurrentabnumber() == PHOTO_FRAGMENT) {
                    if (((ShareActivity) getActivity()).checkPermission(Permissions.CAMERA_PERMISSION[0])) {
                        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraintent, Camerarequestcode);

                    } else {
                        startActivity(new Intent(getActivity(), ShareActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camerarequestcode) {
            Bitmap bm;
            bm = (Bitmap) data.getExtras().get("data");

            if (isRoottask()) {

                try {

                    Intent intent = new Intent(getActivity(), NextActivity.class);

                    intent.putExtra(getString(R.string.selected_bitmap), bm);
                    startActivity(intent);

                } catch (NullPointerException ignored) {
                }

            } else {

                try {

                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);

                    intent.putExtra(getString(R.string.selected_bitmap), bm);
                    intent.putExtra(getString(R.string.returntofragment), getString(R.string.editprofile));
                    startActivity(intent);
                    getActivity().finish();
                } catch (NullPointerException ignored) {
                }
            }
        }
    }

    private boolean isRoottask() {
        if ((((ShareActivity) getActivity()).gettask() == 0)) {
            return true;
        } else {
            return false;
        }
    }
}

