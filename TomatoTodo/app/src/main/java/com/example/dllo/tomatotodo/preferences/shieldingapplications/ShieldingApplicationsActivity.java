package com.example.dllo.tomatotodo.preferences.shieldingapplications;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.util.BlockUtils;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.util.TopActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class ShieldingApplicationsActivity extends BaseActivity implements View.OnClickListener {

    private ListView alreadyList, noneAlreadyList;
    private ImageView returnIv;
    private Switch shieldingSwitch;
    private ShieldingNoneAlreadyListAdapter mAdapter;
    private LinearLayout alreadyListLayout;
    private static final int REQUEST_SETTING = 1;

    @Override
    public int initView() {
        return R.layout.activity_shielding_applications;
    }

    @Override
    public void initData() {

        alreadyList = (ListView) findViewById(R.id.shielding_already_list);
        noneAlreadyList = (ListView) findViewById(R.id.shielding_none_already_list);
        returnIv = (ImageView) findViewById(R.id.shielding_return_iv);
        alreadyListLayout = (LinearLayout) findViewById(R.id.shielding_already_list_layout);
        shieldingSwitch = (Switch) findViewById(R.id.shielding_switch);

        returnIv.setOnClickListener(this);

        // 判断已屏蔽ListView是否为空
        if (alreadyList.getCount() != 0) {
            alreadyListLayout.setVisibility(View.VISIBLE);
            alreadyList.setVisibility(View.VISIBLE);
        }

        // 找出系统应用设置数据
        setPackInfo();
        // 屏蔽应用
        shieldingApplications();
    }

    private void shieldingApplications() {

        shieldingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (BlockUtils.isBlockServiceRunning(ShieldingApplicationsActivity.this, CoreService.class)) {

                    Intent intent = new Intent();
                    intent.setClass(ShieldingApplicationsActivity.this, CoreService.class);
                    stopService(intent);
                } else {
                    if (!TopActivityUtils.isStatAccessPermissionSet(ShieldingApplicationsActivity.this)) {
                        showDialog();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(ShieldingApplicationsActivity.this, CoreService.class);
                        startService(intent);
                    }
                }
            }
        });
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // some rom has removed the newly introduced android.settings.USAGE_ACCESS_SETTINGS
                        try {
                            startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), REQUEST_SETTING);
                        } catch (Exception e) {
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.shielding_return_iv:
                finish();
                break;
        }
    }

    private void setPackInfo() {

        List<PackageInfo> appList = getPackageManager().getInstalledPackages(0);
        List<PackageInfo> installedList = new ArrayList<PackageInfo>();

        for (PackageInfo packageInfo : appList) {

            if (!isSystemPackage(packageInfo) && !getApplicationInfo().packageName.equals(packageInfo.packageName)) {
                installedList.add(packageInfo);
            }
        }
        mAdapter = new ShieldingNoneAlreadyListAdapter(this, installedList, BlockUtils.getBlockList(getApplicationContext()));
        noneAlreadyList.setAdapter(mAdapter);
    }

    private boolean isSystemPackage(PackageInfo packageInfo) {
        // 判断是否为非系统预装的应用程序
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
