package com.example.dllo.tomatotodo.potatolist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;

import java.util.List;

/**
 * Created by dllo on 16/7/20.
 */
public class PhtatoListAdapter extends RecyclerView.Adapter<PhtatoListAdapter.MyViewHolder> {

    private List<PhtatoListData> datas;
    private Context context;

    public PhtatoListAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<PhtatoListData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_potatolist_fragment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(datas.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_potatolist_tv);
        }
    }

//    holder.setText(R.id.item_potatolist_tv, s);
//    finishCb = holder.getView(R.id.item_potatolist_finish_checkbox);// 关闭checkBox
//    toTpCb = holder.getView(R.id.item_potatolist_totop_checkbox);// 置顶CheckBox
//    //  点击关闭CheckBox  关闭
//    finishCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            toTpCb = holder.getView(R.id.item_potatolist_totop_checkbox);
//            if (isChecked) {
//                toTpCb.setVisibility(View.INVISIBLE);
//                holder.setTextColor(R.id.item_potatolist_tv, Color.GRAY);
//            } else {
//                toTpCb.setVisibility(View.VISIBLE);
//                holder.setTextColor(R.id.item_potatolist_tv, Color.BLACK);
}
