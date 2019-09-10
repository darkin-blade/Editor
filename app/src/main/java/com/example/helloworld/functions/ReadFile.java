package com.example.helloworld.functions;

import android.util.Log;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/** 用于读取文件内容
 *
 */

public class ReadFile extends FileManager {
    public int readFile(String path, EditText text) {
        if (path == null) {// TODO
            return -1;
        }

        File file = new File(path);
        if (!file.exists()) {// TODO 文件不存在
            return -1;
        }

        try {
            int file_length = (int) file.length();// 计算文件长度
            if (file_length + 16 < 0) {// TODO 溢出
                new AssertionError("file length error");
            }
            byte[] file_content = new byte[file_length];// TODO 大小不够

            // 读取文件内容
            Log.i("read file", path);
            RandomAccessFile raFile = new RandomAccessFile(file, "r");
            raFile.read(file_content);

            // 将byte转成char
            Charset cSet = Charset.forName("UTF-8");
            ByteBuffer bBuffer = ByteBuffer.allocate(file_length);
            bBuffer.put(file_content);
            bBuffer.flip();
            CharBuffer cBuffer = cSet.decode(bBuffer);
            Log.i("file length", file_length + "===" + cBuffer.array().length);
            text.setText(cBuffer.array(), 0, cBuffer.length());// 防止越界

            return 0;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
