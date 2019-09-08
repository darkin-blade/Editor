package com.example.helloworld.functions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.helloworld.MainActivity;

import java.io.File;

/** 用于创建新的文件
 *
 */

public class NewFile extends FileManager {// 继承用于打印信息
    public int newFile(String file_path, Activity activity) {
        String file_result = "error";
        try {
            if (file_path.equals("")) {// 自动创建临时文件
                // TODO 创建目录
                String dir_path = activity.getExternalFilesDir(".").getAbsolutePath();
                file_path = activity.getExternalFilesDir(".").getAbsolutePath() + "/" + file_path;// 获取app目录
                File dir = new File(dir_path);
                if (!dir.exists()) {// 目录不存在
                    if (!dir.mkdir()) {// 创建目录
                        showResult("mkdir " + file_path + " failed", activity);
                        return -1;// 创建目录失败,直接返回
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
                    return -1;
                } else {
                    showResult("create " + file_path + file_name + file_num + " succeed", activity);// 创建文件成功
                    return 0;
                }
            } else {// TODO 在指定位置创建文件
                ;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showResult("error", activity);
        return -1;
    }
}
