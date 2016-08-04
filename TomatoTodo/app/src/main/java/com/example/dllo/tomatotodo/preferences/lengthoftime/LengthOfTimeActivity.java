package com.example.dllo.tomatotodo.preferences.lengthoftime;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;

public class LengthOfTimeActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageView finishIv;
    private SeekBar timeSeekBar, timeRestSeekBar;
    private TextView timeTv, timeRestTv, barTv;

    @Override
    public int initView() {
        return R.layout.activity_length_of_time;
    }

    @Override
    public void initData() {

        finishIv = (ImageView) findViewById(R.id.bar_return_iv);
        timeSeekBar = (SeekBar) findViewById(R.id.length_of_time_seek_bar);
        timeRestSeekBar = (SeekBar) findViewById(R.id.length_of_time_rest_seek_bar);
        timeTv = (TextView) findViewById(R.id.length_of_time_text);
        timeRestTv = (TextView) findViewById(R.id.length_of_time_rest_text);
        barTv = (TextView) findViewById(R.id.bar_text);

        timeTv.setText("10min");
        timeRestTv.setText("1min");
        barTv.setText("番茄时长");

        finishIv.setOnClickListener(this);
        timeSeekBar.setOnSeekBarChangeListener(this);
        timeRestSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bar_return_iv:
                finish();
                break;
        }
    }

    // 数值改变（onProgressChanged）
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        SharedPreferences sharedPreferences = getSharedPreferences("titleTime",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (seekBar.getId()){

            case R.id.length_of_time_seek_bar:
                timeTv.setText((progress + 10) + "min");
                editor.putInt("workTime", progress + 10);
                editor.commit();
                break;

            case R.id.length_of_time_rest_seek_bar:
                timeRestTv.setText((progress + 1) + "min");
                editor.putInt("restTime", progress + 1);
                editor.commit();
                break;
        }
    }

    // 开始拖动（onStartTrackingTouch）
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    // 停止拖动（onStopTrackingTouch）
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
