package com.example.dllo.tomatotodo.statistics;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.base.MyApp;

/**
 * Created by dllo on 16/7/21.
 * 目标设置
 */
public class ObjectOptionsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvReturn;
    private LinearLayout mLayoutDay, mLayoutWeek, mLayouMonth;
    private PopupWindow mPopupWindow;

    @Override
    public int initView() {
        return R.layout.activity_object_options;
    }

    @Override
    public void initData() {
        mTvReturn = (TextView) findViewById(R.id.tv_options_return);
        mLayoutDay = (LinearLayout) findViewById(R.id.ll_object_day);
        mLayoutWeek = (LinearLayout) findViewById(R.id.ll_object_week);
        mLayouMonth = (LinearLayout) findViewById(R.id.ll_object_month);
        mTvReturn.setOnClickListener(this);
        mLayoutDay.setOnClickListener(this);
        mLayouMonth.setOnClickListener(this);
        mLayoutWeek.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_options_return:
                finish();
                break;

            case R.id.ll_object_day:
                showPopupWindow();
                break;

            case R.id.ll_object_week:

                break;

            case R.id.ll_object_month:

                break;

        }
    }

    private void showPopupWindow() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View v = LayoutInflater.from(MyApp.context).inflate(R.layout.popopwindow_object, null);
        TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero, tvSure, tvCancle;

        tvOne = (TextView) v.findViewById(R.id.tv_object_options_one);
        tvTwo = (TextView) v.findViewById(R.id.tv_object_options_two);
        tvThree = (TextView) v.findViewById(R.id.tv_object_options_three);
        tvFour = (TextView) v.findViewById(R.id.tv_object_options_four);
        tvFive = (TextView) v.findViewById(R.id.tv_object_options_five);
        tvSix = (TextView) v.findViewById(R.id.tv_object_options_six);
        tvSeven = (TextView) v.findViewById(R.id.tv_object_options_seven);
        tvEight = (TextView) v.findViewById(R.id.tv_object_options_eight);
        tvNine = (TextView) v.findViewById(R.id.tv_object_options_nine);
        tvZero = (TextView) v.findViewById(R.id.tv_object_options_zero);
        tvCancle = (TextView) v.findViewById(R.id.tv_object_options_cancel);
        tvSure = (TextView) v.findViewById(R.id.tv_object_options_sure);

        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvSix.setOnClickListener(this);
        tvSeven.setOnClickListener(this);
        tvEight.setOnClickListener(this);
        tvNine.setOnClickListener(this);
        tvZero.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        tvSure.setOnClickListener(this);



        mPopupWindow.setContentView(v);
        mPopupWindow.showAtLocation(mTvReturn, Gravity.TOP, 0, 0);
    }
}
