package com.example.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.helloworld.functions.GetPath;
import com.example.helloworld.functions.MyDialog;
import com.example.helloworld.functions.NewFile;
import com.example.helloworld.functions.ReadFile;
import com.example.helloworld.functions.WriteFile;

import java.io.File;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    String[] current_temp = new String[5];// 所有打开文件对应的临时文件
    int file_total_num = 0;// TODO 当前打开的文件总数
    int file_cur_num = -1;// TODO 当前窗口的文件编号,-1为空
    int buttonMove = 280;// 所有button一起移动的水平参数
    int button_id = 1234321;// button的起始id

    public MyDialog dialog;
    public int dialog_result;

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
            String file = tempPath.getPathFromUri(MainActivity.this, uri);// 将uri转成路径

            // TODO 打开文件
            if (checkTemp(file)) {// 不能打开临时文件
                Toast.makeText(this, "can't load tempFile " + file, Toast.LENGTH_SHORT).show();
                return;
            }
            File temp = new File(file);
            if (temp.exists()) {// 如果文件存在,才打开文件
                openNewFile(file);// 将该文件与临时文件绑定并打开
            }// TODO
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
            if (requestCode == 1) {// `打开`按钮
                // 转换文件路径为绝对路径
                Uri uri = data.getData();
                GetPath tempPath = new GetPath();
                String file = tempPath.getPathFromUri(this, uri);// 将uri转成路径

                // TODO 打开文件
                if (checkTemp(file)) {// 不能打开临时文件
                    Toast.makeText(this, "can't load tempFile " + file, Toast.LENGTH_SHORT).show();
                    return;
                }
                File temp = new File(file);
                if (temp.exists()) {// 如果文件存在,才打开文件
                    openNewFile(file);// 将该文件与临时文件绑定并打开
                }// TODO
            } else {// TODO
                ;
            }
        }
    }

    @Override
    protected void onPause() {// TODO 用于临时保存数据
        super.onPause();
        tempSave();
    }

    public boolean checkTemp(String file_name) {// 检查是否是临时文件
        if (file_name == null) {
            return false;
        }

        // 先检查是否是app目录
        File file = new File(file_name);
        String dir_path = file.getParent();// TODO 不包含"/."
        Log.i("fuck " + dir_path, MainActivity.this.getExternalFilesDir(".").getAbsolutePath());// TODO
        Log.i(file.getName(), Pattern.matches("^temp\\d{1,2}$", file.getName()) + "");// TODO
        if (!Pattern.matches(dir_path + "(/.)*(/)*", MainActivity.this.getExternalFilesDir(".").getAbsolutePath())) {// 如果不是app目录
            return false;
        }

        // 再检查是否是临时文件名
        if (!Pattern.matches("^temp\\d{1,2}$", file.getName())) {// 不是临时文件名
            return false;
        }
        return true;
    }

    private void createBtn() {// 为所有按钮绑定点击事件
        Button openBtn = findViewById(R.id.openButton);// `打开`按钮
        openBtn.setOnClickListener(new View.OnClickListener() {// 点击`打开`按钮
            @Override
            public void onClick(View view) {
                // 调用系统文件管理,获取文件名
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");// 所有文件
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        Button btnCtrl = findViewById(R.id.ctrlButton);// TODO 控制`隐藏/显示`按钮
        btnCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // 隐藏/显示所有按钮
                ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.buttons), "translationX", buttonMove + Math.abs(buttonMove));
                animator.setDuration(250);
                animator.start();
                buttonMove = -buttonMove;
                // 修改`隐藏/显示`按钮样式
                Button temp = findViewById(R.id.ctrlButton);
                if (buttonMove == -Math.abs(buttonMove)) {// 此时为`显示`按钮
                    temp.setBackgroundResource(R.drawable.button_show);
                } else {// `隐藏`按钮
                    temp.setBackgroundResource(R.drawable.button_hide);
                }
            }
        });

        final Button btnNew = findViewById(R.id.newButton);// `新建`按钮
        btnNew.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                newTempFile();
            }
        });

        Button btnSave = findViewById(R.id.saveButton);// `保存`按钮
        btnSave.setOnClickListener(new View.OnClickListener() {
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
                closeCurFile();// 关闭当前窗口的文件
            }
        });
    }

    protected void closeCurFile() {
        // 获取文件名和临时文件名
        SharedPreferences preferencesFile = getSharedPreferences("temp_tab", MODE_PRIVATE);
        String tempFile = preferencesFile.getString(file_cur_num + "", null);// 获取当前窗口的临时文件位置
        SharedPreferences preferences = getSharedPreferences("temp_file", MODE_PRIVATE);
        String file = preferencesFile.getString(tempFile, null);
        if (file == null || tempFile == null) {// TODO
            Log.i("fuck close", "failed");
            // return;// TODO
        }

        // 显示`是否保存`提示框
        dialog = new MyDialog(MainActivity.this, R.style.save_style);// 新建dialog


        // TODO TODO TODO
        dialog.show();// TODO 获取点击结果

        Log.i("fuck", "return " + dialog_result);
//
//        // 删除标签栏
//        Log.i("fuck before", file_cur_num + ", total: " + file_total_num);
//        if (file_cur_num >= 0 && false) {// 当前是否打开了文件
//            // 关闭当前页面
//            Button btn = findViewById(button_id + file_cur_num);// TODO 获取当前要关闭的页面tab
//            LinearLayout tab = findViewById(R.id.file_tab);
//            tab.removeView(btn);
//
//            // 切换至邻近页面
//            // TODO 修改数据库
//            if (file_cur_num == file_total_num - 1) {// 关闭的是最后一个文件
//                // 打开前一个文件
//                if (file_total_num > 1) {// 还能够打开另一个文件
//                    Button btnNow = findViewById(button_id + file_cur_num - 1);
//                    btnNow.callOnClick();
//                } else {
//                    file_cur_num --;// TODO
//                }
//            } else {// 将文件从后往前移动,补空位
//                Button btnNow = null;
//                for (int i = file_cur_num + 1; i < file_total_num ; i ++) {
//                    btnNow = findViewById(button_id + i);
//                    btnNow.setId(button_id + i - 1);// 将id - 1
//                    Log.i("fucking", "i: " + i + " find: " + (findViewById(button_id + i - 1) != null));
//                }
//                btnNow = findViewById(button_id + file_cur_num);// 切换当前文件
//                btnNow.callOnClick();
//            }
//            file_total_num --;// TODO 总文件数减少
//
//        } else {
//            Log.i("fuck cur_total", file_cur_num + "===" + file_total_num);
//        }
//        Log.i("fuck after", file_cur_num + ", total: " + file_total_num);
    }

    protected void tempSave() {// 将输入框内容保存到临时文件中
        // 从当前窗口获取临时文件的路径
        SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);
        String tempFile = preferences.getString(file_cur_num + "", null);// 文件不存在则返回null
        if (tempFile == null) {// TODO 没有打开文件
            return;
        }
        Log.i("fuck cur", tempFile + " temp saved");// TODO

        // 将EditText的内容写入临时文件
        File file = new File(tempFile);
        EditText text = findViewById(R.id.editText1);// 获取输入框
        WriteFile tempWrite = new WriteFile();
        int result = tempWrite.writeFile(text.getText().toString(), tempFile);// 写入临时文件
        if (result == 0) {// TODO 保存成功
            Toast.makeText(MainActivity.this, file.getName() + " temp save", Toast.LENGTH_LONG).show();
        } else {// 保存失败
            Toast.makeText(MainActivity.this, file.getName() + " temp save error", Toast.LENGTH_LONG).show();
        }
    }

    protected void openNewFile(String file) {// 打开非临时文件,并创建临时文件副本
        // 将打开的文件与临时文件绑定,已经获取打开的文件的绝对路径
        String tempFile = newTempFile();// 新建临时文件并打开,获取临时文件名
        SharedPreferences preferences = getSharedPreferences("temp_file", MODE_PRIVATE);// 只能被自己的应用程序访问,打开临时文件映射
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(tempFile, file);// 将临时文件与打开的文件绑定
        editor.commit();

        // 将文件内容读取到输入框
        ReadFile tempRead = new ReadFile();
        EditText text = findViewById(R.id.editText1);
        tempRead.readFile(file, text);// 如果文件不存在則会返回-1,将打开的文件的内容读入输入框

        // TODO 保存到临时文件
        tempSave();
    }

    protected String newTempFile() {// 新建临时文件并显示到最后一个窗口
        // 新建临时文件
        NewFile tempNew = new NewFile();
        int temp_num = tempNew.newFile("", MainActivity.this);// TODO 获取临时文件编号

        // 将临时文件绑定到窗口号
        String tempName = getExternalFilesDir(".").getAbsolutePath() + "/temp" + temp_num;// TODO 新临时文件的文件名
        SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);// 只能被自己的应用程序访问,打开窗口映射
        SharedPreferences.Editor editor = preferences.edit();// 用于编辑存储数据
        editor.putString(file_total_num + "", tempName);// TODO 将临时文件绑定到最大窗口号
        editor.commit();// 保存
        Log.i("fuck new temp", tempName + " " + file_total_num);// TODO
        Log.i("fuck data", preferences.getString("" + file_total_num, "nothing fuck"));// 第二个参数:若找不到key,则返回第二个参数

        // 在顶部栏创建tab
        LinearLayout tab = findViewById(R.id.file_tab);
        Button btnNow = new Button(MainActivity.this);
        btnNow.setBackgroundResource(R.drawable.tab_active);
        btnNow.setLayoutParams(new LinearLayout.LayoutParams(220, LinearLayout.LayoutParams.MATCH_PARENT));// 设定大小
        btnNow.setText("temp" + temp_num);// TODO 为tab设定文件名
        btnNow.setPadding(0, 0, 0, 0);

        // TODO 将其他button置为不活跃
        if (file_cur_num >= 0) {
            Button btnLast = findViewById(button_id + file_cur_num);// TODO 找出当前文件对应的tab
            btnLast.setBackgroundResource(R.drawable.tab_notactive);// TODO
        }

        // TODO 为button标号
        btnNow.setId(button_id + file_total_num);// TODO
        file_cur_num = file_total_num;// 切换当前窗口为`新建文件`的对应窗口
        file_total_num ++;// 文件数增加

        // TODO 为button添加切换监听
        btnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("fuck view", view.getId() - button_id + "");
                changeTab(view.getId());// 传递被点击按钮的id
            }
        });
        tab.addView(btnNow);// 添加按钮

        // TODO 返回临时文件的绝对地址
        return tempName;
    }

    public void changeTab(int click_id) {// TODO 切换窗口
        Button btnLast = findViewById(button_id + file_cur_num);// 找出当前文件对应tab
        if (btnLast != null) {// TODO 关闭事件等
            btnLast.setBackgroundResource(R.drawable.tab_notactive);// 置为不活跃
        }

        Button btnNow = findViewById(click_id);// 找出被点击的tab
        btnNow.setBackgroundResource(R.drawable.tab_active);// 置为活跃
        file_cur_num = click_id - button_id;// TODO 切换当前文件
    }
}
