package com.example.dllo.tomatotodo.potatolist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.main.MainActivity;
import com.example.dllo.tomatotodo.potatolist.activity.EditPotatolistActivity;
import com.example.dllo.tomatotodo.potatolist.adapter.PhtatoListAdapter;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.example.dllo.tomatotodo.potatolist.tools.PhtatolistListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by dllo on 16/7/16.
 */
public class PotatoListFragment extends BaseFragment implements PhtatolistListener {

    private LinearLayout addLinearLayout;// 添加数据的textview
    private PopupWindow popupWindow;
    private List<PhtatoListData> datas;
    private RecyclerView mRecyclerView;
    private PhtatoListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ViewPager viewPager;
    private int month = 1;
    private int day, hour, minute, weeks;
    private MyReceiver myReceiver;
    private PhtatoListData data;


    @Override
    public int createView() {
        return R.layout.fragment_potatolist;

    }


    @Override
    public void initView(View view) {


        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_potatolist_recyclerview);
        datas = new ArrayList<>();
        adapter = new PhtatoListAdapter(context);
        viewPager = ((MainActivity) getActivity()).getViewPager();
        adapter.setViewPager(viewPager);
        addTotatoList();
        moveItem();
    }

    @Override
    public void initData() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("editContent");
        context.registerReceiver(myReceiver, intentFilter);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setDatas(DBTools.getInstance(context).queryChecked(PhtatoListData.class, "topCheck", true));
        adapter.setDatas(DBTools.getInstance(context).queryChecked(PhtatoListData.class, "topCheck", false));
    }


    // 拖拽item
    public void moveItem() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlag;
                int swipFlag = 0;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    //上下左右拖拽
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else {
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    // 滑动删除  方向
                    //  swipFlag = ItemTouchHelper.START;// 触发滑动方向

                }
                return makeMovementFlags(dragFlag, swipFlag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getLayoutPosition();
                int toPos = target.getLayoutPosition();
                Collections.swap(DBTools.getInstance(context).queryAll(PhtatoListData.class), fromPos, toPos);
                adapter.notifyItemMoved(fromPos, toPos);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getLayoutPosition();
                adapter.notifyItemRemoved(pos);
                datas.remove(pos);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                //如果当前状态是空闲的
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    //  添加土豆
    public void addTotatoList() {
        addLinearLayout = (LinearLayout) getView().findViewById(R.id.fragment_potatolist_add_linearlayout);

        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLinearLayout.setVisibility(View.INVISIBLE);
                popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                int height = ((MainActivity) getActivity()).getHeight();
                final View popupView = LayoutInflater.from(context).inflate(R.layout.fragment_potatolist_add_popupwindow, null);
                final LinearLayout linearLayout = (LinearLayout) popupView.findViewById(R.id.bg);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                layoutParams.topMargin = height;
                linearLayout.setLayoutParams(layoutParams);

                // popupWindow 的点击事件  保存数据  添加到集合
                popupView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        EditText editText = (EditText) popupView.findViewById(R.id.add_et);
                        addLinearLayout.setVisibility(View.VISIBLE);
                        String number = editText.getText().toString();
                        if (number.length() != 0) {
                            data = new PhtatoListData();
                            data.setContent(number);
                            // 添加系统时间
                            Calendar calendar = Calendar.getInstance();
                            day = calendar.get(Calendar.DAY_OF_WEEK);
                            month = calendar.get(Calendar.MONTH);
                            hour = calendar.get(Calendar.HOUR);
                            minute = calendar.get(Calendar.MINUTE);
                            weeks = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                            data.setWeeks(weeks);
                            Log.d("PotatoListFragment", "weeks:" + weeks);
                            data.setMonth(month);
                            data.setDay(day);
                            data.setHour(hour);
                            data.setMinute(minute);
                            // 添加checkBox的选中状态
                            data.setIsItemChecked(false);
                            data.setTopCheck(false);
                            datas.add(data);
                            DBTools.getInstance(context).insertSingle(data);
                            adapter.addData(data);
                            //   adapter.setDatas(DBTools.getInstance(context).queryAll(PhtatoListData.class));
                        }
                    }
                });
                popupWindow.setContentView(popupView);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(popupView, 0, 0);
            }

        });
    }

    @Override
    public void onClick(int position) {

    }
//
//    private String getWeekDay(Calendar c) {
//        if (c == null) {
//            return "星期一";
//        }
//


    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.editData(DBTools.getInstance(context).queryAll(PhtatoListData.class));

            //adapter.editData(DBTools.getInstance(context).queryChecked(PhtatoListData.class, "topCheck", true));
            //adapter.editData(DBTools.getInstance(context).queryChecked(PhtatoListData.class,"topCheck",false));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(myReceiver);
    }

}

