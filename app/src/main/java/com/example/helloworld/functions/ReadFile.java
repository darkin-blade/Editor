package com.example.helloworld.functions;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile {
    public void readFile(String file) {
        try {
            FileInputStream file_input = new FileInputStream(file);
            byte[] file_content = new byte[1024];
            file_input.read(file_content);
            String read_content = new String(file_content, "utf-8");// 对读取的数据进行编码
            Log.i("file content:", read_content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
