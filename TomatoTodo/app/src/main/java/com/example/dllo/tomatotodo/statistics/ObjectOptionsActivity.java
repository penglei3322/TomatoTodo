package com.example.dllo.tomatotodo.statistics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.base.MyApp;

/**
 * Created by dllo on 16/7/21.
 * 目标设置
 */
public class ObjectOptionsActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvReturn;
    private TextView mTvObjectDay, mTvObjectWeek, mTvObjectMonth;
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
        mTvObjectDay = (TextView) findViewById(R.id.tv_object_day);
        mTvObjectWeek = (TextView) findViewById(R.id.tv_object_week);
        mTvObjectMonth = (TextView) findViewById(R.id.tv_object_month);
        mTvReturn.setOnClickListener(this);
        mLayoutDay.setOnClickListener(this);
        mLayouMonth.setOnClickListener(this);
        mLayoutWeek.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("object",MODE_PRIVATE);
        mTvObjectDay.setText(sp.getInt("objectDay",8)+"");
        mTvObjectWeek.setText(sp.getInt("objectWeek",40)+"");
        mTvObjectMonth.setText(sp.getInt("objectMonth",160)+"");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_options_return:
                //通知之前的activity刷新目标
                Intent intent = new Intent("refresh");
                sendBroadcast(intent);
                finish();
                break;

            case R.id.ll_object_day:
                showPopupWindow(1);
                break;

            case R.id.ll_object_week:
                showPopupWindow(2);
                break;

            case R.id.ll_object_month:
                showPopupWindow(3);
                break;


        }
    }

    private void showPopupWindow(final int temp) {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View v = LayoutInflater.from(MyApp.context).inflate(R.layout.popopwindow_object, null);
        final TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero, tvSure, tvCancle;
        final EditText etNum;
        ImageView ivDelete;

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
        etNum = (EditText) v.findViewById(R.id.et_object_select);
        ivDelete = (ImageView) v.findViewById(R.id.delete_object_select);

        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ObjectOptionsActivity.this, "11:" + 11, Toast.LENGTH_SHORT).show();
            }
        });

        View[] onclickView = {tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero, tvCancle, tvSure, etNum, ivDelete};

        for (int i = 0; i < onclickView.length; i++) {
            onclickView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tv_object_options_one:
                            etNum.setText(etNum.getText().append(tvOne.getText().toString()));
                            break;

                        case R.id.tv_object_options_two:
                            etNum.setText(etNum.getText().append(tvTwo.getText().toString()));
                            break;

                        case R.id.tv_object_options_three:
                            etNum.setText(etNum.getText().append(tvThree.getText().toString()));
                            break;

                        case R.id.tv_object_options_four:
                            etNum.setText(etNum.getText().append(tvFour.getText().toString()));
                            break;

                        case R.id.tv_object_options_five:
                            etNum.setText(etNum.getText().append(tvFive.getText().toString()));
                            break;

                        case R.id.tv_object_options_six:
                            etNum.setText(etNum.getText().append(tvSix.getText().toString()));
                            break;

                        case R.id.tv_object_options_seven:
                            etNum.setText(etNum.getText().append(tvSeven.getText().toString()));
                            break;

                        case R.id.tv_object_options_eight:
                            etNum.setText(etNum.getText().append(tvEight.getText().toString()));
                            break;

                        case R.id.tv_object_options_nine:
                            etNum.setText(etNum.getText().append(tvNine.getText().toString()));
                            break;

                        case R.id.tv_object_options_zero:
                            etNum.setText(etNum.getText().append(tvZero.getText().toString()));
                            break;

                        case R.id.tv_object_options_cancel:
                            mPopupWindow.dismiss();
                            break;

                        case R.id.tv_object_options_sure:
                            SharedPreferences sp = getSharedPreferences("object", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            if (temp == 1) {
                                mTvObjectDay.setText(etNum.getText().toString());
                                editor.putInt("objectDay", Integer.parseInt(etNum.getText().toString()));
                            } else if (temp == 2) {
                                mTvObjectWeek.setText(etNum.getText().toString());
                                editor.putInt("objectWeek", Integer.parseInt(etNum.getText().toString()));
                            } else {
                                mTvObjectMonth.setText(etNum.getText().toString());
                                editor.putInt("objectYear", Integer.parseInt(etNum.getText().toString()));
                            }
                            editor.commit();

                            mPopupWindow.dismiss();
                            break;

                        case R.id.delete_object_select:
                            if (etNum.getText().toString().isEmpty()){
                                return;
                            }
                            int lenth = etNum.getText().length();
                            etNum.setText(etNum.getText().toString().substring(0, lenth-1));
                            break;


                    }
                }
            });

        }


        mPopupWindow.setContentView(v);
        mPopupWindow.showAtLocation(mTvReturn, Gravity.TOP, 0, 0);
    }
}
