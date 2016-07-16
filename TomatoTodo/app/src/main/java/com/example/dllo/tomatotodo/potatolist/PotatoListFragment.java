package com.example.dllo.tomatotodo.potatolist;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseFragment;

/**
 * Created by dllo on 16/7/16.
 */
public class PotatoListFragment extends BaseFragment {

    private ListView listView;
    private LinearLayout linearLayout;

    @Override
    public int createView() {
        return 0;
    }

    @Override
    public void initView(View view) {
        listView = (ListView) view.findViewById(R.id.fragment_potatolist_listview);
        linearLayout = (LinearLayout) view.findViewById(R.id.fragment_potatolist_add_linearlayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                View popupView = LayoutInflater.from(context).inflate(R.layout.fragment_potatolist_add_popupwindow, null);
                popupWindow.setContentView(popupView);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());


            }
        });
    }

    @Override
    public void initData() {

    }
}
