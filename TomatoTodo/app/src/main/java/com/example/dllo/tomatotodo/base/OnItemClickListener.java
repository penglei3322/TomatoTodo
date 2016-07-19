package com.example.dllo.tomatotodo.base;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ChenFengYao on 16/4/10.
 * 监听接口
 */
public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}