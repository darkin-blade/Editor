package com.example.helloworld.functions;

import android.util.Log;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/** 用于读取文件内容
 *
 */

public class ReadFile extends FileManager {
    public int readFile(String file_path, EditText text) {
        File file = new File(file_path);
        Log.i(file_path = " length", String.valueOf(file.length()));

        if (!file.exists()) {// TODO 文件不存在
            return -1;
        }
        try {
            int file_length = (int) file.length();// 计算文件长度
            if (file_length + 16 < 0) {// TODO 溢出
                new AssertionError("file length error");
            }
            byte[] file_content = new byte[file_length + 16];// TODO 大小不够

            // 读取文件内容
            RandomAccessFile raFile = new RandomAccessFile(file, "r");
            raFile.read(file_content);
            text.setText(file_content.toString().toCharArray(), 0, file_length);
            return 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
