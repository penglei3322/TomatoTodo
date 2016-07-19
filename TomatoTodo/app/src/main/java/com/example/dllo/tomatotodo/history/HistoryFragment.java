package com.example.dllo.tomatotodo.history;

import android.support.design.widget.TabLayout;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dllo on 16/7/18.
 */
public class HistoryFragment extends BaseFragment {

    private TabLayout historyTab;
    private HashMap<Integer, String> map;
    private ListView historyList;
    private HistoryAdapter historyAdapter;
    private ArrayList<String> datas;

    @Override
    public int createView() {
        return R.layout.fragment_history;
    }

    @Override
    public void initView(View view) {

        historyList = (ListView) view.findViewById(R.id.history_list);
        historyAdapter = new HistoryAdapter(context);
        datas = new ArrayList<>();
    }

    @Override
    public void initData() {


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

        ArrayList<HistoryBean> datas = (ArrayList<HistoryBean>) buildLocalDatas();
        historyAdapter.setHistoryBeen(datas);
        historyList.setAdapter(historyAdapter);
    }

    private List<HistoryBean> buildLocalDatas() {

        ArrayList<HistoryBean> historyBeen = new ArrayList<>();

        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "测试", "内容", "哈哈哈", "0"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "测试", "内容", "哈哈哈", "1"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "测试", "内容", "哈哈哈", "1"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "测试", "内容", "哈哈哈", "0"));
        historyBeen.add(new HistoryBean("测试", "内容", "哈哈哈", "测试", "内容", "哈哈哈", "1"));

        return historyBeen;
    }
}
