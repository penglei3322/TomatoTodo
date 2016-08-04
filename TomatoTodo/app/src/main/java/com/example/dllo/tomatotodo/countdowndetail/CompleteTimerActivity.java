package com.example.dllo.tomatotodo.countdowndetail;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.db.HistoryAllBean;
import com.example.dllo.tomatotodo.service.TomatoService;

/**
 * Created by dllo on 16/7/21.
 */
public class CompleteTimerActivity extends BaseActivity implements View.OnClickListener {

    private ImageView completeBtn;
    private EditText editText;

    private ServiceConnection serviceConnection;
    private TomatoService.MyBinder myBinder;

    @Override
    public int initView() {
        return R.layout.activity_complete_timer;
    }

    @Override
    public void initData() {
        completeBtn = (ImageView) findViewById(R.id.complete_aty_accept_iv);
        editText = (EditText) findViewById(R.id.complete_aty_msg_et);

        completeBtn.setOnClickListener(this);

        Intent serviceIntent = new Intent(this, TomatoService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (TomatoService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    // 点击完成
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complete_aty_accept_iv:
                myBinder.startRestCountDown();
                DBTools.getInstance(this).insertSingle(new HistoryAllBean(myBinder.getStartWorkTime(),
                        System.currentTimeMillis(),editText.getText().toString()));

                finish();
                break;
        }
    }
}
