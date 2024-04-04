package com.njupt.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.njupt.bean.UserInfo;
import com.njupt.bean.toparsejson.LoginOrRegistResult;
import com.njupt.utils.HttpManager;
import com.njupt.utils.LoginManager;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    Button btn_register,btn_login;
    EditText username,userPwd;

    UserInfo userInfo;
    HttpManager httpManager = new HttpManager();
    InnerHandler handler = new InnerHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInfo = new UserInfo();

        btn_register = findViewById(R.id.login_btn_regist);
        btn_login = findViewById(R.id.login_btn_login);
        username = findViewById(R.id.login_username);
        userPwd = findViewById(R.id.login_user_pwd);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("") && !userPwd.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "正在登录", Toast.LENGTH_SHORT).show();
                    //开启网络线程
                    try {
                        httpManager.login(username.getText().toString(),userPwd.getText().toString(),handler);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("") && !userPwd.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "正在注册", Toast.LENGTH_SHORT).show();
                    //开启网络线程
                    try {
                        httpManager.register(username.getText().toString(),userPwd.getText().toString(),handler);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class InnerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) { //登录成功
                //网络将数据放入message
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                userInfo = ((LoginOrRegistResult.InnerClass)msg.obj).loginUser;
                LoginManager.getInstance().setLoggedIn(true);
                LoginManager.getInstance().setUidAndUsername(userInfo.uid,userInfo.username);
                //获取数据，改变登录状态
                //1秒后退出
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish(); // 关闭当前 Activity
                    }
                }, 1000);
            } else if (msg.what == 201) {//用户名错误
                Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 202){//密码错误
                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 300) {//注册成功
                Toast.makeText(LoginActivity.this, "注册成功，请直接登录", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 301) {
                Toast.makeText(LoginActivity.this, "用户名已被使用", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(LoginActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
