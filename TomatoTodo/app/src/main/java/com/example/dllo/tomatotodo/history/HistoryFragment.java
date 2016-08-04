package com.example.dllo.tomatotodo.history;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dllo on 16/7/18.
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    private HashMap<Integer, String> map;
    private TextView leftTv, centerTv, rightTv;
    private String date;// 当时的日期,用作点击事件判断
    private int key;// map里日期的key
    private int temp = 0;// 记录点击的次数
    private FloatingGroupExpandableListView mExpandableListView;
    private HistoryAdapter mHistoryAdapter;
    //查询需要月份的arrayList
    private ArrayList<HistoryAllBean> arrayListThisMonth;
    private FrameLayout mFrameLayout;

    @Override
    public int createView() {
        return R.layout.fragment_history;
    }

    @Override
    public void initView(View view) {
        leftTv = (TextView) view.findViewById(R.id.history_left_tv);
        centerTv = (TextView) view.findViewById(R.id.history_center_tv);
        rightTv = (TextView) view.findViewById(R.id.history_right_tv);
        mExpandableListView = (FloatingGroupExpandableListView) view.findViewById(R.id.history_list_view);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.history_fl);
        mHistoryAdapter = new HistoryAdapter(context);
    }

    @Override
    public void initData() {

        setText();
        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);


        arrayListThisMonth = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        //获取当前年月
        String dateThis = format.format(new Date());
        ArrayList<HistoryAllBean> list = (ArrayList<HistoryAllBean>) DBTools.getInstance(context).queryAll(HistoryAllBean.class);


        for (HistoryAllBean historyAllBean : list) {
            if (dateThis.equals(format.format(historyAllBean.getStartTime()))) {
                arrayListThisMonth.add(historyAllBean);
            }
        }

        if (arrayListThisMonth.size() == 0) {
            mExpandableListView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mExpandableListView.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.GONE);
            mHistoryAdapter.setBeans(arrayListThisMonth);
//        mHistoryAdapter.setBeans(DBTools.getInstance(context).queryCondition(HistoryAllBean.class, "startTime", ));
            WrapperExpandableListAdapter wrapperExpandableListAdapter = new WrapperExpandableListAdapter(mHistoryAdapter);
            mExpandableListView.setAdapter(wrapperExpandableListAdapter);
            // 设置expandableListView
            setExpandableListView();
        }
    }


    private void setExpandableListView() {

        int count = mExpandableListView.getCount();
        for (int i = 0; i < count; i++) {
            mExpandableListView.expandGroup(i);
        }
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ShowAlertDialog();

                return false;
            }
        });
    }

    private void setText() {

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

        // 根据当时时间的月份从hashMap中获取三个TextView的显示内容
        SimpleDateFormat format = new SimpleDateFormat("MM");
        date = format.format(new Date());
        Log.d("date", date);
        // 将String类型的date 转化成为int类型
        key = Integer.valueOf(date);
        Log.d("key", key + " ");
        // 根据转换的int类型key值拿到想要的月份
        centerTv.setText(map.get(key));
        leftTv.setText(map.get(key - 1));
        rightTv.setText(map.get(key + 1));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.history_left_tv:

                key--;// key值递减
                Log.d("key", key + "``");
                rightTv.setVisibility(View.VISIBLE);
                // 当点击到一月份时候,让TextView可以重新循环整个map
                if (key == 0) {
                    key = 12;
                    leftTv.setText(map.get(key));
                }
                // 当点击左侧TextView时,把中间的TextView中的值赋给右边的TextView
                rightTv.setText(centerTv.getText().toString());
                // 把左侧TextView中的值赋给中间TextView
                centerTv.setText(leftTv.getText().toString());
                // 左面的TextView get到新的值
                leftTv.setText(map.get(key - 1));// BUG!!!!!!!!!!!!!!!!!!!!!!
                temp++;// 每当点击一次左侧按钮,记录一次点击次数

                ClickChangeList(temp);
                break;

            case R.id.history_right_tv:

                if (temp == 0) {
                    rightTv.setVisibility(View.GONE);
                    centerTv.setText(map.get(key));
                    leftTv.setText(map.get(key - 1));
                }
                if (temp == 1) {
                    rightTv.setVisibility(View.GONE);
                    centerTv.setText(map.get(key));
                    leftTv.setText(map.get(key - 1));
                }
                key++;
                centerTv.setText(rightTv.getText().toString());
                leftTv.setText(map.get(key - 1));
                rightTv.setText(map.get(key + 1));
                temp--;

                ClickChangeList(temp);
                break;
        }
    }

    private void ClickChangeList(int temp) {

        arrayListThisMonth = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        //获取当前年月
        String dateThis = format.format(new Date());
        int left = Integer.valueOf(dateThis) - temp;
        ArrayList<HistoryAllBean> list = (ArrayList<HistoryAllBean>) DBTools.getInstance(context).queryAll(HistoryAllBean.class);


        for (HistoryAllBean historyAllBean : list) {
            if (String.valueOf(left).equals(format.format(historyAllBean.getStartTime()))) {
                arrayListThisMonth.add(historyAllBean);

            }

        }

        if (arrayListThisMonth.size() == 0) {
            mExpandableListView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mExpandableListView.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.GONE);
            mHistoryAdapter.setBeans(arrayListThisMonth);
            WrapperExpandableListAdapter wrapperExpandableListAdapter = new WrapperExpandableListAdapter(mHistoryAdapter);
            mExpandableListView.setAdapter(wrapperExpandableListAdapter);
            // 设置expandableListView
            setExpandableListView();
        }
    }

    private void ShowAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除");
        builder.setMessage("您可以点击确定来删除这条历史记录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
