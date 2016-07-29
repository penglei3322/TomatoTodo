package com.example.dllo.tomatotodo.potatolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.example.dllo.tomatotodo.potatolist.tools.SlidingMenuView;

import java.util.Collections;
import java.util.List;

/**
 * Created by dllo on 16/7/20.
 */
public class PhtatoListAdapter extends RecyclerView.Adapter<PhtatoListAdapter.MyViewHolder> implements SlidingMenuView.SlidingListener {

    private List<PhtatoListData> datas;
    private Context context;
    private SlidingMenuView slidingMenuView;
    private int pos;
    private ViewPager viewPager;
    boolean isCheck, isTop;

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

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
        ViewGroup.LayoutParams layoutParams = holder.relativeLayout.getLayoutParams();
        layoutParams.width = com.example.dllo.tomatotodo.potatolist.tools.Utils.getScreenWidth(context);
        holder.relativeLayout.setLayoutParams(layoutParams);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textView.setText(datas.get(position).getContent());
        final PhtatoListData data = datas.get(position);
        DBTools.getInstance(context).queryAll(PhtatoListData.class);
        for (int i = 0; i < datas.size(); i++) {
            isCheck = data.isItemChecked();
            isTop = data.isTopCheck();
        }
        holder.okCheck.setChecked(isCheck);
        holder.topCheck.setChecked(isTop);
        if (data.isItemChecked()) {
            holder.topCheck.setVisibility(View.INVISIBLE);
            holder.textView.setTextColor(Color.GRAY);
        } else {
            holder.topCheck.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(Color.BLACK);
        }

        holder.okCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getLayoutPosition();
                PhtatoListData data = datas.get(pos);
                if (datas.get(pos).isItemChecked()) {
                    holder.topCheck.setVisibility(View.VISIBLE);
                    holder.textView.setTextColor(Color.BLACK);
                } else {
                    holder.topCheck.setVisibility(View.INVISIBLE);
                    holder.textView.setTextColor(Color.GRAY);
                }
                CheckBox checkBox = (CheckBox) v;
                data.setIsItemChecked(checkBox.isChecked());
                DBTools.getInstance(context).upDataSingle(data);
            }
        });
        holder.topCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getLayoutPosition();
                PhtatoListData data = datas.get(pos);
                CheckBox checkBox = (CheckBox) v;
                data.setTopCheck(checkBox.isChecked());
                if (!datas.get(pos).isTopCheck()) {
                    Collections.swap(DBTools.getInstance(context).queryAll(PhtatoListData.class), pos, datas.size() - 1);
                    notifyItemMoved(pos, datas.size() - 1);
                } else {
                    Collections.swap(datas, 0, pos);
                    notifyItemMoved(pos, 0);
                }

//TODO 更改数据库
                DBTools.getInstance(context).upDataSingle(data);

            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       int pos = holder.getLayoutPosition();
                                                       PhtatoListData data = datas.get(pos);
                                                       DBTools.getInstance(context).deleteCondition(PhtatoListData.class, "content", data.getContent());
                                                       datas.remove(pos);
                                                       notifyItemRemoved(pos);
                                                   }
                                               }
        );
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.example.dllo.tomatotodo.potatolist.activity.EditPotatolistActivity.class);
                PhtatoListData data = datas.get(position);
                DBTools.getInstance(context).queryCondition(PhtatoListData.class, "content", data.getContent());
                intent.putExtra("content", data.getContent() + "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void onMove(SlidingMenuView slidingMenuView) {
        if (this.slidingMenuView != slidingMenuView) {
            if (this.slidingMenuView != null) {
                this.slidingMenuView.close();
            }
        }
    }

    @Override
    public void onMenuIsOpen(SlidingMenuView slidingMenuView) {
        this.slidingMenuView = slidingMenuView;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox okCheck, topCheck;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_weight);
            textView = (TextView) itemView.findViewById(R.id.item_potatolist_tv);
            okCheck = (CheckBox) itemView.findViewById(R.id.item_potatolist_finish_checkbox);
            topCheck = (CheckBox) itemView.findViewById(R.id.item_potatolist_totop_checkbox);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_potatolist_dele_linelayout);
            ((SlidingMenuView) itemView).setSlidingListener(PhtatoListAdapter.this).setVp(viewPager);
        }
    }

}