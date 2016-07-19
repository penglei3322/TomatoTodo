package com.example.dllo.tomatotodo.potatolist;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;
import com.example.dllo.tomatotodo.base.CommonAdapter;
import com.example.dllo.tomatotodo.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 16/7/16.
 */
public class PotatoListFragment extends BaseFragment {

    private ListView listView;
    private LinearLayout addLinearLayout;
    private PopupWindow popupWindow;
    private List<String> datas;
    private CheckBox finishCb, toTpCb;
    private RecyclerView recyclerView;

    @Override
    public int createView() {
        return R.layout.fragment_potatolist;
    }

    @Override
    public void initView(View view) {
        listView = (ListView) view.findViewById(R.id.fragment_potatolist_listview);
        addLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_potatolist_add_linearlayout);
        datas = new ArrayList<>();
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
                popupView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        EditText editText = (EditText) popupView.findViewById(R.id.add_et);
                        addLinearLayout.setVisibility(View.VISIBLE);
                        String number = editText.getText().toString();
                        if (number.length() != 0) {
                            datas.add(number);
                        }

                        listView.setAdapter(new CommonAdapter<String>(context, datas, R.layout.item_potatolist_fragment) {
                            @Override
                            public void convert(final ViewHolder holder, final String s) {
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
                        });
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
    public void initData() {

    }

}
