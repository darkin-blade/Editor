package com.example.helloworld.functions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;

public class NewFile {
    public void newFile(String file_path, Activity activity) {
        String file_result = "error";
        try {
            // 重新检察权限
            String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            int check_result = ActivityCompat.checkSelfPermission(activity, permission);// `允许`返回0,`拒绝`返回-1
            if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);// 获取`写`权限
            }

            // 创建目录
            File dir = new File(file_path);
            if (!dir.exists()) {// 目录不存在
                if (!dir.mkdir()) {// 创建目录
                    showResult("mkdir " + file_path + " failed", activity);
                    return;// 创建目录失败,直接返回
                } else {// 创建新的目录
                    Log.i("newFile", "mkdir succeed");
                }
            } else {// 目录已存在
                Log.i("newFile", "dir already exists");
            }

            // 创建文件
            String file_name = "temp";// 临时文件名
            int file_num = 0;// 临时文件编号
            File tempFile = new File(file_path + file_name + file_num);
            while (tempFile.exists()) {// 文件存在
                file_num ++;// 一直找到一个不存在的文件名
                tempFile = new File(file_path + file_name + file_num);
                if (file_num > 100) {// 临时文件数量限定,防止死循环
                    throw new AssertionError("dead loop");
                }
            }

            if (!tempFile.createNewFile()) {// 创建临时文件
                showResult("create " + file_path + file_name + file_num + " failed", activity);// 创建文件失败
            } else {
                showResult("create " + file_path + file_name + file_num + " succeed", activity);// 创建文件成功
            }
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showResult("error", activity);
        return;
    }

    private void showResult(String result, Activity activity) {// 打印信息
        Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
        return;
    }
}
