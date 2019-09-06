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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helloworld.functions.FileManager;
import com.example.helloworld.functions.GetPath;
import com.example.helloworld.functions.NewFile;
import com.example.helloworld.functions.ReadFile;
import com.example.helloworld.functions.WriteFile;


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
                // 新建文件
                NewFile tempNew = new NewFile();
                tempNew.newFile("/", MainActivity.this);
            }
        });

        Button testButton = findViewById(R.id.testButton);// `测试`按钮,测试用
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.editText1);
                WriteFile tempWrite = new WriteFile();
                String file_name = MainActivity.this.getExternalFilesDir(".").getAbsolutePath() + "/" + "temp0";
                int result = tempWrite.writeFile(text.getText().toString(), file_name);
                Toast.makeText(MainActivity.this, String.valueOf(result) + " " + file_name, Toast.LENGTH_LONG).show();
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
                path = tempPath.getPathFromUri(this, uri);

                // 读取文件内容并显示
                ReadFile tempRead = new ReadFile();
                EditText text = findViewById(R.id.editText1);
                tempRead.readFile(path, text);

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
