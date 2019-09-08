package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.helloworld.functions.GetPath;
import com.example.helloworld.functions.NewFile;
import com.example.helloworld.functions.ReadFile;
import com.example.helloworld.functions.WriteFile;

import java.io.File;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    String[] current_temp = new String[5];// 所有打开文件对应的临时文件
    String[] current_file = new String[5];// 最多支持打开5个文件
    int file_total_num = 0;// TODO 当前打开的文件总数
    int file_cur_num = -1;// TODO 当前窗口的文件编号
    int buttonMove = 240;// 所有button一起移动的水平参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();// 判断本软件启动的方式
        if (action.equals("android.intent.action.VIEW")) {// 由其他软件打开本软件
            Uri uri = intent.getData();

            // 获取打开的文件地址
            GetPath tempPath = new GetPath();
            String path = tempPath.getPathFromUri(MainActivity.this, uri);// 将uri转成路径

            // 读取打开的文件内容
            ReadFile tempRead = new ReadFile();
            EditText text = findViewById(R.id.editText1);
            int result = tempRead.readFile(path, text);// 如果文件不存在则会返回-1

            // 加载文件路径
            loadFile(path, result);
        }

        // 首次检察权限
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int check_result = ActivityCompat.checkSelfPermission(this, permission);// `允许`返回0,`拒绝`返回-1
        if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);// 获取`写`权限
        }

        createBtn();// 为所有按钮绑定点击事件

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// 回调方法
        if (resultCode == Activity.RESULT_OK) {
            String path = null;
            if (requestCode == 1) {// `打开`按钮
                Uri uri = data.getData();

                // 转换文件路径为绝对路径
                GetPath tempPath = new GetPath();
                path = tempPath.getPathFromUri(this, uri);// 将uri转成路径

                // 读取文件内容并显示
                ReadFile tempRead = new ReadFile();
                EditText text = findViewById(R.id.editText1);
                int result = tempRead.readFile(path, text);// 如果文件不存在則会返回-1

                // 读取文件路径 TODO
                loadFile(path, result);
            } else {// TODO
                ;
            }
        }
    }

    @Override
    protected void onPause() {// TODO 用于临时保存数据
        super.onPause();

        if (current_temp[0] == null) {// TODO 没有打开任何文件
            return;// TODO
        } else {// TODO 检查当前文件是否为临时文件
            File file = new File(current_temp[0]);
            String dir_path = file.getParent();// TODO 不包含"/."
            if (!Pattern.matches(dir_path + "(/.)*(/)*", MainActivity.this.getExternalFilesDir(".").getAbsolutePath())) {// 前者包含"/.
                Log.i("fuck " + dir_path, MainActivity.this.getExternalFilesDir(".").getAbsolutePath());
                new AssertionError(current_temp[0]);// 所有打开的文件都对应一个临时文件,不可能出现错误
            }

            // 将EditText的内容写入当前文件
            EditText text = findViewById(R.id.editText1);
            WriteFile tempWrite = new WriteFile();
            String file_name = current_temp[0];// TODO 当前文件
            int result = tempWrite.writeFile(text.getText().toString(), file_name);
            if (result == 0) {// 保存成功
                Toast.makeText(MainActivity.this, file.getName() + " temp save", Toast.LENGTH_LONG).show();
            } else {// 保存失败
                Toast.makeText(MainActivity.this, file.getName() + " error", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void loadFile(String file_name, int result) {// TODO `统计已经加载的文件`对外接口
        if (result == 0) {// 文件打开成功
            file_cur_num = 0;// TODO 默认-1
            current_temp[file_cur_num] = file_name.replace("/.", "");// TODO 保存路径到当前文件编号,删除多余"/."
            Toast.makeText(this, file_name + " open succeed", Toast.LENGTH_LONG).show();
        } else {// 文件打开失败
            Toast.makeText(this, file_name + " open failed", Toast.LENGTH_LONG).show();
        }
    }

    private void createBtn() {// 为所有按钮绑定点击事件
        // 调用系统文件管理
        Button openBtn = findViewById(R.id.openButton);// `打开`按钮
        openBtn.setOnClickListener(new View.OnClickListener() {// 点击`打开`按钮
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");// 所有文件
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        Button picBtn = findViewById(R.id.ctrlButton);// TODO 控制`隐藏/显示`按钮
        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 隐藏/显示所有按钮
                ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.buttons), "translationX", buttonMove + 240);
                animator.setDuration(250);
                animator.start();
                buttonMove = -buttonMove;
                // 修改`隐藏/显示`按钮样式
                Button temp = findViewById(R.id.ctrlButton);
                if (buttonMove == -240) {// 此时为`显示`按钮
                    temp.setBackgroundResource(R.drawable.button_show);
                } else {// `隐藏`按钮
                    temp.setBackgroundResource(R.drawable.button_hide);
                }
            }
        });

        Button newBtn = findViewById(R.id.newButton);// `新建`按钮
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 新建文件
                NewFile tempNew = new NewFile();
                int temp_num = tempNew.newFile("", MainActivity.this);// 获取临时文件编号
            }
        });

        Button saveBtn = findViewById(R.id.saveButton);// `保存`按钮
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_temp[0] == null) {// TODO 没有打开任何文件
                    Toast.makeText(MainActivity.this, "TODO save", Toast.LENGTH_SHORT).show();
                } else {
                    // 将EditText的内容写入当前文件
                    EditText text = findViewById(R.id.editText1);
                    WriteFile tempWrite = new WriteFile();
                    String file_name = current_temp[0];// TODO 当前文件
                    int result = tempWrite.writeFile(text.getText().toString(), file_name);
                    if (result == 0) {// 保存成功
                        Toast.makeText(MainActivity.this, file_name + " save succeed", Toast.LENGTH_LONG).show();
                    } else {// 保存失败
                        Toast.makeText(MainActivity.this, file_name + " save failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button closeBtn = findViewById(R.id.closeButton);// `关闭`按钮
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// TODO

                View saveFile = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_save, null);

                AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MainActivity.this);
                saveBuilder.setTitle("Input file name:");
                saveBuilder.setView(saveFile);
                saveBuilder.show();

                if (current_file[0] != null) {// TODO 打开的临时文件对应真实文件的备份,需要保存
                }
            }
        });
    }
}
