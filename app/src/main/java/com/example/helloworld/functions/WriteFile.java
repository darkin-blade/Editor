package com.example.helloworld.functions;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/** 用于写入文件
 *
 */

public class WriteFile extends FileManager {
    public int writeFile(String content, String file_path) {
        File file = new File(file_path);
        if (!file.exists()) {// 若文件不存在
            Log.i(file_path, "not exist");
            return -1;
        }
        try {
            int origin_length = (int) file.length();// 文件原先的大小
            int new_length = (int) content.getBytes().length;// 新的大小
            Log.i("content length", origin_length + "<==" + new_length);

            if (origin_length > new_length) {// 需要删除原文件
                file.delete();
                file.createNewFile();
            }
            // 写入文件
            RandomAccessFile raFile = new RandomAccessFile(file, "rw");
            raFile.write(content.getBytes());// 写入文件
            raFile.close();

            return 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
