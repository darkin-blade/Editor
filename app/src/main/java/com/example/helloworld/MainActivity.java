package com.example.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import com.example.helloworld.functions.GetPath;
import com.example.helloworld.functions.NewFile;
import com.example.helloworld.functions.ReadFile;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 首次检察权限
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int check_result = ActivityCompat.checkSelfPermission(this, permission);// `允许`返回0,`拒绝`返回-1
        if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);// 获取`写`权限
        }

        // 打开文件系统
        Button openBtn = findViewById(R.id.openButton);// `打开`按钮
        openBtn.setOnClickListener(new View.OnClickListener() {// 点击`打开`按钮
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");// 所有文件
                // intent.setType("image/*");// 图片
                // intent.setType("audio/*");// 音频
                // intent.setType("video/*");// 视频
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        Button picBtn = findViewById(R.id.picButton);// `图片`按钮
        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);//intent  action属性
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        Button newBtn = findViewById(R.id.newButton);// `新建`按钮
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sd_path = MainActivity.this.getExternalFilesDir(".").getAbsolutePath();// 获取内置存储目录
                Log.i("sdcard", sd_path);
                // 新建文件
                NewFile tempNew = new NewFile();
                tempNew.newFile(sd_path + "/Editor/", MainActivity.this);
            }
        });

        Button testButton = findViewById(R.id.testButton);// `测试`按钮,测试用
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, MainActivity.this.getExternalFilesDir(".").getAbsolutePath(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "internal:" + System.getenv("EXTERNAL_STORAGE"), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// 回调方法
        if (resultCode == Activity.RESULT_OK) {
            String path = null;
            if (requestCode == 1) {// `打开`按钮
                Uri uri = data.getData();

                // 转换文件路径为绝对路径
                GetPath tempPath = new GetPath();
                path = tempPath.getPathFromUri(uri, this);
                // 读取文件内容
//                ReadFile tempRead = new ReadFile();
//                tempRead.readFile(path);
            } else if (requestCode == 2) {// `图片`按钮
                Uri uri = data.getData();
                path = uri.getPath();
            }
            if (path != null) {// path非空
                Log.i("onActivityResult", path);
                Toast.makeText(this, path, Toast.LENGTH_LONG).show();
            }
        }
    }
}
