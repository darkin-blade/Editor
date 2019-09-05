package com.example.helloworld.functions;

import android.content.Context;
import android.net.Uri;

/** 用于转换路径
 *
 */

public class GetPath extends FileManager {
    public String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {// uri以content://开头
            try {
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// uri以file://开头
            return uri.getPath();
        }
        return null;
    }
}
