package com.example.dllo.tomatotodo.potatolist;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
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
                        Log.d("PotatoListFragment", number + "");
                        datas.add(number + "");
                        listView.setAdapter(new CommonAdapter<String>(context, datas, R.layout.item_potatolist_fragment) {
                            @Override
                            public void convert(ViewHolder holder, String s) {
                                holder.setText(R.id.item_potatolist_tv, s);
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
