package com.example.dllo.tomatotodo.preferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.base.MyApp;
import com.example.dllo.tomatotodo.preferences.lengthoftime.LengthOfTimeActivity;
import com.example.dllo.tomatotodo.preferences.settings.SettingsActivity;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.ShieldingApplicationsActivity;

import cn.bmob.v3.BmobUser;

public class PreferencesActivity extends BaseActivity implements View.OnClickListener {

    private ImageView returnIv;
    private int ids[] = {R.id.preferences_account, R.id.preferences_expiry_data, R.id.preferences_wifi_switch,
                         R.id.preferences_login, R.id.preferences_effect, R.id.preferences_work_effect,
                         R.id.preferences_how_long_time, R.id.preferences_goal, R.id.preferences_other,
                         R.id.preferences_apply_screen, R.id.preferences_setting, R.id.preferences_help};
    private LinearLayout accountLayout, loginLayout;
    private Switch wifiSwitch;
    private TextView  accountTv;
    private FreshenBroadcastReceiver freshenBroadcastReceiver;

    @Override
    public int initView() {
        return R.layout.activity_preferences;
    }

    @Override
    public void initData() {

        returnIv = (ImageView) findViewById(R.id.bar_return_iv);
        accountLayout = (LinearLayout) findViewById(R.id.preferences_account_layout);
        loginLayout = (LinearLayout) findViewById(R.id.preferences_login_layout);
        wifiSwitch = (Switch) findViewById(R.id.preferences_wifi_switch);
        accountTv = (TextView) findViewById(R.id.preferences_account);

        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
        }
        returnIv.setOnClickListener(this);

        // WIFI
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToggleWiFi(true);
                    Toast.makeText(getApplicationContext(), "WiFi已开启", Toast.LENGTH_LONG).show();
                } else {
                    ToggleWiFi(false);
                    Toast.makeText(getApplicationContext(), "WiFi已关闭", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 通过是否登录, 显示不同布局
        BmobUser user = BmobUser.getCurrentUser(MyApp.context);
        if (user == null) {
            accountLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }else {
            loginLayout.setVisibility(View.GONE);
            accountLayout.setVisibility(View.VISIBLE);
            String name = user.getUsername();
            accountTv.setText(name);
        }

        freshenBroadcastReceiver = new FreshenBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Refresh");
        this.registerReceiver(freshenBroadcastReceiver, intentFilter);
    }

    private void ToggleWiFi(boolean status) {

        WifiManager wifiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);

        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            // finish页面
            case R.id.bar_return_iv:
                finish();
                break;
            // 屏蔽应用
            case R.id.preferences_apply_screen:
                startActivity(new Intent(this, ShieldingApplicationsActivity.class));
                break;
            // 番茄时长
            case R.id.preferences_how_long_time:
                startActivity(new Intent(this, LengthOfTimeActivity.class));
                break;
            // 账号及退出登录
            case R.id.preferences_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            // 登录
            case R.id.preferences_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.preferences_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    // 接收广播, 刷新数据
    class FreshenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BmobUser bmobUser = BmobUser.getCurrentUser(MyApp.context);
            if (bmobUser == null) {
                accountLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }else {
                loginLayout.setVisibility(View.GONE);
                accountLayout.setVisibility(View.VISIBLE);
                String name = bmobUser.getUsername();
                accountTv.setText(name);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(freshenBroadcastReceiver);
    }
}
