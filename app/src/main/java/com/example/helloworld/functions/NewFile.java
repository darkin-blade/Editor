package com.example.helloworld.functions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.StringReader;

public class NewFile {
    public String newFile(String filePath, String fileName, Activity activity) {
        try {
            // 重新检察权限
            String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            int check_result = ActivityCompat.checkSelfPermission(activity, permission);// `允许`返回0,`拒绝`返回-1
            if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);// 获取`写`权限
            }
            // 创建目录
            File dir = new File(filePath);
            if (!dir.exists()) {// 目录不存在
                if (dir.mkdir()) {// 创建目录
                    return "mkdir success";
                } else {
                    return "mkdir failed";
                }
            } else {
                return "dir already exists";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "error";
    }
}
