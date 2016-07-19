package com.example.dllo.tomatotodo.statistics;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dllo on 16/7/18.
 */
public class StatisticsFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvLeft, tvCenter, tvRight, tvAll, tvAverage, tvGrowth;
    private HashMap<Integer, String> map;
    private String date;//当时的日期,用作点击事件判断
    private int dates;//map里日期的key
    private int temp = 0;//记录点击的次数

    @Override
    public int createView() {
        return R.layout.fragmet_statistics;
    }

    @Override
    public void initView(View view) {
        tvLeft = (TextView) view.findViewById(R.id.tv_left_fragment_statics);
        tvCenter = (TextView) view.findViewById(R.id.tv_center_fragment_statics);
        tvRight = (TextView) view.findViewById(R.id.tv_right_fragment_statics);
        tvAll = (TextView) view.findViewById(R.id.tv_tomato_all);
        tvAverage = (TextView) view.findViewById(R.id.tv_daily_average);
        tvGrowth = (TextView) view.findViewById(R.id.tv_month_growth);
        tvLeft.setOnClickListener(this);
        tvCenter.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //添加HashMap
        tvCenter.setText("过去三十天");
        map = new HashMap<>();
        map.put(1, "一月");
        map.put(2, "二月");
        map.put(3, "三月");
        map.put(4, "四月");
        map.put(5, "五月");
        map.put(6, "六月");
        map.put(7, "七月");
        map.put(8, "八月");
        map.put(9, "九月");
        map.put(10, "十月");
        map.put(11, "十一月");
        map.put(12, "十二月");
        //根据当时时间的月份从hashMap中获取三个TextView的显示内容
        SimpleDateFormat format = new SimpleDateFormat("MM");
        date = format.format(new Date());
        Log.d("date", date);
        //将String类型的date 转化成为int类型
        dates = Integer.valueOf(date);
        Log.d("dates", dates + "```");
        //根据转换的int类型key值拿到想要的月份
        tvLeft.setText(map.get(dates));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_fragment_statics:
                dates--;
                tvRight.setVisibility(View.VISIBLE);
                Log.d("dateLeft",dates + "-");
                //当点击到一月份时候,让TextView可以重新循环整个map
                if (dates == 0) {
                    dates = 12;
                }
                tvRight.setText(tvCenter.getText().toString());
                //当点击左侧按钮时,把左侧按钮中的值赋给中间按钮
                tvCenter.setText(tvLeft.getText().toString());
                tvLeft.setText(map.get(dates));
                //每当点击一次左侧按钮,记录一次
                temp++;
                Log.d("tempLeft",temp + "-");
                break;

            case R.id.tv_center_fragment_statics:

                break;

            case R.id.tv_right_fragment_statics:
                Log.d("tempRight",temp + "-");
                //首先判断点击的temp次数,如果为0,则拦截点击事件
                if (temp == 0) {
                    tvRight.setVisibility(View.INVISIBLE);
                    return;
                }
                dates++;
                if (temp == 1){
                    tvLeft.setText(tvCenter.getText().toString());
                    tvCenter.setText(tvRight.getText().toString());
                    tvRight.setText("过去三十天");


                }else if (temp == 2){



                } else {
                    Log.d("dates", dates + "月");
                    tvLeft.setText(tvCenter.getText().toString());
                    tvCenter.setText(tvRight.getText().toString());
                    tvRight.setText(map.get(dates));

                }
                temp--;
                break;


        }
    }
}
