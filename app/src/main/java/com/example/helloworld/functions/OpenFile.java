package com.example.helloworld.functions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

public class OpenFile {
    public String openFile (Intent intent, Context context, EditText text) {// TODO view
        Uri uri = intent.getData();

        // 获取打开的文件地址
        GetPath tempPath = new GetPath();
        String path = tempPath.getPathFromUri(context, uri);

        // 读取打开的文件内容
        ReadFile tempRead = new ReadFile();
        int result = tempRead.readFile(path, text);// 如果文件不存在則会返回-1

        // 读取文件路径
        if (result != -1) {// 打开文件成功
            return path;
        } else {// 打开文件失败
            return null;
        }
    }
}
