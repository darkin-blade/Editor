package com.example.helloworld.functions;

import android.app.Activity;
import android.widget.Toast;

/** 所有文件操作类的父类
 *
 */

public class FileManager {
    protected void showResult(String result, Activity activity) {// 打印信息
        Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
        return;
    }

    public void Panic() {
        if (true) {
            new AssertionError("fuck you");
        }
    }
}
