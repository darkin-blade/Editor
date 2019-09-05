package com.example.helloworld.functions;

import java.io.File;
import java.io.StringReader;

public class NewFile {
    public String newFile(String filePath, String fileName) {
        try {
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
