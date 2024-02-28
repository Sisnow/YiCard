package com.example.yicard;


import androidx.appcompat.app.AppCompatActivity;

// 导入相关的类
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yicard.db.DbHelper;

// 定义RegisterActivity类，继承自AppCompatActivity类
public class RegisterActivity extends AppCompatActivity {

    // 定义输入框和按钮控件的变量
    private EditText etUsername;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private Button btnLogin;
    private TextView lblResult;

    // 定义DbHelper的变量
    private DbHelper dbHelper;

    public RegisterActivity() {
    }

    // 重写onCreate()方法，用于初始化布局和控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.activity_register);

        // 获取输入框和按钮控件，并为它们创建相应的变量
        etUsername = findViewById(R.id.et_username);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        //lblResult = findViewById(R.id.msglbl);

        // 创建DbHelper的实例
        dbHelper = new DbHelper(this);

        // 为注册按钮添加点击事件监听器，用于实现注册的逻辑
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入框中的用户名、密码和确认密码
                String username = etUsername.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // 判断用户名、密码和确认密码是否为空
                if (username.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // 如果为空，提示用户输入
                    Toast.makeText(RegisterActivity.this, "info error", Toast.LENGTH_SHORT).show();
                } else {
                    //判断用户是否已存在
                    Cursor cursor = dbHelper.queryUserByName(username);
                    if (cursor.getCount() != 0){
                        Toast.makeText(RegisterActivity.this, "user already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 判断密码和确认密码是否匹配
                    if (password.equals(confirmPassword)) {
                        // 如果匹配，使用DbHelper的insertUser()方法，来注册新的用户信息
                        boolean result = dbHelper.insertUser(username, null, phone, password);
                        // 判断注册是否成功
                        if (result) {
                            // 如果成功，跳转到主界面
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            // 调用finish()方法，结束当前的Activity
                            finish();
                            startActivity(intent);
                        } else {
                            // 如果失败，提示用户注册失败
                            Toast.makeText(RegisterActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 如果不匹配，提示用户密码不一致
                        Toast.makeText(RegisterActivity.this, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 定义一个方法，用于跳转到登录界面
    public void goToLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        // 调用finish()方法，结束当前的Activity
        finish();
        startActivity(intent);
    }
}
