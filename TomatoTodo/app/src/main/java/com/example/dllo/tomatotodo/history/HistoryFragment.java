package com.example.dllo.tomatotodo.history;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ListView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 16/7/18.
 */
public class HistoryFragment extends BaseFragment {

    private TabLayout historyTab;
    private String[] tabNames = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    private ListView historyList;
    private HistoryAdapter historyAdapter;
    private ArrayList<String> datas;

    @Override
    public int createView() {
        return R.layout.fragment_history;
    }

    @Override
    public void initView(View view) {

        historyTab = (TabLayout) view.findViewById(R.id.history_tab);
        historyList = (ListView) view.findViewById(R.id.history_list);
        historyAdapter = new HistoryAdapter(context);
        datas = new ArrayList<>();
    }

    @Override
    public void initData() {

        for (int i = 0; i < tabNames.length; i++) {
            historyTab.addTab(historyTab.newTab().setText(tabNames[i]));
        }

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
