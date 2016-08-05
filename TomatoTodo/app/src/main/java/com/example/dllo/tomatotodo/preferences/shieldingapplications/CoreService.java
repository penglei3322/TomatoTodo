package com.example.dllo.tomatotodo.preferences.shieldingapplications;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.dllo.tomatotodo.preferences.shieldingapplications.util.BlockUtils;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.util.Logger;
import com.example.dllo.tomatotodo.preferences.shieldingapplications.util.TopActivityUtils;

import java.util.ArrayList;

/**
 * Created by Clark on 2016/7/21.
 * <p/>
 * 后台默默运行着的Service
 */
public class CoreService extends Service {

    private static final int delayMillis = 1000;

    private Context mContext;

    // 系统中所有运行着的Activity交互提供了接口
    private ActivityManager mActivityManager;

    private Handler mHandler;

    private ArrayList<String> mBlockList = null;

    private Runnable mRunnable = new Runnable() {

        public void run() {

            Logger.getLogger().d("block service is running...");

            mBlockList = BlockUtils.getBlockList(getApplicationContext());

            String packageName = null;

            // ComponentName（组件名称）是用来打开其他应用程序中的Activity或服务的
            ComponentName componentName = TopActivityUtils.getTopActivity(mContext, mActivityManager);

            if (componentName != null) {
                packageName = componentName.getPackageName();
//                Logger.getLogger().i("packageName:" + packageName);
                Log.d("CoreService","当前包名" + packageName);
            } else {
                Logger.getLogger().e("getTopActivity Error!");
            }

            if (mBlockList != null && mBlockList.contains(packageName)) {
                Log.d("CoreService", "应该跳转");
                Intent intent = new Intent(getApplicationContext(), WarningActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            // 要做的事情，这里再次调用此Runnable对象，以实现每一秒实现一次的定时器操作
            mHandler.postDelayed(mRunnable, delayMillis);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("CoreService", "服务已开启");
        mContext = this;
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        mHandler = new Handler();
        // 要做的事情，这里再次调用此Runnable对象，以实现每一秒实现一次的定时器操作
        mHandler.postDelayed(mRunnable, delayMillis);

        Logger.getLogger().i("onCreate");
    }

    @Override
    public void onDestroy() {

        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();

        Logger.getLogger().i("onDestroy");
    }
}