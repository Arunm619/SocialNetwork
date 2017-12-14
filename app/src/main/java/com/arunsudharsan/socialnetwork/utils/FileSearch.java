package com.arunsudharsan.socialnetwork.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by root on 14/12/17.
 */

public class FileSearch {

    public static ArrayList<String> getDirectoryPath(String Directory) {
        ArrayList<String> path = new ArrayList<>();
        File file = new File(Directory);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                path.add(files[i].getAbsolutePath());


            }
        }
        return path;
    }

    public static ArrayList<String> getFilesPath(String Directory) {
        ArrayList<String> path = new ArrayList<>();
        File file = new File(Directory);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                path.add(files[i].getAbsolutePath());


            }
        }
        return path;
    }
}
