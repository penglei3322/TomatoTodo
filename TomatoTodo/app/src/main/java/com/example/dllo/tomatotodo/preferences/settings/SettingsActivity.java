package com.example.dllo.tomatotodo.preferences.settings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView returnIv;
    private TextView titleTv;

    @Override
    public int initView() {
        return R.layout.activity_settings;
    }

    @Override
    public void initData() {

        returnIv = (ImageView) findViewById(R.id.bar_return_iv);
        titleTv = (TextView) findViewById(R.id.bar_text);

        titleTv.setText("界面设置");

        returnIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bar_return_iv:
                finish();
                break;
        }
    }
}
