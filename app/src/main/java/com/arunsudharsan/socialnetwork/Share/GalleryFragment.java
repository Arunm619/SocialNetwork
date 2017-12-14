package com.arunsudharsan.socialnetwork.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.Profile.AccountSettingsActivity;
import com.arunsudharsan.socialnetwork.R;
import com.arunsudharsan.socialnetwork.utils.FilePaths;
import com.arunsudharsan.socialnetwork.utils.FileSearch;
import com.arunsudharsan.socialnetwork.utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by root on 13/12/17.
 */

public class GalleryFragment extends Fragment {

    private GridView gridview;
    private ImageView ivgalleryimg;
    private ProgressBar progressBar;
    private Spinner dirSpinner;

    private ArrayList<String> dir;
    private int NUMCOLUMN = 3;
    private String mselectedimg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

//        dir = new ArrayList<>();

        ivgalleryimg = v.findViewById(R.id.galleryimgview);
        gridview = v.findViewById(R.id.gallerygridview);
        progressBar = v.findViewById(R.id.progressbargallerygrid);
        dirSpinner = v.findViewById(R.id.spinnerdirectory);
        progressBar.setVisibility(View.GONE);


        ImageView shareclose = v.findViewById(R.id.ivcloseshare);
        shareclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        TextView nextscreen = v.findViewById(R.id.next);
        nextscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRoottask()) {
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selectedimg), mselectedimg);

                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);

                    // Toast.makeText(getActivity(), ""+mselectedimg, Toast.LENGTH_SHORT).show();
                    intent.putExtra(getString(R.string.selectedimg), mselectedimg);
                    intent.putExtra(getString(R.string.returntofragment), getString(R.string.editprofile));
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        init();
        return v;
    }

    private boolean isRoottask() {
        if ((((ShareActivity) getActivity()).gettask() == 0)) {
            return true;
        } else {
            return false;
        }
    }

    private void init() {
        dir = new ArrayList<>();
        ArrayList<String> dirnames = new ArrayList<>();

        FilePaths filePaths = new FilePaths();
        if (FileSearch.getDirectoryPath(filePaths.PICTURES) != null) {
            dir = FileSearch.getDirectoryPath(filePaths.PICTURES);

        }

        for (int i = 0; i < dir.size(); i++) {
            int index = dir.get(i).lastIndexOf("/");
            String string = dir.get(i).substring(index);
            dirnames.add(string);

        }


        dir.add(filePaths.CAMERA);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dirnames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dirSpinner.setAdapter(adapter);
        dirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupgridview(dir.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupgridview(String selecteddir) {
        final ArrayList<String> imgurls = FileSearch.getFilesPath(selecteddir);
        int gridwidth = getResources().getDisplayMetrics().widthPixels;
        int imgwidth = gridwidth / NUMCOLUMN;
        gridview.setColumnWidth(imgwidth);

        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, "file://", imgurls);
        gridview.setAdapter(adapter);

        setimage(imgurls.get(0), ivgalleryimg, "file:/");
        mselectedimg = imgurls.get(0);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mselectedimg = imgurls.get(i);
                setimage(mselectedimg, ivgalleryimg, "file:/");

            }
        });
    }

    private void setimage(String imgurl, ImageView iv, String append) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgurl, iv, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
