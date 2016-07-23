package com.example.dllo.tomatotodo.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.ShieldingApplicationsActivity;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView returnIv;
    private int ids[] = {R.id.preferences_account, R.id.preferences_expiry_data, R.id.preferences_wifi_switch,
                         R.id.preferences_login, R.id.preferences_effect, R.id.preferences_work_effect,
                         R.id.preferences_how_long_time, R.id.preferences_goal, R.id.preferences_other,
                         R.id.preferences_apply_screen, R.id.preferences_setting, R.id.preferences_help};
    private LinearLayout accountLayout, loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        returnIv = (ImageView) findViewById(R.id.preferences_return_iv);
        accountLayout = (LinearLayout) findViewById(R.id.preferences_account_layout);
        loginLayout = (LinearLayout) findViewById(R.id.preferences_login_layout);

        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
        }
        returnIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.preferences_return_iv:
                finish();
                break;

            case R.id.preferences_apply_screen:
                startActivity(new Intent(this, ShieldingApplicationsActivity.class));
                break;
        }
    }
}
