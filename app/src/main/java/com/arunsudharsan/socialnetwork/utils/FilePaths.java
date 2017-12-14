package com.arunsudharsan.socialnetwork.utils;

import android.os.Environment;

/**
 * Created by root on 14/12/17.
 */

public class FilePaths {
    public String ROOT_DIR= Environment.getExternalStorageDirectory().getPath();
    public String CAMERA = ROOT_DIR+ "/DCIM/camera";
    public String PICTURES = ROOT_DIR+ "/Pictures";

    public String FirebaseImageStorage ="photos/users/";
}
