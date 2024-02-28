package com.example.yicard;

// 导入相关的类
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yicard.db.DbHelper;

// 定义LoginActivity类，继承自AppCompatActivity类
public class LoginActivity extends AppCompatActivity {

    // 定义输入框和按钮控件的变量
    private EditText etPassword;
    private EditText etPhone;
    private Button btnLogin;
    private Button btnRegister;
    private TextView lblResult;

    // 定义DbHelper的变量
    private DbHelper dbHelper;

    // 重写onCreate()方法，用于初始化布局和控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.activity_login);

        // 获取输入框和按钮控件，并为它们创建相应的变量
        etPassword = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_name);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        //lblResult = findViewById(R.id.msglbl);

        // 创建DbHelper的实例
        dbHelper = new DbHelper(this);

        // 为登录按钮添加点击事件监听器，用于实现登录的逻辑
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入框中的用户名和密码
                String name = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // 判断用户名和密码是否为空
                if (name.isEmpty() || password.isEmpty()) {
                    // 如果为空，提示用户输入
                    Toast.makeText(LoginActivity.this, "Please enter name and password", Toast.LENGTH_SHORT).show();
                } else {
                    // 如果不为空，使用DbHelper的checkUser()方法，来验证用户的登录信息
                    boolean result = dbHelper.checkUser(name, password);
                    // 判断验证是否成功
                    if (result) {
                        // 如果成功，跳转到主界面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", name);
                        // 调用finish()方法，结束当前的Activity
                        finish();
                        startActivity(intent);

                    }  // 如果失败，提示用户登录失败
                    else{
                        //lblResult.setText("Login failed");
                        Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 为注册按钮添加点击事件监听器，用于跳转到注册界面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // 调用finish()方法，结束当前的Activity
                finish();
                startActivity(intent);
            }
        });
    }
}
