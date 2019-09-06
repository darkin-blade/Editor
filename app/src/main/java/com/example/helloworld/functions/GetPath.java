package com.example.helloworld.functions;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.loader.content.CursorLoader;

import com.example.helloworld.MainActivity;

/** 用于转换路径
 *
 */

public class GetPath extends FileManager {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPathFromUri(Uri uri, Context context) {// TODO
        String[] path = uri.getPath().split(":");
        if (path.length != 2) {
            return null;// TODO 异常处理
        }
        String sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();// 获取内置存储目录
        return sd_path + "/" + path[1];
    }

}
