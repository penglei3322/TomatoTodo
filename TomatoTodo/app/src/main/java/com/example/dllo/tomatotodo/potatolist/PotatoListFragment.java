package com.example.dllo.tomatotodo.potatolist;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;
import com.example.dllo.tomatotodo.base.RecyclerViewCommonAdapter;
import com.example.dllo.tomatotodo.base.ViewHolder;
import com.example.dllo.tomatotodo.potatolist.activity.PotatoListDetailActivity;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.example.dllo.tomatotodo.potatolist.tools.OnRecyclerItemClickListener;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.decoration.DividerItemDecoration;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dllo on 16/7/16.
 */
public class PotatoListFragment extends BaseFragment {

    private LinearLayout addLinearLayout;// 添加数据的textview
    private PopupWindow popupWindow;
    private List<String> datas;
    private CheckBox finishCb, toTpCb;
    private RecyclerView mRecyclerView;
    private RecyclerViewCommonAdapter<String> adapter;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout footerLinearLayout;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;


    private RecyclerView recyclerView;


    @Override
    public int createView() {
        return R.layout.fragment_potatolist;

    }


    @Override
    public void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_potatolist_recyclerview);

        datas = new ArrayList<>();
        addTotatoList();
        moveItem();
        add();


    }

    @Override
    public void initData() {

    }

    // 拖拽item

    public void moveItem() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlag;
                int swipFlag = 0;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else {
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipFlag = ItemTouchHelper.END;// 触发滑动方向
                }
                return makeMovementFlags(dragFlag, swipFlag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getLayoutPosition();
                int toPos = target.getLayoutPosition();
                Collections.swap(datas, fromPos, toPos);
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
                    viewHolder.itemView.setBackgroundColor(Color.RED);
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
        adapter = new RecyclerViewCommonAdapter<String>(context, R.layout.item_potatolist_fragment, datas) {
            @Override
            public void convert(final ViewHolder holder, String s) {
                holder.setText(R.id.item_potatolist_tv, s);
                finishCb = holder.getView(R.id.item_potatolist_finish_checkbox);
                toTpCb = holder.getView(R.id.item_potatolist_totop_checkbox);
                finishCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        toTpCb = holder.getView(R.id.item_potatolist_totop_checkbox);
                        if (isChecked) {

                            toTpCb.setVisibility(View.INVISIBLE);
                            holder.setTextColor(R.id.item_potatolist_tv, Color.GRAY);
                        } else {
                            toTpCb.setVisibility(View.VISIBLE);
                            holder.setTextColor(R.id.item_potatolist_tv, Color.BLACK);
                        }
                    }
                });
            }
        };
        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLinearLayout.setVisibility(View.INVISIBLE);
                popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                final View popupView = LayoutInflater.from(context).inflate(R.layout.fragment_potatolist_add_popupwindow, null);
                final LinearLayout linearLayout = (LinearLayout) popupView.findViewById(R.id.bg);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                layoutParams.topMargin = 320;
                linearLayout.setLayoutParams(layoutParams);
                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(adapter);
                // popupWindow 的点击事件  保存数据  添加到集合
                popupView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        EditText editText = (EditText) popupView.findViewById(R.id.add_et);
                        addLinearLayout.setVisibility(View.VISIBLE);
                        String number = editText.getText().toString();
                        if (number.length() != 0) {
                            adapter.addSingleData(number);
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

    public void add() {
        Log.d("PotatoListFragment", "走了吗");

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if (y1 - y2 > 70) {
                        Toast.makeText(context, "向上", Toast.LENGTH_SHORT).show();
                        Log.d("PotatoListFragment", "加载");
                        Intent intent = new Intent(context, PotatoListDetailActivity.class);
                        context.startActivity(intent);

                    } else if (y2 - y1 > 50) {
                        Toast.makeText(context, "向下", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;

            }
        });
    }
}

