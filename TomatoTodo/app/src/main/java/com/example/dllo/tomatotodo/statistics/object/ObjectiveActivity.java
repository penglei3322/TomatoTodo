package com.example.dllo.tomatotodo.statistics.object;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.custom.CustomGridViewPoint;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.db.HistoryAllBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dllo on 16/7/19.
 */
public class ObjectiveActivity extends BaseActivity {

    private ArrayList<HistoryAllBean> monthAgoList;

    private GridView mGridViewLastMonth, mGridViewTwelveWeek, mGridViewTwelveMonth;
    private GridViewAdapter adapter;

    private boolean isFirst = true;


    @Override
    public int initView() {
        return R.layout.activity_object;
    }

    @Override
    public void initData() {
        mGridViewLastMonth = (GridView) findViewById(R.id.gridViewLastMonth);
        mGridViewLastMonth.setVerticalSpacing(30);
        adapter = new GridViewAdapter(this);
        getLastThirtyData();
        adapter.setOnOtherItemClickListener(new GridViewAdapter.OnOtherItemClickListener() {
            @Override
            public void onOtherItemClick(CustomGridViewPoint customGridViewPoint) {
                if (!isFirst) {
                    customGridViewPoint.setTouch(false);
                    isFirst = false;
                }
                isFirst = false;
            }
        });
    }

    public void getLastThirtyData(){
        //30天前 获取30天前的日期(Java)
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        theCa.add(theCa.DATE, -30);
        Date date = theCa.getTime();
        //转型
        long mouthAgo = date.getTime();
//        Log.d("StatisticsFragment", "mouthAgo:" + mouthAgo);
        //根据时间遍历出所有符合条件的数据
        monthAgoList = new ArrayList();
        for (HistoryAllBean historyAllBean : DBTools.getInstance(this).queryAll(HistoryAllBean.class)) {
            if (historyAllBean.getStartTime() > mouthAgo) {
                monthAgoList.add(historyAllBean);
            }
        }
        adapter.setBeanArrayList(monthAgoList,1);
        mGridViewLastMonth.setAdapter(adapter);

    }
}
