package com.example.helloworld.functions;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.helloworld.MainActivity;

/** 用于转换路径
 *
 */

public class GetPath extends FileManager {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPathFromUri(Uri uri, Context context) {
        Cursor cursor = null;
        String result = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projection,null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
