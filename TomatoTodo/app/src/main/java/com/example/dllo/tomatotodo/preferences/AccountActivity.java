package com.example.dllo.tomatotodo.preferences;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView titleText, quitTv;
    private ImageView returnTv;

    @Override
    public int initView() {
        return R.layout.activity_account;
    }

    @Override
    public void initData() {

        titleText = (TextView) findViewById(R.id.bar_text);
        returnTv = (ImageView) findViewById(R.id.bar_return_iv);
        quitTv = (TextView) findViewById(R.id.account_quit);

        titleText.setText("账号");
        returnTv.setOnClickListener(this);
        quitTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bar_return_iv:
                finish();
                break;

            case R.id.account_quit:
                ShowAlertDialog();
                break;
        }
    }

    private void ShowAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定要登出么");
        builder.setMessage("登出时将会清空本地记录");
        builder.setPositiveButton("退出当前账号", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
