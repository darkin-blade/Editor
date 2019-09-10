package com.example.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
    int file_cur_num = -1;// TODO 当前窗口的文件编号,-1为空(初始值)
    int buttonMove = 280;// 所有button一起移动的水平参数
    int button_id = 1234321;// button的起始id

    public MyDialog dialog;

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

            // TODO 打开文件
            if (checkTemp(path)) {// 不能打开临时文件
                Toast.makeText(this, "can't load tempFile " + path, Toast.LENGTH_SHORT).show();
                return;
            }
            File temp = new File(path);
            if (temp.exists()) {// 如果文件存在,才打开文件
                openNewFile(path);// 将该文件与临时文件绑定并打开
            }// TODO
        }

        // 首次检察权限
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int check_result = ActivityCompat.checkSelfPermission(this, permission);// `允许`返回0,`拒绝`返回-1
        if (check_result != PackageManager.PERMISSION_GRANTED) {// 没有`写`权限
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);// 获取`写`权限
        }

        // 为所有按钮绑定点击事件
        createBtn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// 回调方法
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {// `打开`按钮
                // 转换文件路径为绝对路径
                Uri uri = data.getData();
                GetPath tempPath = new GetPath();
                String path = tempPath.getPathFromUri(this, uri);// 将uri转成路径

                // TODO 打开文件
                if (checkTemp(path)) {// 不能打开临时文件
                    Toast.makeText(this, "can't load tempFile " + path, Toast.LENGTH_SHORT).show();
                    return;
                }
                File temp = new File(path);
                if (temp.exists()) {// 如果文件存在,才打开文件
                    openNewFile(path);// 将该文件与临时文件绑定并打开
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

    public boolean checkTemp(String path) {// 检查是否是临时文件
        if (path == null) {
            return false;
        }

        // 先检查是否是app目录
        File file = new File(path);
        String dir_path = file.getParent();// TODO 不包含"/."
        Log.i("fuck checktemp" + dir_path, MainActivity.this.getExternalFilesDir(".").getAbsolutePath());// TODO
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
        // `打开`按钮
        Button openBtn = findViewById(R.id.openButton);
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

        // TODO 控制`隐藏/显示`按钮
        Button btnCtrl = findViewById(R.id.ctrlButton);
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

        // `新建`按钮
        final Button btnNew = findViewById(R.id.newButton);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                createFile();
                Button tempBtn = findViewById(button_id + file_total_num);// TODO
                tempBtn.callOnClick();
                file_cur_num = file_total_num;// TODO 切换当前窗口为`新建文件`的对应窗口
                file_total_num ++;// TODO 文件数增加
            }
        });

        // `保存`按钮
        Button btnSave = findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencesFile = getSharedPreferences("temp_tab", MODE_PRIVATE);
                String tempPath = preferencesFile.getString(file_cur_num + "", null);// 获取当前窗口的临时文件位置
                saveFile(tempPath);// TODO
            }
        });

        // `关闭`按钮
        Button closeBtn = findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// TODO
                closeCurFile();// 关闭当前窗口的文件
            }
        });
    }

    protected int saveFile(String tempPath) {// 保存当前窗口文件
        // 获取临时文件名和文件名
        SharedPreferences preferencesFile = getSharedPreferences("temp_file", MODE_PRIVATE);
        String path = preferencesFile.getString(tempPath, null);

        if (path == null) {// 新建的临时文件
            ;// TODO 调用文件管理器
        }

        if (path == null) {// 调用文件管理器之后没有进行保存(取消了保存操作)
            Toast.makeText(MainActivity.this, "save canceled", Toast.LENGTH_LONG).show();// TODO
            return -1;// 取消操作
        }

        // 将EditText的内容写入文件
        EditText text = findViewById(R.id.editText1);
        WriteFile tempWrite = new WriteFile();
        int result = tempWrite.writeFile(text.getText().toString(), path);// TODO 写入文件
        if (result == 0) {// 保存成功
            Toast.makeText(MainActivity.this, path + " save succeed", Toast.LENGTH_LONG).show();
        } else {// 保存失败
            Toast.makeText(MainActivity.this, path + " save failed", Toast.LENGTH_LONG).show();
        }
        return result;// TODO 分为:保存成功,保存失败,取消保存
    }

    protected void removeFile(String tempPath) {
        // 删除文件
        File tempFile = new File(tempPath);
        tempFile.delete();// TODO 如果文件不存在
        if (tempFile.exists()) {// TODO 判断是否删除成功
            Toast.makeText(MainActivity.this, tempPath + " deleted", Toast.LENGTH_LONG).show();
        }

        // 解除链接
        SharedPreferences preferencesFile = getSharedPreferences("temp_file", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesFile.edit();
        editor.putString(tempPath, null);// TODO 解除临时文件的链接
        editor.commit();
    }

    protected void closeTab() {// TODO 必须打开至少一个文件
        Log.i("fuck dialog result", dialog.result + "");
        if (dialog.result == 0) {// `取消`
            return;
        } else {
            SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);
            String tempPath = preferences.getString(file_cur_num + "", null);// 获取当前窗口的临时文件位置
            if (dialog.result == 1) {// `保存`
                if (saveFile(tempPath) == 0) {// TODO 保存当前窗口文件成功,删除临时文件
                    removeFile(tempPath);// 删除临时文件
                } else {// TODO 保存失败
                    return;
                }
            } else if (dialog.result == -1) {// 不保存文件
                removeFile(tempPath);
            }
        }

        // 只有在点击删除或确定保存的情况下进行此步
        Log.i("fuck before", file_cur_num + ", total: " + file_total_num);

        // TODO 关闭当前页面标签
        Button btnLast = findViewById(button_id + file_cur_num);// 获取当前要关闭的页面tab
        LinearLayout tab = findViewById(R.id.file_tab);
        tab.removeView(btnLast);

        // 分情况转移标签, TODO TODO 修改标签名
        if (file_cur_num + 2 <= file_total_num) {// 不是最后一个文件
            Button btnNow = null;
            String tempPath = null;// 保存临时文件绝对路径
            SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            // 从当前窗口往后修改(向前平移)
            for (int i = file_cur_num + 1; i < file_total_num ; i ++) {
                // 修改button编号
                btnNow = findViewById(button_id + i);
                btnNow.setId(button_id + i - 1);// 将id - 1

                // 修改tab绑定的临时文件
                tempPath = preferences.getString(i + "", null);
                if (tempPath == null) {
                    new AssertionError("invalid tab and tempPath");// TODO 得到null
                }
                editor.putString(i - 1 + "", tempPath);// 将临时文件名绑定到前一个标签栏
            }

            // TODO 解除最后一个标签页的绑定
            editor.putString(file_total_num - 1 + "", null);
            editor.commit();

            // 切换至临近窗口,不能将文本框内容保存到file_cur_num对应文件中
            btnNow = findViewById(button_id + file_cur_num);// 切换当前文件
            int temp = file_cur_num;
            file_cur_num = -999999;
            btnNow.callOnClick();
            file_cur_num = temp;
        } else if (file_cur_num >= 1) {// 当前打开不止一个文件,且是最后一个标签页
            // 解除窗口和最后一个文件的链接
            SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(file_cur_num + "", null);// 最后一个窗口不再绑定临时文件
            editor.commit();

            // TODO 打开前一个文件,此时最后一个标签页已被删除
            Button btnNow = findViewById(button_id + file_cur_num - 1);// 前一个文件的标签栏
            btnNow.callOnClick();
            file_cur_num --;// TODO
        } else {// TODO 只打开了一个文件
            // 关闭所有文件
            file_cur_num --;
            if (file_cur_num != -1) {
                new AssertionError("all file closed: " + file_total_num);
            }

            // 清空输入框内容
            EditText text = findViewById(R.id.editText1);
            text.setText("");
        }

        file_total_num --;// 总文件数减少
        Log.i("fuck after", file_cur_num + ", total: " + file_total_num);
    }

    protected void closeCurFile() {
        // 获取文件名和临时文件名
        SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);
        String tempPath = preferences.getString(file_cur_num + "", null);// 获取当前窗口的临时文件位置
        if (tempPath == null) {// TODO 没有打开任何文件
            if (file_total_num != 0) {
                new AssertionError("file total: " + file_total_num);
            }
            return;// TODO
        }

        // 显示`是否保存`提示框
        dialog = new MyDialog(MainActivity.this, R.style.save_style);// 新建dialog
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeTab();
            }
        });
        dialog.show();// TODO 获取点击结果
    }

    protected void openNewFile(String path) {// 打开非临时文件,并创建临时文件副本
        // 将打开的文件与临时文件绑定,已经获取打开的文件的绝对路径
        String tempPath = createFile();// TODO 新建临时文件并打开,获取临时文件名
        SharedPreferences preferencesFile = getSharedPreferences("temp_file", MODE_PRIVATE);// 只能被自己的应用程序访问,打开临时文件映射
        SharedPreferences.Editor editor = preferencesFile.edit();
        editor.putString(tempPath, path);// 将临时文件与打开的文件绑定
        editor.commit();

        // 将文件内容读取到输入框
        file_cur_num = file_total_num;// TODO 切换当前窗口为`新建文件`的对应窗口
        file_total_num ++;// TODO 文件数增加
        ReadFile tempRead = new ReadFile();
        EditText text = findViewById(R.id.editText1);
        tempRead.readFile(path, text);// 如果文件不存在則会返回-1,将打开的文件的内容读入输入框
    }

    protected String createFile() {// 新建临时文件并显示到最后一个窗口, TODO TODO 文件名的显示
        // 新建临时文件
        NewFile tempNew = new NewFile();
        int temp_num = tempNew.newFile("", MainActivity.this);// TODO 获取临时文件编号

        // 将临时文件绑定到窗口号
        String tempName = getExternalFilesDir(".").getAbsolutePath() + "/temp" + temp_num;// TODO 新临时文件的文件名
        SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);// 只能被自己的应用程序访问,打开窗口映射
        SharedPreferences.Editor editor = preferences.edit();// 用于编辑存储数据
        editor.putString(file_total_num + "", tempName);// TODO 将临时文件绑定到最大窗口号
        editor.commit();// 保存

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

        // TODO 为button添加切换监听
        btnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(view.getId());// 传递被点击按钮的id
            }
        });
        tab.addView(btnNow);// 添加按钮

        // TODO 返回临时文件的绝对地址
        return tempName;
    }

    protected void tempSave() {// 将输入框内容保存到临时文件中
        // 从当前窗口获取临时文件的路径
        SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);
        String tempPath = preferences.getString(file_cur_num + "", null);// 文件不存在则返回null
        if (tempPath == null) {// TODO 没有打开文件
            return;
        }

        // 将EditText的内容写入临时文件
        File tempFile = new File(tempPath);
        EditText text = findViewById(R.id.editText1);// 获取输入框
        WriteFile tempWrite = new WriteFile();
        int result = tempWrite.writeFile(text.getText().toString(), tempPath);// 写入临时文件
        if (result == 0) {// TODO 保存成功
            Toast.makeText(MainActivity.this, tempFile.getName() + " temp save", Toast.LENGTH_SHORT).show();
        } else {// 保存失败
            Toast.makeText(MainActivity.this, tempFile.getName() + " temp save error", Toast.LENGTH_SHORT).show();
        }

        Log.i("fuck temp save", text.getText().toString() + " " + tempPath);// TODO
    }

    public void changeTab(int tabNew) {// TODO 切换窗口

        // TODO 将旧的窗口置为不活跃,如果不需要此步,将file_cur_num置为负数
        Button btnLast = findViewById(button_id + file_cur_num);// 找出当前文件对应tab
        if (btnLast != null) {// TODO 由关闭事件调用的此函数,至少还留有一个打开的文件窗口
            tempSave();// TODO 保存到临时文件
            btnLast.setBackgroundResource(R.drawable.tab_notactive);// 置为不活跃
        }

        // 将新的窗口置为活跃
        Button btnNow = findViewById(tabNew);// 找出被点击的tab
        btnNow.setBackgroundResource(R.drawable.tab_active);// 置为活跃
        file_cur_num = tabNew - button_id;// TODO 切换当前文件

        // 获取新窗口文件路径
        SharedPreferences preferences = getSharedPreferences("temp_tab", MODE_PRIVATE);// 获取窗口对应临时文件的路径
        String tempPath = preferences.getString(file_cur_num + "", null);

        // 读取新窗口对应文件的数据
        EditText text = findViewById(R.id.editText1);
        ReadFile tempRead = new ReadFile();
        tempRead.readFile(tempPath, text);
    }
}
