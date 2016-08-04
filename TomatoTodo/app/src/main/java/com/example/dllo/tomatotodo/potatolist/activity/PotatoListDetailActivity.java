package com.example.dllo.tomatotodo.potatolist.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.main.MainActivity;
import com.example.dllo.tomatotodo.potatolist.adapter.PotatoHistoryAdapter;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.example.dllo.tomatotodo.potatolist.data.PotatolistChildData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 16/7/19.
 */
public class PotatoListDetailActivity extends BaseActivity {
    private FloatingGroupExpandableListView listView;
    private PotatoHistoryAdapter adapter;

    private ImageView backIv;

    @Override
    public int initView() {
        return R.layout.activity_potatolist_detail;
    }

    @Override
    public void initData() {
        backIv = (ImageView) findViewById(R.id.acy_potatolist_details_back_iv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PotatoListDetailActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_out_to_bottom, R.anim.slide_in_from_bottom);
            }
        });
        listView = (FloatingGroupExpandableListView) findViewById(R.id.activity_potatolist_details_listview);
        adapter = new PotatoHistoryAdapter(this);
        //DBTools.getInstance(this).queryCondition(PhtatoListData.class, "Checked",);
        adapter.setDatas(DBTools.getInstance(this).queryChecked(PhtatoListData.class, "Checked", true));
        WrapperExpandableListAdapter wrapperExpandableListAdapter = new WrapperExpandableListAdapter(adapter);
        listView.setAdapter(wrapperExpandableListAdapter);
        setExpandableListView();//设置expandableListView

    }

    public void setExpandableListView() {
        int count = listView.getCount();
        for (int i = 0; i < count; i++) {
            listView.expandGroup(i);
        }
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }
}
