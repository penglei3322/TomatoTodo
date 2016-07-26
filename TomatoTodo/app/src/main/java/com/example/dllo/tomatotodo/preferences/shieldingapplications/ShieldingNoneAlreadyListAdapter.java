package com.example.dllo.tomatotodo.preferences.shieldingapplications;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.util.BlockUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/7/23.
 */
public class ShieldingNoneAlreadyListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<PackageInfo> mPackageInfoList = null;
    private ArrayList<String> mCheckedList = null;

    public ShieldingNoneAlreadyListAdapter(Activity mActivity, List<PackageInfo> mPackageInfoList, ArrayList<String> mCheckedList) {
        this.mActivity = mActivity;
        this.mPackageInfoList = mPackageInfoList;
        this.mCheckedList = mCheckedList;
    }

    public ArrayList<String> getCheckedList() {
        return mCheckedList;
    }

    @Override
    public int getCount() {
        return mPackageInfoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mPackageInfoList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_shielding_none_already_list, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final PackageInfo packageInfo = mPackageInfoList.get(position);

        mViewHolder.textView.setText(packageInfo.applicationInfo.loadLabel(mActivity.getPackageManager()).toString());
        Drawable drawable = packageInfo.applicationInfo.loadIcon(mActivity.getPackageManager());
        mViewHolder.imageView.setBackgroundDrawable(drawable);

        if (contains(mCheckedList, packageInfo)) {
            mViewHolder.checkBox.setChecked(true);
        }else {
            mViewHolder.checkBox.setChecked(false);
        }
        mViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contains(mCheckedList, packageInfo)) {
                    remove(mCheckedList, packageInfo);
                    Log.d("Clark", "mCheckedList remove:" + mCheckedList.size());
                } else {

                    mCheckedList.add(packageInfo.packageName);
                    Log.d("Clark", "mCheckedList: add" + mCheckedList.size());
                }
                BlockUtils.save(mActivity, mCheckedList);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private boolean contains(ArrayList<String> list, PackageInfo item) {
        if (list == null || item == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).equals(item.packageName)) {
                return true;
            }
        }
        return false;
    }

    private void remove(ArrayList<String> list, PackageInfo item) {
        if (list == null || item == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).equals(item.packageName)) {
                list.remove(i);
                break;
            }
        }
    }

    class ViewHolder {

        CheckBox checkBox;
        TextView textView;
        ImageView imageView;

        public ViewHolder(View view){

            checkBox = (CheckBox) view.findViewById(R.id.app_check_box);
            textView = (TextView) view.findViewById(R.id.app_name);
            imageView = (ImageView) view.findViewById(R.id.app_logo);
        }
    }
}
