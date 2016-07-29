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
    private ActivityManager mActivityManager;

    private Handler mHandler;

    private ArrayList<String> mBlockList = null;

    private Runnable mRunnable = new Runnable() {

        public void run() {

            Logger.getLogger().d("block service is running...");

            mBlockList = BlockUtils.getBlockList(getApplicationContext());

            String packageName = null;

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