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
