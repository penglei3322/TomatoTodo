package com.example.dllo.tomatotodo.history;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dllo on 16/7/18.
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    private HashMap<Integer, String> map;
    private ListView historyList;
    private HistoryAdapter historyAdapter;
    private TextView leftTv, centerTv, rightTv;
    private View historyView;
    private String date;// 当时的日期,用作点击事件判断
    private int key;// map里日期的key
    private int temp = 0;// 记录点击的次数

    private ArrayList<String> datas;

    @Override
    public int createView() {
        return R.layout.fragment_history;
    }

    @Override
    public void initView(View view) {
        historyList = (ListView) view.findViewById(R.id.history_list);
        leftTv = (TextView) view.findViewById(R.id.history_left_tv);
        centerTv = (TextView) view.findViewById(R.id.history_center_tv);
        rightTv = (TextView) view.findViewById(R.id.history_right_tv);
        historyView = (View) view.findViewById(R.id.history_view);
        historyAdapter = new HistoryAdapter(context);
        datas = new ArrayList<>();
    }

    @Override
    public void initData() {


        setText();

        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);

        ArrayList<HistoryBean> datas = (ArrayList<HistoryBean>) buildLocalDatas();
        historyAdapter.setHistoryBeen(datas);
        historyList.setAdapter(historyAdapter);
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


//        historyView.setLayoutParams(new LayoutParams(centerTv.getWidth()));
        // 设置试图最小宽度
//        historyView.setMinimumWidth(centerTv.getWidth());
    }

    private List<HistoryBean> buildLocalDatas() {

        ArrayList<HistoryBean> historyBeen = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        String[] months = new String[30];
        StringBuilder dates = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            months[i] = new SimpleDateFormat("MM").format(new Date(c.getTimeInMillis()));
            c.add(Calendar.DAY_OF_MONTH, -1);
        }

        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "1"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈",  "1"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "1"));

        return historyBeen;
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

                break;
        }
    }
}
