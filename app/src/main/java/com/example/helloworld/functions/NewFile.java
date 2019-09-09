package com.example.helloworld.functions;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class NewFile extends FileManager {// 继承用于打印信息
    public int newFile(String file_path, Activity activity) {// 创建临时文件,返回新建临时文件的编号
        try {
            if (file_path.equals("")) {// 自动创建临时文件
                // TODO 创建目录
                String dir_path = activity.getExternalFilesDir(".").getAbsolutePath() + "/";// TODO 获取app目录
                Log.i("fuck parent", dir_path);
                File dir = new File(dir_path);// 打开目录
                if (!dir.exists()) {// 目录不存在
                    if (!dir.mkdirs()) {//  创建多重目录
                        showResult("mkdir " + dir_path + " failed", activity);
                        return -1;// 创建目录失败,直接返回
                    } else {// 创建新的目录
                        Log.i("newFile", "mkdir succeed");
                    }
                } else {// 目录已存在
                    Log.i("newFile", "dir already exists");
                }

                // 创建文件
                String file_name = dir_path + "temp";// 临时文件名
                int file_num = 0;// 临时文件编号
                File tempFile = new File(file_name + file_num);
                while (tempFile.exists()) {// 文件存在
                    file_num ++;// 一直找到一个不存在的文件名
                    tempFile = new File(file_name + file_num);
                    if (file_num >= 100) {// 临时文件数量限定,防止死循环
                        return -1;// TODO 不允许创建
                    }
                }
                if (!tempFile.createNewFile()) {// 创建临时文件
                    showResult("create " + file_name + file_num + " failed", activity);// 创建文件失败
                    return -1;
                } else {// 创建临时文件成功
                    showResult("create " + file_name + file_num + " succeed", activity);// 创建文件成功
                    return file_num;// TODO 返回临时文件的编号
                }
            } else {// TODO 在指定位置创建文件
                Panic();// TODO 调用下面的函数
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showResult("error", activity);
        return -1;
    }

    protected int newFileOfPath(String file_path, Activity activity) {// 在指定位置创建文件
        try {
            File file = new File(file_path);

            // 创建父目录
            String dir_path = file.getParent();
            Log.i("fuck parent", dir_path);
            File dir = new File(file.getParent());
            if (!dir.exists()) {// 目录不存在
                if (!dir.mkdirs()) {//  创建多重目录
                    showResult("mkdir " + dir_path + " failed", activity);
                    return -1;// 创建目录失败,直接返回
                } else {// 创建新的目录
                    Log.i("newFile", "mkdir succeed");
                }
            } else {// 目录已存在
                Log.i("newFile", "dir already exists");
            }

            // 创建子文件
            if (file.exists()) {// 文件已存在,直接报错
                showResult(file_path + " already exists", activity);
                return -1;// TODO
            } else {// 创建新的文件
                if (!file.createNewFile()) {// 创建失败
                    showResult("create " + file_path + " failed", activity);// 创建文件失败
                    return -1;
                } else {// 创建成功
                    showResult("create " + file_path + " succeed", activity);// 创建文件成功
                    return 0;
                }

            }    } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;// 创建失败
    }
}
