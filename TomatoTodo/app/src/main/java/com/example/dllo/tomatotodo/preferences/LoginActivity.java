package com.example.dllo.tomatotodo.preferences;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.base.MyApp;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView titleText, forgetPswTv, termsOfServiceTv, noHaveAccountTv;
    private ImageView returnIv;
    private TabLayout tabLogin;
    private EditText nameEt, pswEt;
    private Button loginBtn, registerBtn;

    @Override
    public int initView() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {

        titleText = (TextView) findViewById(R.id.bar_text);
        returnIv = (ImageView) findViewById(R.id.bar_return_iv);
        tabLogin = (TabLayout) findViewById(R.id.tab_login);
        forgetPswTv = (TextView) findViewById(R.id.login_forget_psw_tv);
        termsOfServiceTv = (TextView) findViewById(R.id.login_terms_of_service_tv);
        noHaveAccountTv = (TextView) findViewById(R.id.login_no_have_account_tv);
        nameEt = (EditText) findViewById(R.id.login_name_et);
        pswEt = (EditText) findViewById(R.id.login_psw_et);
        loginBtn = (Button) findViewById(R.id.login_login_btn);
        registerBtn = (Button) findViewById(R.id.login_register_btn);

        titleText.setText("账号");
        pswEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tabLogin.addTab(tabLogin.newTab().setText("登录"));
        tabLogin.addTab(tabLogin.newTab().setText("注册"));
        tabLogin.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    registerBtn.setVisibility(View.GONE);
                    termsOfServiceTv.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                    forgetPswTv.setVisibility(View.VISIBLE);
                }
                if (tab.getPosition() == 1) {
                    loginBtn.setVisibility(View.GONE);
                    forgetPswTv.setVisibility(View.GONE);
                    registerBtn.setVisibility(View.VISIBLE);
                    termsOfServiceTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        returnIv.setOnClickListener(this);
        noHaveAccountTv.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        forgetPswTv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bar_return_iv:
            case R.id.login_no_have_account_tv:
                finish();
                break;

            case R.id.login_login_btn:
                login();
                break;

            case R.id.login_register_btn:
                register();
                break;

            case R.id.login_forget_psw_tv:
                ShowAlertDialog();
                break;
        }
    }

    private void ShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("忘记密码?");
        builder.setMessage("您可以点击确定来更改密码");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this, "忘记密码, 登陆你妈逼", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void register() {
        // 加入云端数据库
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(nameEt.getText().toString());
        bmobUser.setPassword(pswEt.getText().toString());
        bmobUser.signUp(MyApp.context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(nameEt.getText().toString());
        bmobUser.setPassword(pswEt.getText().toString());
        bmobUser.login(MyApp.context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                BmobUser user1 = BmobUser.getCurrentUser(MyApp.context);
                if (user1 != null) {
                    sendBroadcast(new Intent("Refresh"));
                    finish();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
