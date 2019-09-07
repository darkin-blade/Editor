package com.example.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.helloworld.functions.FileManager;
import com.example.helloworld.functions.GetPath;
import com.example.helloworld.functions.NewFile;
import com.example.helloworld.functions.ReadFile;
import com.example.helloworld.functions.WriteFile;


public class MainActivity extends AppCompatActivity {

    String[] current_file = new String[5];// 最多同时打开5个文件
    int file_total_num = 0;// 当前打开的文件总数
    int file_cur_num = 0;// 当前窗口的文件编号

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

        // 调用系统文件管理
        final Button openBtn = findViewById(R.id.openButton);// `打开`按钮
        openBtn.setOnClickListener(new View.OnClickListener() {// 点击`打开`按钮
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");// 所有文件
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        Button picBtn = findViewById(R.id.picButton);// TODO 测试按钮
        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {// TODO 点击后按钮向右移动
                ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.buttons), "translationX", 240);
                animator.setDuration(250);
                animator.start();
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

        Button saveButton = findViewById(R.id.saveButton);// `保存`按钮
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_file[0] == null) {// 没有打开任何文件
                    Toast.makeText(MainActivity.this, "fuck", Toast.LENGTH_SHORT).show();
                } else {
                    // 将EditText的内容写入当前文件
                    EditText text = findViewById(R.id.editText1);
                    WriteFile tempWrite = new WriteFile();
                    String file_name = current_file[0];// TODO 当前文件
                    int result = tempWrite.writeFile(text.getText().toString(), file_name);
                    if (result == 0) {// 保存成功
                        Toast.makeText(MainActivity.this, file_name + " save succeed", Toast.LENGTH_LONG).show();
                    } else {// 保存失败
                        Toast.makeText(MainActivity.this, file_name + " save failed", Toast.LENGTH_LONG).show();
                    }
                }
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
                int result = tempRead.readFile(path, text);

                // 读取文件路径
                if (result != -1) {// 打开文件成功
                    current_file[0] = path;// TODO 保存路径
                    Toast.makeText(this, path + " open succeed", Toast.LENGTH_LONG).show();
                } else {// 打开文件失败
                    Toast.makeText(this, path + " open failed", Toast.LENGTH_LONG).show();
                }

            } else if (requestCode == 2) {// TODO
                ;
            }
        }
    }
}
