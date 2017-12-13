package com.arunsudharsan.socialnetwork.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arunsudharsan.socialnetwork.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by root on 13/12/17.
 */

public class GridImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mAppend;
    private int layoutResource;
    private ArrayList<String> imgurls;

    public GridImageAdapter(Context mContext, int layoutResource, String mAppend, ArrayList<String> imgurls) {
        super(mContext, layoutResource, imgurls);
        this.mContext = mContext;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAppend = mAppend;
        this.layoutResource = layoutResource;
        this.imgurls = imgurls;
    }

    private static class ViewHolder {
        SquareImageView img;
        ProgressBar progressBar;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.progressBar = convertView.findViewById(R.id.gridimgprogressbar);
            holder.img = convertView.findViewById(R.id.gridimgview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imgurl = getItem(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mAppend + imgurl, holder.img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);

                }
            }
        });
        return convertView;
    }
}
