package com.arunsudharsan.socialnetwork.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by root on 15/12/17.
 */

public class ImageManager {
    public static Bitmap getBm(String url) {

        File file = new File(url);
        FileInputStream stream = null;
        Bitmap bm = null;
        try {
            stream = new FileInputStream(file);
            bm = BitmapFactory.decodeStream(stream);

        } catch (FileNotFoundException ignored) {
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ignored) {

            }
            return bm;
        }
    }

    public static byte[] getbytes(Bitmap bitmap,int quality)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);

        return stream.toByteArray();
    }
}
