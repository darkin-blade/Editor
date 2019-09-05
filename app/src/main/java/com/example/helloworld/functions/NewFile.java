package com.example.helloworld.functions;

import java.io.File;

public class NewFile {
    public boolean newFile(String filePath, String fileName) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {// 目录不存在
                return dir.mkdir();// 创建目录
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
