package com.example.dllo.tomatotodo.preferences;

import android.content.Intent;
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
import com.example.dllo.tomatotodo.preferences.lengthoftime.LengthOfTimeActivity;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.ShieldingApplicationsActivity;

public class PreferencesActivity extends BaseActivity implements View.OnClickListener {

    private ImageView returnIv;
    private int ids[] = {R.id.preferences_account, R.id.preferences_expiry_data, R.id.preferences_wifi_switch,
                         R.id.preferences_login, R.id.preferences_effect, R.id.preferences_work_effect,
                         R.id.preferences_how_long_time, R.id.preferences_goal, R.id.preferences_other,
                         R.id.preferences_apply_screen, R.id.preferences_setting, R.id.preferences_help};
    private LinearLayout accountLayout, loginLayout;
    private Switch wifiSwitch;
    private TextView howLongTimeTv, accountTv;

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
        howLongTimeTv = (TextView) findViewById(R.id.preferences_how_long_time);
        accountTv = (TextView) findViewById(R.id.preferences_account);

        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
        }
        returnIv.setOnClickListener(this);
        howLongTimeTv.setOnClickListener(this);
        accountTv.setOnClickListener(this);

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

            case R.id.bar_return_iv:
                finish();
                break;

            case R.id.preferences_apply_screen:
                startActivity(new Intent(this, ShieldingApplicationsActivity.class));
                break;

            case R.id.preferences_how_long_time:
                startActivity(new Intent(this, LengthOfTimeActivity.class));
                break;

            case R.id.preferences_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
        }
    }
}
