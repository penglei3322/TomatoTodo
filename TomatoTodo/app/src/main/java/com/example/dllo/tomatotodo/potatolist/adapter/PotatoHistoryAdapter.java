package com.example.dllo.tomatotodo.potatolist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.example.dllo.tomatotodo.potatolist.data.PotatolistChildData;
import com.example.dllo.tomatotodo.potatolist.data.PotatolistGroupData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dllo on 16/6/20.
 */
public class PotatoHistoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<PhtatoListData> datas;
    private List<PotatolistGroupData> groupDatas;
    private String today;


    private Map<PotatolistGroupData, List<PotatolistChildData>> all;

    public PotatoHistoryAdapter(Context context) {
        this.context = context;
    }


    public void setDatas(List<PhtatoListData> datas) {
        this.datas = datas;
        groupDatas = new ArrayList<>();
        all = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            PotatolistGroupData potatolistGroupData = null;
            PhtatoListData data = datas.get(i);
            for (PotatolistGroupData groupData : groupDatas) {
                if (groupData.isThisDay(data.getMonth(), data.getDay(), data.getWeeks())) {
                    potatolistGroupData = groupData;
                }
            }

            if (potatolistGroupData == null) {
                potatolistGroupData = new PotatolistGroupData();
                potatolistGroupData.setWeeks(data.getWeeks())
                        .setMonth(data.getMonth())
                        .setDays(data.getDay());
                groupDatas.add(potatolistGroupData);

                all.put(potatolistGroupData, new ArrayList<PotatolistChildData>());
            }

            List<PotatolistChildData> childDatas = all.get(potatolistGroupData);
            PotatolistChildData potatolistChildData = new PotatolistChildData();
            potatolistChildData.setContent(data.getContent());
            potatolistChildData.setMinute(data.getMinute());
            potatolistChildData.setHour(data.getHour());

            childDatas.add(potatolistChildData);
            potatolistGroupData.setCount();

        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groupDatas == null ? 0 : groupDatas.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        List<PotatolistChildData> potatolistChildDatas = all.get(groupDatas.get(groupPosition));
        return potatolistChildDatas.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupDatas.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return all.get(groupDatas.get(groupPosition)).get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupHolder = null;
        PotatolistGroupData groupData = groupDatas.get(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_potatolist_history, parent, false);
            groupHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupViewHolder) convertView.getTag();
        }
        groupHolder.monthTv.setText(groupData.getMonth() + 1 + "月");
        groupHolder.daysTv.setText(groupData.getDays() + "日");
        groupHolder.countTv.setText(groupData.getCount() + "");
        if (groupData.getWeeks() == 1) {
            today = "周一";
        } else if (groupData.getWeeks() == 2) {
            today = "周二";
        } else if (groupData.getWeeks() == 3) {
            today = "周三";
        } else if (groupData.getWeeks() == 4) {
            today = "周四";
        } else if (groupData.getWeeks() == 5) {
            today = "周五";
        } else if (groupData.getWeeks() == 6) {
            today = "周六";
        } else if (groupData.getWeeks() == 7) {
            today = "周日";
        }
        // groupHolder.weeksTv.setText("周 " + groupData.getWeeks());
        groupHolder.weeksTv.setText(today);
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PotatolistChildData childData = all.get(groupDatas.get(groupPosition)).get(childPosition);

        ChildViewHolder childHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_potatolist_history_child, parent, false);
            childHolder = new ChildViewHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildViewHolder) convertView.getTag();
        }

        childHolder.contentTv.setText(childData.getContent());
        childHolder.hourTv.setText(childData.getHour() + ":");
        childHolder.minuteTv.setText(childData.getMinute() + "");
        childHolder.checkBox.setChecked(true);
        final ChildViewHolder finalChildHolder = childHolder;
        childHolder.deleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<PotatolistChildData> potatolistChildDatas = all.get(groupDatas.get(groupPosition));
                final PotatolistChildData potatolistChildData = potatolistChildDatas.get(childPosition);

                Snackbar.make(finalChildHolder.deleIv, "已删除" + potatolistChildData.getContent(),
                        Snackbar.LENGTH_LONG).setAction("撤销删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        potatolistChildDatas.add(childPosition, potatolistChildData);
                        delete = false;
                        //DBTools.getInstance(context).insertSingle(potatolistChildData);
                        notifyDataSetChanged();
                    }
                }).setActionTextColor(Color.RED).show();


                deleteFromDb(potatolistChildData);
                potatolistChildDatas.remove(potatolistChildData);
                notifyDataSetChanged();

            }
        });
        return convertView;
    }
    public boolean delete = true;

    public void deleteFromDb(final PotatolistChildData potatolistChildData){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(delete)
                DBTools.getInstance(context).deleteCondition(PhtatoListData.class, "content",
                        potatolistChildData.getContent());
                delete = true;
            }
        }).start();

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        TextView monthTv, daysTv, countTv, weeksTv;

        public GroupViewHolder(View view) {
            monthTv = (TextView) view.findViewById(R.id.item_aty_potato_history_month);
            daysTv = (TextView) view.findViewById(R.id.item_aty_potato_history_days);
            countTv = (TextView) view.findViewById(R.id.item_aty_potato_history_count);
            weeksTv = (TextView) view.findViewById(R.id.item_aty_potato_history_weeks);
        }
    }

    class ChildViewHolder {
        TextView contentTv, hourTv, minuteTv;
        ImageView deleIv;
        CheckBox checkBox;

        public ChildViewHolder(View view) {
            contentTv = (TextView) view.findViewById(R.id.item_potatolist_history_child_content);
            hourTv = (TextView) view.findViewById(R.id.item_hour_potatolist_tv);
            minuteTv = (TextView) view.findViewById(R.id.item_minute_potatolist_tv);
            deleIv = (ImageView) view.findViewById(R.id.item_potato_dele_iv);
            checkBox = (CheckBox) view.findViewById(R.id.item_potatolist_history_child_checkbox);
        }
    }
}

