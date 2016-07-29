package com.example.dllo.tomatotodo.potatolist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.example.dllo.tomatotodo.potatolist.data.PotatolistChildData;
import com.example.dllo.tomatotodo.potatolist.data.PotatolistGroupData;

import java.util.ArrayList;
import java.util.Calendar;
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
    // private List<PotatolistChildData> allDatas;

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

//            PotatolistGroupData data = new PotatolistGroupData();
//            PotatolistChildData childData = new PotatolistChildData();
//            data.setMonth(datas.get(i).getMonth());
//            data.setDays(datas.get(i).getDay());
//            data.setWeeks(datas.get(i).getWeeks());
//            childData.setContent(datas.get(i).getContent());
//            childData.setHour(datas.get(i).getHour());
//            childData.setMinute(datas.get(i).getMinute());
//            groupDatas.add(data);
//            allDatas.add(childData);


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
        groupHolder.weeksTv.setText("周 " + groupData.getWeeks());
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PotatolistChildData childData = all.get(groupDatas.get(groupPosition)).get(childPosition);

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
        childHolder.deleIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PhtatoListData data = datas.get(childPosition);
                // DBTools.getInstance(context).deleteCondition(PhtatoListData.class, "content", data.getContent());
                Log.d("PotatoHistoryAdapter", "点点");
                datas.remove(childPosition);
                notifyDataSetChanged();
            }
        });
        return convertView;
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

        public ChildViewHolder(View view) {
            contentTv = (TextView) view.findViewById(R.id.item_potatolist_history_child_content);
            hourTv = (TextView) view.findViewById(R.id.item_hour_potatolist_tv);
            minuteTv = (TextView) view.findViewById(R.id.item_minute_potatolist_tv);
            deleIv = (ImageView) view.findViewById(R.id.item_potato_dele_iv);
        }
    }
}

