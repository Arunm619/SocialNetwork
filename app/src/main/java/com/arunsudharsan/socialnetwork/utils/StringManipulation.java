package com.arunsudharsan.socialnetwork.utils;

/**
 * Created by root on 14/12/17.
 */

public class StringManipulation {
    public static String expandUsername(String name) {
        return name.replace(".", " ");
    }

    public static String condenseUsername(String name) {
        return name.replace(" ", ".");
    }

    public static String extracttags(String string) {
        if (string.indexOf("#") > 0) {
            StringBuilder sb = new StringBuilder();
            char[] charArray = string.toCharArray();
            boolean foundword = false;
            for (char c : charArray) {
                if (c == '#') {
                    foundword = true;
                    sb.append(c);
                } else if (foundword) {
                    sb.append(c);
                }
                if (c == ' ') {
                    foundword = false;

                }
            }

            String s = sb.toString().replace(" ", "").replace("#", ",#");
            return s.substring(1, s.length());
        }
return string;
    }


}
